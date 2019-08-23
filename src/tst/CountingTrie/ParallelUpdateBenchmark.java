package tst.CountingTrie;

import main.CountingTrie.CountingTrie;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ParallelUpdateBenchmark {

    @State(Scope.Thread)
    public static class ParallelUpdateBenchState {
        @Setup(Level.Trial)
        public void setUp() {
            NGrams = new ArrayList<>();
            sortedNGrams = new ArrayList<>();

            String content = null;
            try {
                content = new String(Files.readAllBytes(Paths.get("austen.txt")));
            } catch (IOException e) {
                e.printStackTrace();
            }


            int order = 8;
            for (int i = 0; i < content.length() - order; i++) {
                CharSequence cs = content.subSequence(i, i + order);
                NGrams.add(cs);
                sortedNGrams.add(cs);
            }

            Collections.sort(sortedNGrams, CharSequence::compare);
        }
        List<CharSequence> NGrams;
        List<CharSequence> sortedNGrams;
    }

    @State(Scope.Benchmark)
    public static class BenchResult {
        CountingTrie ct;
        @Setup(Level.Trial)
        public void setUp() {
            ct = new CountingTrie();
        }
    }

    @Benchmark @BenchmarkMode(Mode.AverageTime)
    public void parallelUpdate(ParallelUpdateBenchState pub, BenchResult br) {
        pub.NGrams.stream().parallel().forEach(br.ct::insertRecursive);
    }

    @Benchmark @BenchmarkMode(Mode.AverageTime)
    public void parallelUpdateSorted(ParallelUpdateBenchState pub, BenchResult br) {
        pub.sortedNGrams.stream().parallel().forEach(br.ct::insertRecursive);
    }

    @Benchmark @BenchmarkMode(Mode.AverageTime)
    public void sequentialUpdate(ParallelUpdateBenchState pub) {
        CountingTrie ct = new CountingTrie();
        pub.NGrams.stream().sequential().forEach(ct::insertRecursive);
    }
}
