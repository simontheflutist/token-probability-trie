# token-probability-trie

## basic idea
     import probability_trie
     ptrie = probability_trie.fromText(sometext, depth=5)
     # ptrie is a JSON-able data structure of maps
     assert(ptrie['a']['b']['c']['d']['e'] == 0.2)
