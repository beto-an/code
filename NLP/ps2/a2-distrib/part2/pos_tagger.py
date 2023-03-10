# pos_tagger.py

import argparse
import sys
import time
from treedata import *
from models import *
from inference import *
import matplotlib.pyplot as plt


def _parse_args():
    """
    Command-line arguments to the system. --model switches between the main modes you'll need to use. The other arguments
    are provided for convenience.
    :return: the parsed args bundle
    """
    parser = argparse.ArgumentParser(description='pos_tagger.py')
    parser.add_argument('--model', type=str, default='BAD', help='model to run (BAD or HMM)')
    parser.add_argument('--use_beam', dest='use_beam', default=True, action='store_true', help='use beam search instead of Viterbi')
    parser.add_argument('--beam_size', type=int, default=1, help='beam size')
    parser.add_argument('--train_path', type=str, default='data/train_sents.conll', help='path to train set (you should not need to modify)')
    parser.add_argument('--dev_path', type=str, default='data/dev_sents.conll', help='path to dev set (you should not need to modify)')
    args = parser.parse_args()
    return args


def print_evaluation(gold, pred):
    """
    Prints accuracy comparing gold tagged sentences and pred tagged sentences
    :param gold:
    :param pred:
    :return:
    """
    num_correct = 0
    num_total = 0
    for (gold_sent, pred_sent) in zip(gold, pred):
        for (gold_tag, pred_tag) in zip(gold_sent.get_tags(), pred_sent.get_tags()):
            num_total += 1
            if gold_tag == pred_tag:
                num_correct += 1
    accuracy = float(num_correct) / num_total
    output = "Accuracy: %i / %i = %f" % (num_correct, num_total, accuracy)
    return accuracy, output


if __name__ == '__main__':
    start_time = time.time()
    args = _parse_args()
    print(args)

    train_sents = read_labeled_sents(args.train_path)
    dev_sents = read_labeled_sents(args.dev_path)

    # Here's a few sentences...
    print("Examples of sentences:")
    print(str(dev_sents[1]))
    print(str(dev_sents[3]))
    print(str(dev_sents[5]))
    system_to_run = args.model
    # Train our model
    if system_to_run == "BAD":
        bad_model = train_bad_tagging_model(train_sents)
        dev_decoded = [decode_bad_tagging_model(bad_model, dev_ex.get_words()) for dev_ex in dev_sents]
    elif system_to_run == "HMM":
        hmm_model = train_hmm_model(train_sents)
        x = [i for i in range(1, 50)]
        accuracies = []
        times = []
        for bs in x:
            dev_decoded = []
            decode_start = time.time()
            for dev_ex in dev_sents:
                if args.use_beam:
                    dev_decoded.append(beam_decode(hmm_model, dev_ex.get_words(), bs))
                else:
                    dev_decoded.append(viterbi_decode(hmm_model, dev_ex.get_words()))
                if len(dev_decoded) % 100 == 0:
                    print("Decoded %i" % (len(dev_decoded)))
            time_elapsed = time.time() - decode_start
            print("Total time to tag the development set: %i seconds" % time_elapsed)
            acc, out = print_evaluation(dev_sents, dev_decoded)
            print(out)
            times.append(time_elapsed)
            accuracies.append(acc)

        fig, ax = plt.subplots()
        ax.plot(x, accuracies)
        plt.ylim([0.93, 0.95])
        ax.set(xlabel='Beam Size', ylabel='Accuracy',
               title="Beam Size vs. Accuracy Tradeoff")
        plt.locator_params(axis="x", integer=True, tight=True)
        ax.grid()
        plt.savefig(f'BeamSizeAccuracy.png', format='png')
        plt.show()
        plt.clf()

        fig, ax = plt.subplots()
        ax.plot(x, times)
        plt.ylim([0, 200])
        ax.set(xlabel='Beam Size', ylabel='Time Elapsed',
               title="Beam Size vs. Time Tradeoff")
        plt.locator_params(axis="x", integer=True, tight=True)
        ax.grid()
        plt.savefig(f'BeamSizeTime.png', format='png')
        plt.show()
    else:
        raise Exception("Pass in either BAD or HMM to run the appropriate system")
    # Print the evaluation statistics

