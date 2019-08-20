"""
A module that creates probability trie structures ready for JSON serialization.
"""
from collections import defaultdict
from functools import partial


class FrequencyTrie(object):
    def __init__(self, depth):
        self.depth = depth
        self.trie = FrequencyTrie._new_empty_trie(self.depth)

    @staticmethod
    def _new_empty_trie(n, end_type=int):
        """Return a data structure such that after n-1 associative array
        deferences, you get a defaultdict of type end_type"""
        if n == 1:
            return defaultdict(end_type)
        if n > 1:
            return defaultdict(partial(FrequencyTrie._new_empty_trie,
                                       n - 1,
                                       end_type))
        raise ValueError

    def update(self, text):
        """Update frequencies using text."""
        for ngram in self._make_ngrams(text):
            self.update_trie(ngram)

    def _make_ngrams(self, text):
        """Return all n-grams of a specified length in a text."""
        for i in range(len(text) - self.depth):
            yield text[i:i + self.depth]

    @staticmethod
    def _update_trie(trie, key, func=lambda i: i + 1):
        """Apply func to the value of this trie at key."""
        # if we have a defaultdict of a plain type
        if len(key) == 1:
            trie[key] = func(trie[key])
            return
        if len(key) > 1:
            FrequencyTrie._update_trie(trie[key[0]], key[1:], func)
            return
        raise ValueError

    def update_trie(self, key):
        return FrequencyTrie._update_trie(self.trie, key)

    @staticmethod
    def _as_dict(trie):
        """Convert from nested defaultdict to nested dict."""
        if not isinstance(trie, defaultdict):
            return trie
        return {k: FrequencyTrie._as_dict(v) for k, v in trie.items()}

    @property
    def as_dict(self):
        return FrequencyTrie._as_dict(self.trie)

    @staticmethod
    def _compress_trie(trie):
        if not isinstance(trie, dict):
            return trie

        compressed_trie = dict()
        for key, value in trie.items():
            value = FrequencyTrie._compress_trie(value)
            if not isinstance(value, dict):
                compressed_trie[key] = value
                continue
            if len(value) == 1:
                subkey, subvalue = tuple(value.items())[0]
                compressed_trie[key + subkey] = subvalue
            else:
                compressed_trie[key] = value
        return compressed_trie

    @property
    def as_compressed_dict(self):
        return FrequencyTrie._compress_trie(self.trie)

    def merge(self, other):
        """"addAll" the counts from other."""
        if not isinstance(other, FrequencyTrie):
            raise ValueError
        for key, count in other.items:
            FrequencyTrie._update_trie(self.trie, key, lambda x: x + count)
        return

    @property
    def items(self):
        raise NotImplementedError


if __name__ == '__main__':
    import re
    with open('data/austen.txt', 'r') as f:
        t = f.read()
        t = re.sub(r"""\s+""", " ", t)
