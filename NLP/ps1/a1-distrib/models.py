# models.py

from sentiment_data import *
from utils import *
import numpy as np
import matplotlib.pyplot as plt
import random

from collections import Counter

class FeatureExtractor(object):
    """
    Feature extraction base type. Takes a sentence and returns an indexed list of features.
    """
    def get_indexer(self):
        raise Exception("Don't call me, call my subclasses")

    def extract_features(self, sentence: List[str], add_to_indexer: bool=False) -> Counter:
        """
        Extract features from a sentence represented as a list of words. Includes a flag add_to_indexer to
        :param sentence: words in the example to featurize
        :param add_to_indexer: True if we should grow the dimensionality of the featurizer if new features are encountered.
        At test time, any unseen features should be discarded, but at train time, we probably want to keep growing it.
        :return: A feature vector. We suggest using a Counter[int], which can encode a sparse feature vector (only
        a few indices have nonzero value) in essentially the same way as a map. However, you can use whatever data
        structure you prefer, since this does not interact with the framework code.
        """
        raise Exception("Don't call me, call my subclasses")


class UnigramFeatureExtractor(FeatureExtractor):
    """
    Extracts unigram bag-of-words features from a sentence. It's up to you to decide how you want to handle counts
    and any additional preprocessing you want to do.
    """

    indexer = None

    def __init__(self, indexer: Indexer):
        self.indexer = indexer

    def get_indexer(self):
        return self.indexer

    def extract_features(self, sentence: List[str], add_to_indexer: bool = False) -> Counter:
        counter = Counter()
        for word in sentence:
            word = word.lower()
            if add_to_indexer:
                index = self.indexer.add_and_get_index(word)
            else:
                index = self.indexer.index_of(word)
                if index == -1:
                    continue
            if index not in counter:
                counter[index] = 1
            # else:
                # counter[index] = min(1 + counter[index], 20)
        return counter


class BigramFeatureExtractor(FeatureExtractor):
    """
    Bigram feature extractor analogous to the unigram one.
    """
    indexer = None

    def __init__(self, indexer: Indexer):
        self.indexer = indexer

    def get_indexer(self):
        return self.indexer

    def extract_features(self, sentence: List[str], add_to_indexer: bool = False) -> Counter:
        counter = Counter()
        new_sentence = []
        for i in range(len(sentence) - 1):
            new_sentence.append(sentence[i] + '|' + sentence[i + 1])
        for word in new_sentence:
            word = word.lower()
            if add_to_indexer:
                index = self.indexer.add_and_get_index(word)
            else:
                index = self.indexer.index_of(word)
                if index == -1:
                    continue
            if index not in counter:
                counter[index] = 1
            # else:
                # counter[index] = min(1 + counter[index], 20)
        return counter


class BetterFeatureExtractor(FeatureExtractor):
    """
    Better feature extractor...try whatever you can think of!
    """
    indexer = None

    def __init__(self, indexer: Indexer):
        self.indexer = indexer

    def get_indexer(self):
        return self.indexer

    def extract_features(self, sentence: List[str], add_to_indexer: bool = False) -> Counter:
        positive = True
        counter = Counter()
        for word in sentence:
            word = word.lower()
            if word in dead_words:
                continue
            if has_punc_start_end(word):
                positive = True
            if not positive:
                word += "_NOT"
            if is_negation(word):
                positive = False
            word = remove_punc(word)

            if add_to_indexer:
                index = self.indexer.add_and_get_index(word)
            else:
                index = self.indexer.index_of(word)
                if index == -1:
                    continue
            if index not in counter:
                counter[index] = 1
            # else:
            # counter[index] = min(1 + counter[index], 20)

        return counter


class SentimentClassifier(object):
    """
    Sentiment classifier base type
    """
    def predict(self, sentence: List[str]) -> int:
        """
        :param sentence: words (List[str]) in the sentence to classify
        :return: Either 0 for negative class or 1 for positive class
        """
        raise Exception("Don't call me, call my subclasses")


class TrivialSentimentClassifier(SentimentClassifier):
    """
    Sentiment classifier that always predicts the positive class.
    """
    def predict(self, sentence: List[str]) -> int:
        return 1


class PerceptronClassifier(SentimentClassifier):
    """
    Implement this class -- you should at least have init() and implement the predict method from the SentimentClassifier
    superclass. Hint: you'll probably need this class to wrap both the weight vector and featurizer -- feel free to
    modify the constructor to pass these in.
    """
    weight = None
    featurizer = None
    bias = 0

    epochs = 30
    learning_rate_base = 0.005
    step = 1

    def __init__(self, featurizer: FeatureExtractor, size):
        self.featurizer = featurizer
        self.weight = np.zeros(size)

    def evaluate(self, counter: Counter):
        return product(counter, self.weight, self.bias)

    def predict(self, sentence: List[str]) -> int:
        return 0 if self.evaluate(self.featurizer.extract_features(sentence)) < 0 else 1

    def update_hyperparameters(self, epochs=30, learning_rate_base=0.4, step=1):
        self.epochs = epochs
        self.learning_rate_base = learning_rate_base
        self.step = step


class LogisticRegressionClassifier(SentimentClassifier):
    """
    Implement this class -- you should at least have init() and implement the predict method from the SentimentClassifier
    superclass. Hint: you'll probably need this class to wrap both the weight vector and featurizer -- feel free to
    modify the constructor to pass these in.
    """

    weight = None
    featurizer = None
    bias = 0

    epochs = 30
    learning_rate_base = 0.03
    step = 1

    def __init__(self, featurizer: FeatureExtractor, size):
        self.featurizer = featurizer
        self.weight = np.zeros(size)

    def prob_counter(self, counter: Counter):
        return sigmoid(product(counter, self.weight, self.bias))

    def prob_sentence(self, sentence: List[str]):
        counter = self.featurizer.extract_features(sentence)
        return self.prob_counter(counter)

    def predict(self, sentence: List[str]) -> int:
        return 0 if self.prob_sentence(sentence) < 0.5 else 1

    def accuracy(self, train_exs: List[SentimentExample]):
        counter = 0
        for ex in train_exs:
            counter += self.predict(ex.words) == ex.label
        return counter/len(train_exs)


    def update_hyperparameters(self, epochs=30, learning_rate_base=0.4, step=1):
        self.epochs = epochs
        self.learning_rate_base = learning_rate_base
        self.step = step



def train_perceptron(train_exs: List[SentimentExample], feat_extractor: FeatureExtractor) -> PerceptronClassifier:
    """
    Train a classifier with the perceptron.
    :param train_exs: training set, List of SentimentExample objects
    :param feat_extractor: feature extractor to use
    :return: trained PerceptronClassifier model
    """

    random.shuffle(train_exs)
    counters = [feat_extractor.extract_features(example.words, add_to_indexer=True) for example in train_exs]

    model = PerceptronClassifier(feat_extractor, len(feat_extractor.get_indexer().ints_to_objs))
    model.update_hyperparameters(epochs=20, learning_rate_base=0.01)

    costs = perceptron_sgd(train_exs, counters, model, verbose=True)

    fig, ax = plt.subplots()
    ax.plot(range(model.epochs + 1), costs)

    ax.set(xlabel='epoch', ylabel='cost',
           title='Objective')
    ax.grid()
    plt.show()
    temp_weights = np.copy(model.weight)
    ranked = np.argsort(temp_weights)

    print("Bottom Words")
    for j in range(10):
        print(f"{j + 1}: {model.featurizer.get_indexer().get_object(ranked[j])} has weight {model.weight[ranked[j]]}")

    print("Top Words")
    for j in range(1, 11):
        print(f"{j}: {model.featurizer.get_indexer().get_object(ranked[-j])} has weight {model.weight[ranked[-j]]}")

    return model


def train_logistic_regression(train_exs: List[SentimentExample], feat_extractor: FeatureExtractor) -> LogisticRegressionClassifier:
    """
    Train a logistic regression model.
    :param train_exs: training set, List of SentimentExample objects
    :param feat_extractor: feature extractor to use
    :return: trained LogisticRegressionClassifier model
    """
    random.shuffle(train_exs)
    counters = [feat_extractor.extract_features(example.words, add_to_indexer=True) for example in train_exs]

    model = LogisticRegressionClassifier(feat_extractor, len(feat_extractor.get_indexer().ints_to_objs))
    model.update_hyperparameters(epochs=25, learning_rate_base=0.003)

    costs, accuracy = lr_sgd(train_exs, counters, model, verbose=True)

    fig, ax = plt.subplots()
    ax.plot(range(model.epochs + 1), accuracy)
    plt.ylim([0, 1])
    ax.set(xlabel='epoch', ylabel='Accuracy',
           title=f'Constant Learning Schedule: \u03B1 = {model.learning_rate_base}')
    ax.grid()
    plt.savefig(f'ConstantLearningScheduleA{model.learning_rate_base}.png', format='png')
    plt.show()

    return model


def train_model(args, train_exs: List[SentimentExample], dev_exs: List[SentimentExample]) -> SentimentClassifier:
    """
    Main entry point for your modifications. Trains and returns one of several models depending on the args
    passed in from the main method. You may modify this function, but probably will not need to.
    :param args: args bundle from sentiment_classifier.py
    :param train_exs: training set, List of SentimentExample objects
    :param dev_exs: dev set, List of SentimentExample objects. You can use this for validation throughout the training
    process, but you should *not* directly train on this data.
    :return: trained SentimentClassifier model, of whichever type is specified
    """
    # Initialize feature extractor
    if args.model == "TRIVIAL":
        feat_extractor = None
    elif args.feats == "UNIGRAM":
        # Add additional preprocessing code here
        feat_extractor = UnigramFeatureExtractor(Indexer())
    elif args.feats == "BIGRAM":
        # Add additional preprocessing code here
        feat_extractor = BigramFeatureExtractor(Indexer())
    elif args.feats == "BETTER":
        # Add additional preprocessing code here
        feat_extractor = BetterFeatureExtractor(Indexer())
    else:
        raise Exception("Pass in UNIGRAM, BIGRAM, or BETTER to run the appropriate system")

    # Train the model
    if args.model == "TRIVIAL":
        model = TrivialSentimentClassifier()
    elif args.model == "PERCEPTRON":
        model = train_perceptron(train_exs, feat_extractor)
    elif args.model == "LR":
        model = train_logistic_regression(train_exs, feat_extractor)
    else:
        raise Exception("Pass in TRIVIAL, PERCEPTRON, or LR to run the appropriate system")
    return model


def product(counter, weight, bias):
    value = bias
    for index in counter:
        value += counter[index] * weight[index]
    return value


def sigmoid(value):
    return 1 / (1 + np.exp(-1 * value))


def perceptron_loss(train_exs: List[SentimentExample], train_counters: List[Counter], model: PerceptronClassifier):
    loss = 0
    for example, counter in zip(train_exs, train_counters):
        p = model.evaluate(counter)
        y = -1 if example.label == 0 else 1
        loss += max(0, -y * p)
    return loss / len(train_exs)


def cross_entropy_loss(train_exs: List[SentimentExample], train_counters: List[Counter], model: LogisticRegressionClassifier):
    loss = 0
    for example, counter in zip(train_exs, train_counters):
        p = model.prob_counter(counter)
        y = example.label
        loss += y * np.log(p) + (1-y) * np.log(1-p)
    return -loss / len(train_exs)


def perceptron_sgd(train_exs: List[SentimentExample], train_counters: List[Counter], model: PerceptronClassifier, verbose=False):
    costs = [perceptron_loss(train_exs, train_counters, model)]
    if verbose:
        print(f"Epoch {0}, Cost: {costs[0]}")
    for i in range(model.epochs):
        for j in range(len(train_exs)):
            compute_perceptron_gradients(train_exs, train_counters, model, j, 1, i)
        cost = perceptron_loss(train_exs, train_counters, model)
        if verbose:
            print(f"Epoch {i + 1}, Cost: {cost}")
        costs.append(cost)
    return costs


def perceptron_bgd(train_exs: List[SentimentExample], train_counters: List[Counter], model: PerceptronClassifier, verbose=False):
    costs = [perceptron_loss(train_exs, train_counters, model)]
    if verbose:
        print(f"Epoch {0}, Cost: {costs[0]}")
    for i in range(model.epochs):
        compute_perceptron_gradients(train_exs, train_counters, model, 0, len(train_exs), i)
        cost = perceptron_loss(train_exs, train_counters, model)
        if verbose:
            print(f"Epoch {i + 1}, Cost: {cost}")
        costs.append(cost)
    return costs


def lr_sgd(train_exs: List[SentimentExample], train_counters: List[Counter], model: LogisticRegressionClassifier, verbose=False):
    costs = [cross_entropy_loss(train_exs, train_counters, model)]
    accuracy = [model.accuracy(train_exs)]
    if verbose:
        print(f"Epoch {0}, Cost: {costs[0]}")
    for i in range(model.epochs):
        for j in range(len(train_exs)):
            compute_lr_gradients(train_exs, train_counters, model, j, 1, i)
        cost = cross_entropy_loss(train_exs, train_counters, model)
        accuracy.append(model.accuracy(train_exs))
        if verbose:
            print(f"Epoch {i + 1}, Cost: {cost}")
        costs.append(cost)
    return costs, accuracy


def lr_bgd(train_exs: List[SentimentExample], train_counters: List[Counter], model: LogisticRegressionClassifier, verbose=False):
    costs = [cross_entropy_loss(train_exs, train_counters, model)]
    if verbose:
        print(f"Epoch {0}, Cost: {costs[0]}")
    for i in range(model.epochs):
        compute_lr_gradients(train_exs, train_counters, model, 0, len(train_exs), i)
        cost = cross_entropy_loss(train_exs, train_counters, model)
        if verbose:
            print(f"Epoch {i + 1}, Cost: {cost}")
        costs.append(cost)
    return costs


def lr_mbgd(train_exs: List[SentimentExample], train_counters: List[Counter], model: LogisticRegressionClassifier, verbose=False):
    costs = [cross_entropy_loss(train_exs, train_counters, model)]
    if verbose:
        print(f"Epoch {0}, Cost: {costs[0]}")
    for i in range(model.epochs):
        for j in range(len(train_exs)):
            compute_lr_gradients(train_exs, train_counters, model, j, min(model.step, len(train_exs) - j), i)
        cost = cross_entropy_loss(train_exs, train_counters, model)
        if verbose:
            print(f"Epoch {i + 1}, Cost: {cost}")
        costs.append(cost)
    return costs


def compute_perceptron_gradients(train_exs: List[SentimentExample], train_counters: List[Counter], model: PerceptronClassifier, start, step, epoch):
    bias_gradient = 0
    gradients = np.zeros((np.shape(model.weight)))
    for k in range(start, start + step):
        example = train_exs[k]
        counter = train_counters[k]
        p = model.evaluate(counter)
        q = -1 if p < 0 else 1
        y = -1 if example.label == 0 else 1
        for index in counter:
            if q != y:
                gradients[index] += q * counter[index]
        bias_gradient += q-y

    learning_rate = compute_learning_rate(model.learning_rate_base, epoch)
    model.weight -= learning_rate * gradients / step
    model.bias -= learning_rate * bias_gradient / step


def compute_lr_gradients(train_exs: List[SentimentExample], train_counters: List[Counter], model: LogisticRegressionClassifier, start, step, epoch):
    bias_gradient = 0
    gradients = np.zeros((np.shape(model.weight)))
    for k in range(start, start + step):
        example = train_exs[k]
        counter = train_counters[k]
        p = model.prob_counter(counter)
        y = example.label
        for index in counter:
            gradients[index] += (p - y) * counter[index]
        bias_gradient += p - y

    learning_rate = compute_learning_rate(model.learning_rate_base, epoch)
    model.weight -= learning_rate * gradients / step
    model.bias -= learning_rate * bias_gradient / step


def compute_learning_rate(learning_rate_base, epoch):
    return learning_rate_base / (1 + (epoch // 5))


punc_set = {'.': None,
               '?': None,
               '!': None,
               ',': None,
               '(': None,
               ')': None,
               '[': None,
               ']': None,
               '{': None,
               '}': None,
               '\"': None,
            }


dead_words = {
    'the': None,
    'of': None,
    'to': None,
    'a': None,
    'and': None,
    'in': None,
    'that': None,
    'for': None,
    'is': None,
    'on': None,
    'was': None,
    'with': None,
    'said': None,
    'as': None,
    'by': None,
    'he': None,
    'have': None,
    'at': None,
    'from': None,
    'are': None,
    'not': None,
    'has': None,
    'it': None,
    'be': None,
    'an': None,
    'who': None,
    'his': None,
    'had': None,
    'they': None,
    'their': None,
    'would': None,
    'which': None,
    'were': None,
    'been': None,
    'or': None,
    'more': None,
    'about': None,
    'this': None,
    'will': None,
    'its': None,
}


def is_negation(word):
    if len(word) < 3:
        return False
    if word == 'not' or word[-3:] == 'not' or word[-3:] == 'n\'t':
        return True
    return False


def has_punc_start_end(word):
    return word[0] in punc_set or word[-1] in punc_set


def remove_punc(word):
    for p in punc_set:
        word = word.replace(p, '')
    return word
