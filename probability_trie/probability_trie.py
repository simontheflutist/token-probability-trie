"""
A module that creates probability trie structures ready for JSON serialization.
"""
from collections import defaultdict
from functools import partial


def from_text(text, n):
    """Create a probability trie structure from this text."""
    frequency_trie = _make_frequency_trie(text, n)
    probability_trie = _from_frequency_trie(frequency_trie)
    return probability_trie


def _make_ngrams(text, n):
    """Return all n-grams of a specified length in a text."""
    for i in range(len(text) - n):
        yield text[i:i + n]


def _make_empty_trie(n, end_type=int):
    """Return a data structure such that after n-1 associative array
    deferences, you get a defaultdict of type end_type"""
    if n == 1:
        return defaultdict(end_type)
    if n > 1:
        return defaultdict(partial(_make_empty_trie, n - 1, end_type))
    raise ValueError


def _update_trie(trie, key, func):
    """Apply func to the value of trie at key."""
    # if we have a defaultdict of a plain type
    if len(key) == 1:
        trie[key] = func(trie[key])
        return
    if len(key) > 1:
        _update_trie(trie[key[0]], key[1:], func)
        return
    raise ValueError


def _make_frequency_trie(text, n):
    trie = _make_empty_trie(n, end_type=int)
    for n_gram in _make_ngrams(text, n):
        _update_trie(trie, n_gram, lambda i: i + 1)
    return trie


def _as_dict(trie):
    """Convert from nested defaultdict to nested dict."""
    if not isinstance(trie, defaultdict):
        return trie
    return {k: _as_dict(v) for k, v in trie.items()}


def _from_frequency_trie(frequency_trie):
    raise NotImplementedError
