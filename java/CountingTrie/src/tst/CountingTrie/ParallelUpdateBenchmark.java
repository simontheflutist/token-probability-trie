package tst.CountingTrie;

import main.CountingTrie.CountingTrie;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ParallelUpdateBenchmark {

    @State(Scope.Thread)
    public static class ParallelUpdateBenchState {
        @Setup(Level.Trial)
        public void setUp() {
            ngrams = new ArrayList<>();

            String content = null;
            try {
                content = new String(Files.readAllBytes(Paths.get("austen.txt")));
            } catch (IOException e) {
                e.printStackTrace();
            }


            int order = 8;
            for (int i = 0; i < content.length() - order; i++) {
                ngrams.add(content.subSequence(i, i + order));
            }

        }
        List<CharSequence> ngrams;
    }

    @Benchmark
    public void parallelUpdate(ParallelUpdateBenchState pub) {
        CountingTrie ct = new CountingTrie();
        pub.ngrams.stream().parallel().forEach(ct::insertRecursive);
    }

    @Benchmark
    public void sequentialUpdate(ParallelUpdateBenchState pub) {
        CountingTrie ct = new CountingTrie();
        pub.ngrams.stream().sequential().forEach(ct::insertRecursive);
    }
}
