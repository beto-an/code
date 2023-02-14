# inference.py
import sys

from models import *
from treedata import *
from utils import *
from collections import Counter
from typing import List

import numpy as np
np.set_printoptions(threshold=sys.maxsize)

def decode_bad_tagging_model(model: BadTaggingModel, sentence: List[str]) -> List[str]:
    """
    :param sentence: the sequence of words to tag
    :return: the list of tags, which must match the length of the sentence
    """
    pred_tags = []
    for word in sentence:
        if word in model.words_to_tag_counters:
            pred_tags.append(model.words_to_tag_counters[word].most_common(1)[0][0])
        else:
            pred_tags.append("NN") # unks are often NN
    return labeled_sent_from_words_tags(sentence, pred_tags)


def  viterbi_decode(model: HmmTaggingModel, sentence: List[str]) -> LabeledSentence:
    """
    :param model: the HmmTaggingModel to use (wraps initial, emission, and transition scores)
    :param sentence: the words to tag
    :return: a LabeledSentence containing the model's predictions. See BadTaggingModel for an example.
    """
    num_tags = len(model.tag_indexer.ints_to_objs) - 1
    scores = np.zeros((num_tags, len(sentence)), dtype=float)
    back_pointers = np.zeros((num_tags, len(sentence)), dtype=int)
    for i in range(num_tags):
        scores[i, 0] = model.score_init(i) + model.score_emission(sentence, i, 0)

    for j in range(1, len(sentence)):
        for curr in range(num_tags):
            log_probs = [model.score_transition(prev, curr) + model.score_emission(sentence, curr, j) + scores[prev, j - 1] for prev in range(num_tags)]
            back_pointers[curr, j] = np.argmax(log_probs).item()
            scores[curr, j] = log_probs[back_pointers[curr, j]]


    labels = np.zeros(len(sentence), dtype=int)
    labels[-1] = np.argmax(scores[:, -1]).item()
    for j in reversed(range(len(sentence) - 1)):
        labels[j] = (back_pointers[labels[j + 1], j + 1])
    return LabeledSentence([TaggedToken(sentence[i], model.tag_indexer.get_object(labels[i])) for i in range(len(sentence))])


def beam_decode(model: HmmTaggingModel, sentence: List[str], beam_size: int) -> LabeledSentence:
    """
    :param model: the HmmTaggingModel to use (wraps initial, emission, and transition scores)
    :param sentence: the words to tag
    :param beam_size: the beam size to use
    :return: a LabeledSentence containing the model's predictions. See BadTaggingModel for an example.
    """
    num_tags = len(model.tag_indexer.ints_to_objs) - 1
    layers = [Beam(beam_size)]
    back_pointers = np.zeros((num_tags, len(sentence)), dtype=int)
    for i in range(num_tags):
        layers[0].add(Trace(i, None), model.score_init(i) + model.score_emission(sentence, i, 0))

    for j in range(1, len(sentence)):
        layers.append(Beam(beam_size))
        for curr in range(num_tags):
            for prev, score in layers[-2].get_elts_and_scores():
                layers[-1].add(Trace(curr, prev), model.score_transition(prev.elt, curr) + model.score_emission(sentence, curr, j) + score)

    labels = layers[-1].head().flatten()
    return LabeledSentence(
        [TaggedToken(sentence[i], model.tag_indexer.get_object(labels[i])) for i in range(len(sentence))])


class Trace:
    def __init__(self, elt, prev=None):
        self.elt = elt
        self.prev = prev

    def flatten(self):
        llist = []
        current = self
        while current is not None:
            llist.append(current.elt)
            current = current.prev
        llist.reverse()
        return llist
