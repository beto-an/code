import torch
import torch.nn as nn
from torch import optim
import numpy as np
import random


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

    def train_dan_batch(self, train_xs, train_ys, batch_size):
        num_epochs = 40
        initial_learning_rate = 0.0003
        optimizer = optim.Adam(self.parameters(), lr=initial_learning_rate)
        num_classes = 2

        for epoch in range(0, num_epochs):
            ex_indices = [i for i in range(0, len(train_xs), batch_size)]
            random.shuffle(ex_indices)
            total_loss = 0.0
            for idx in ex_indices:
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


    def train_dan(self, train_xs, train_ys):
        num_epochs = 10
        initial_learning_rate = 0.001
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
