# models.py
import time

import torch
import torch.nn as nn
from torch import optim
import random
from sentiment_data import *


class SentimentClassifier(object):
    """
    Sentiment classifier base type
    """
    

    def predict(self, ex_words: List[str]) -> int:
        """
        Makes a prediction on the given sentence
        :param ex_words: words to predict on
        :return: 0 or 1 with the label
        """
        raise Exception("Don't call me, call my subclasses")

    def predict_all(self, all_ex_words: List[List[str]]) -> List[int]:
        """
        You can leave this method with its default implementation, or you can override it to a batched version of
        prediction if you'd like. Since testing only happens once, this is less critical to optimize than training
        for the purposes of this assignment.
        :param all_ex_words: A list of all exs to do prediction on
        :return:
        """
        return [self.predict(ex_words) for ex_words in all_ex_words]


class TrivialSentimentClassifier(SentimentClassifier):
    def predict(self, ex_words: List[str]) -> int:
        """
        :param ex:
        :return: 1, always predicts positive class
        """
        return 1


class NeuralSentimentClassifier(SentimentClassifier):
    """
    Implement your NeuralSentimentClassifier here. This should wrap an instance of the network with learned weights
    along with everything needed to run it on new data (word embeddings, etc.)
    """

    def __init__(self, word_embeddings: WordEmbeddings):
        self.embedder = word_embeddings
        self.nn = DANN(word_embeddings.get_embedding_length(), 100, 20, 2)

    def predict(self, ex_words: List[str]) -> int:
        dan_input = extract_vec(ex_words, self.embedder)
        return torch.argmax(self.nn.forward(dan_input)).item()


def train_deep_averaging_network(args, train_exs: List[SentimentExample], dev_exs: List[SentimentExample], word_embeddings: WordEmbeddings) -> NeuralSentimentClassifier:
    """
    :param args: Command-line args so you can access them here
    :param train_exs: training examples
    :param dev_exs: development set, in case you wish to evaluate your model during training
    :param word_embeddings: set of loaded word embeddings
    :return: A trained NeuralSentimentClassifier model
    """

    start = time.time()
    model = NeuralSentimentClassifier(word_embeddings)
    model.nn = model.nn.to(model.nn.device)
    sentences = []
    train_ys = []
    for ex in train_exs:
        sentences.append(ex.words)
        train_ys.append(ex.label)
    train_xs = extract_array(sentences, word_embeddings)
    model.nn.train_dan_batch(train_xs, train_ys, args)
    end = time.time()

    print(f"Time taken: {end - start}")
    return model


def extract_vec(ex_words: List[str], word_embeddings: WordEmbeddings) -> np.ndarray:
    dan_input = np.zeros(word_embeddings.get_embedding_length())
    count = 0
    for word in ex_words:
        dan_input += word_embeddings.get_embedding(word)
        count += 1
    dan_input /= count
    return dan_input


def extract_array(ex_words: List[List[str]], word_embeddings: WordEmbeddings) -> np.ndarray:
    exs = np.array([], dtype=float).reshape(0,  word_embeddings.get_embedding_length())
    for words in ex_words:
        exs = np.vstack([exs, extract_vec(words, word_embeddings)])
    return exs


class DANN(nn.Module):

    def __init__(self, inp, h1, h2, out):
        super(DANN, self).__init__()
        self.device = torch.device("cpu" if torch.cuda.is_available() else "cpu")
        self.input = nn.Linear(inp, h1)
        self.a1 = nn.ReLU()
        self.h1 = nn.Linear(h1, h2)
        self.a2 = nn.ReLU()
        self.h2 = nn.Linear(h2, out)
        self.a3 = nn.LogSoftmax(dim=0)

        nn.init.xavier_uniform_(self.input.weight)
        nn.init.xavier_uniform_(self.h1.weight)
        nn.init.xavier_uniform_(self.h2.weight)

        # Initialize weights according to a formula due to Xavier Glorot.
        # Initialize with zeros instead
        # nn.init.zeros_(self.V.weight)
        # nn.init.zeros_(self.W.weight)

    def forward(self, x):
        """
        Runs the neural network on the given data and returns log probabilities of the various classes.

        :param x: a [inp]-sized tensor of input data
        :return: an [out]-sized tensor of log probabilities. (In general your network can be set up to return either log
        probabilities or a tuple of (loss, log probability) if you want to pass in y to this function as well
        """
        return self.a3(self.h2(self.a2(self.h1(self.a1(self.input(self.form_input(x)))))))

    # Batching by averaging loss across multiple examples
    def train_dan_batch(self, train_xs, train_ys, args):
        num_epochs = args.num_epochs
        initial_learning_rate = args.lr
        optimizer = optim.Adam(self.parameters(), lr=initial_learning_rate)
        num_classes = 2
        size = args.batch_size
        if size == 1:
            self.train_dan(train_xs, train_ys, args)  # More optimized for single example training
            return

        for epoch in range(0, num_epochs):
            ex_indices = [i for i in range(0, len(train_xs), size)]
            random.shuffle(ex_indices)
            total_loss = 0.0
            for idx in ex_indices:
                batch_size = min(size, len(train_xs) - idx)
                x = train_xs[idx:idx + batch_size]
                y = train_ys[idx:idx + batch_size]
                y_onehot = torch.zeros((num_classes, batch_size), device=self.device)

                y_onehot.scatter_(0, torch.from_numpy(np.reshape(np.asarray(y, dtype=np.int64), (-1, batch_size))).to(self.device), 1)
                y_onehot = torch.transpose(y_onehot, 0, 1)
                self.zero_grad()
                log_probs = self.forward(x)
                loss = torch.sum(torch.mul(torch.neg(log_probs), y_onehot))/batch_size
                total_loss += loss
                loss.backward()
                optimizer.step()
            print("Total loss on epoch %i: %f" % (epoch, total_loss))
        # Evaluate on the train set
        train_correct = 0
        for idx in range(0, len(train_xs)):
            x = train_xs[idx]
            y = train_ys[idx]
            log_probs = self.forward(x)
            prediction = torch.argmax(log_probs)
            if y == prediction:
                train_correct += 1
            #print("Example " + repr(train_xs[idx]) + "; gold = " + repr(train_ys[idx]) + "; pred = " + \
                  #repr(prediction) + " with probs " + repr(log_probs))
        print(repr(train_correct) + "/" + repr(len(train_ys)) + " correct after training")


    def train_dan(self, train_xs, train_ys, args):
        num_epochs = args.num_epochs
        initial_learning_rate = args.lr
        optimizer = optim.Adam(self.parameters(), lr=initial_learning_rate)
        num_classes = 2

        for epoch in range(0, num_epochs):
            ex_indices = [i for i in range(0, len(train_xs))]
            random.shuffle(ex_indices)
            total_loss = 0.0
            for idx in ex_indices:
                x = train_xs[idx]
                y = train_ys[idx]
                y_onehot = torch.zeros(num_classes, device=self.device)

                y_onehot.scatter_(0, torch.from_numpy(np.asarray(y, dtype=np.int64)).to(self.device), 1)
                self.zero_grad()
                log_probs = self.forward(x)
                loss = torch.neg(log_probs).dot(y_onehot)
                total_loss += loss
                loss.backward()
                optimizer.step()
            print("Total loss on epoch %i: %f" % (epoch, total_loss))
        # Evaluate on the train set
        train_correct = 0
        for idx in range(0, len(train_xs)):
            x = train_xs[idx]
            y = train_ys[idx]
            log_probs = self.forward(x)
            prediction = torch.argmax(log_probs)
            if y == prediction:
                train_correct += 1
            #print("Example " + repr(train_xs[idx]) + "; gold = " + repr(train_ys[idx]) + "; pred = " + \
                  #repr(prediction) + " with probs " + repr(log_probs))
        print(repr(train_correct) + "/" + repr(len(train_ys)) + " correct after training")

    def form_input(self, x) -> torch.Tensor:
        """
        Form the input to the neural network. In general this may be a complex function that synthesizes multiple pieces
        of data, does some computation, handles batching, etc.

        :param x: a [num_samples x inp] numpy array containing input data
        :return: a [num_samples x inp] Tensor

        """
        return torch.from_numpy(x).float().to(self.device)
