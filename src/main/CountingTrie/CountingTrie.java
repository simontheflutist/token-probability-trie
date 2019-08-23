package main.CountingTrie;

import org.openjdk.jmh.annotations.Benchmark;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import java.lang.Runtime;

public class CountingTrie {
    /**
     * Number of strings ending at this node.
     */
    final AtomicInteger count;

    /**
     * Strings that continue past this node.
     */
    final Map<Character, CountingTrie> children;

    public CountingTrie() {
        this.count = new AtomicInteger(0);
        this.children = //new HashMap<>();
                new ConcurrentHashMap<>(
                4,
                0.75f,
                Runtime.getRuntime().availableProcessors());
    }

    /**
     * Store CharSequence cs a postfix of this trie.
     * @param cs
     */
    public void insertRecursive(final CharSequence cs) {
        final int length = cs.length();
        if (length == 0) {
            return;
        }

        this.count.incrementAndGet();

        final Character head = cs.charAt(0);
        final CharSequence tail = cs.subSequence(1, length);

        synchronized (this.children) {
            if (!this.children.containsKey(head)) {
                this.children.put(head, new CountingTrie());
            }
        }

        final CountingTrie child = this.children.get(head);
        child.insertRecursive(tail);
    }

    /**
     * Tree representation. Tree grows horizontally to the right. Leaves display counts.
     * @return
     */
    @Override
    public String toString() {
        final StringJoiner childrenJoiner = new StringJoiner("\n");

        List<Map.Entry<Character, CountingTrie>> entries = new ArrayList<>();
        entries.addAll(this.children.entrySet());
        Collections.sort(entries, Map.Entry.comparingByKey());

        for (final Map.Entry<Character, CountingTrie> entry : entries) {
            final Character childHead = entry.getKey();
            final CountingTrie childTails = entry.getValue();
            final StringTokenizer tailTokenizer = new StringTokenizer(childTails.toString(), "\n");

            childrenJoiner.add(childHead.toString()
                + (tailTokenizer.hasMoreTokens() ? tailTokenizer.nextToken() : ""));

            while (tailTokenizer.hasMoreTokens()) {
                childrenJoiner.add("|" + tailTokenizer.nextToken());
            }
        }
        return childrenJoiner.toString();
    }
}
