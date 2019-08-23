package main.CountingTrie;

import org.w3c.dom.Text;

import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TextProcessor {
    final String filename;
    final boolean clobberSpaces;

    public TextProcessor(final String filename, final boolean clobberSpaces) {
        this.filename = filename;
        this.clobberSpaces = clobberSpaces;
    }

    /**
     * Lazily read n-grams from the file.
     *
     * @param length
     * @return a stream of n-grams
     */
    public List<? extends CharSequence> nGrams(final int length){
        String content = null;
        try {
            content = new String(Files.readAllBytes(Path.of(this.filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(content.substring(0, 1000));
        return null;
    }
//    public Stream<? extends CharSequence> nGrams(final int length) {
//        // Define iterator that uses a buffer in its closure.
//        final List<CharSequence> buf = new ArrayList<>(length + 1);
//        final Iterator<CharSequence> iterator = new Iterator<>() {
//            @Override
//            public boolean hasNext() {
//                return buf.size() >= length;
//            }
//
//            @Override
//            public CharSequence next() {
//                final CharSequence cs = String.join("", buf.subList(0, length));
//                buf.remove(0);
//                return cs;
//            }
//        };
//
//        try (final Reader fileReader = new BufferedReader(new FileReader(this.file))) {
//            boolean wasSpace = false;
//            // Read a character.
//            for (; ; ) {
//                int r = fileReader.read();
//                if (r == -1) {
//                    break;
//                }
//
//                char c = (char) r;
//                String s = String.valueOf(c);
//
//                if (!TextProcessor.this.clobberSpaces) {
//                    buf.add(s);
//                    continue;
//                }
//
//                boolean isSpace = s.isBlank();
//                if (!isSpace) {
//                    wasSpace = false;
//                    buf.add(s);
//                    continue;
//                }
//
//                if (wasSpace) {
//                    continue;
//                }
//
//                s = " ";
//                buf.add(s);
//                wasSpace = true;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        final Iterable<CharSequence> iterable = () -> iterator;
//        return StreamSupport.stream(iterable.spliterator(), false);
//    }

    public static void main(String[] args) {
        TextProcessor tp = new TextProcessor("austen.txt", true);
        CountingTrie ct = new CountingTrie();
        tp.nGrams(3)
//                .parallel()
                .forEach(i->{});
        System.out.println(ct.toString());
    }
}
