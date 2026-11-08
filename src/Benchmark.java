import dp.EditDistance;
import dp.LCS;
import matching.KMPMatcher;
import matching.NaiveMatcher;
import matching.RabinKarpMatcher;
import suffix.Kasai;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Benchmark {

    private static final long SEED = 42;
    private static final String ALPHABET = "abcd";
    private static final int RUNS = 5;
    private static final int PATTERN_LENGTH = 10;

    private static final Random random = new Random(SEED);
    private static final List<String> csvRows = new ArrayList<>();

    private static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    private static void record(String experiment, String algorithm, int size, String testCase,
                               double avgMs, long operations, int matches) {
        System.out.printf("%-15s %-12s %8d %-8s %10.3f ms %12d ops %6d result%n",
                experiment, algorithm, size, testCase, avgMs, operations, matches);
        csvRows.add(experiment + "," + algorithm + "," + size + "," + testCase + ","
                + String.format("%.3f", avgMs) + "," + operations + "," + matches);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Seed = " + SEED + ", alphabet = " + ALPHABET + ", runs = " + RUNS + "\n");

        int[] dpSizes = {100, 500, 1000};
        for (int size : dpSizes) {
            String a = randomString(size);
            String b = randomString(size);

            LCS.length(a, b);
            long total = 0;
            int result = 0;
            for (int r = 0; r < RUNS; r++) {
                long start = System.nanoTime();
                result = LCS.length(a, b);
                total += System.nanoTime() - start;
            }
            record("DP", "LCS", size, "random", total / RUNS / 1e6, (long) size * size, result);

            EditDistance.distance(a, b);
            total = 0;
            for (int r = 0; r < RUNS; r++) {
                long start = System.nanoTime();
                result = EditDistance.distance(a, b);
                total += System.nanoTime() - start;
            }
            record("DP", "EditDistance", size, "random", total / RUNS / 1e6, (long) size * size, result);
        }

        int[] textSizes = {10000, 50000, 100000};
        for (int size : textSizes) {
            String text = randomString(size);

            String present = text.substring(size / 2, size / 2 + PATTERN_LENGTH);
            String absent = randomString(PATTERN_LENGTH);
            while (!NaiveMatcher.search(text, absent).isEmpty()) {
                absent = randomString(PATTERN_LENGTH);
            }

            String[] patterns = {present, absent};
            String[] cases = {"present", "absent"};
            for (int p = 0; p < 2; p++) {
                String pattern = patterns[p];

                NaiveMatcher.search(text, pattern);
                long total = 0;
                List<Integer> res = null;
                for (int r = 0; r < RUNS; r++) {
                    long start = System.nanoTime();
                    res = NaiveMatcher.search(text, pattern);
                    total += System.nanoTime() - start;
                }
                record("Matching", "Naive", size, cases[p], total / RUNS / 1e6,
                        NaiveMatcher.comparisons, res.size());

                KMPMatcher.search(text, pattern);
                total = 0;
                for (int r = 0; r < RUNS; r++) {
                    long start = System.nanoTime();
                    res = KMPMatcher.search(text, pattern);
                    total += System.nanoTime() - start;
                }
                record("Matching", "KMP", size, cases[p], total / RUNS / 1e6,
                        KMPMatcher.comparisons, res.size());

                RabinKarpMatcher.search(text, pattern);
                total = 0;
                for (int r = 0; r < RUNS; r++) {
                    long start = System.nanoTime();
                    res = RabinKarpMatcher.search(text, pattern);
                    total += System.nanoTime() - start;
                }
                record("Matching", "RabinKarp", size, cases[p], total / RUNS / 1e6,
                        RabinKarpMatcher.comparisons, res.size());
            }
        }

        int[] kasaiSizes = {1000, 5000, 10000};
        for (int size : kasaiSizes) {
            String s = randomString(size);
            int[] sa = Kasai.buildSuffixArray(s);

            Kasai.buildLCP(s, sa);
            long total = 0;
            for (int r = 0; r < RUNS; r++) {
                long start = System.nanoTime();
                Kasai.buildLCP(s, sa);
                total += System.nanoTime() - start;
            }
            record("Kasai", "Kasai", size, "random", total / RUNS / 1e6, Kasai.comparisons, 0);
        }


        try (FileWriter writer = new FileWriter("results/results.csv")) {
            writer.write("experiment,algorithm,size,case,avg_time_ms,operations,result\n");
            for (String row : csvRows) {
                writer.write(row + "\n");
            }
        }
        System.out.println("\nResults saved to results/results.csv");
    }
}