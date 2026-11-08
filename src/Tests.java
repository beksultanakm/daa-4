import dp.EditDistance;
import dp.LCS;
import matching.KMPMatcher;
import matching.NaiveMatcher;
import matching.RabinKarpMatcher;
import suffix.Kasai;

import java.util.Arrays;
import java.util.List;

public class Tests {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    private static void checkMatching(String name, String text, String pattern, List<Integer> expected) {
        List<Integer> naive = NaiveMatcher.search(text, pattern);
        List<Integer> kmp = KMPMatcher.search(text, pattern);
        List<Integer> rk = RabinKarpMatcher.search(text, pattern);
        boolean ok = naive.equals(expected) && kmp.equals(expected) && rk.equals(expected);
        check(name + " -> naive=" + naive + " kmp=" + kmp + " rk=" + rk, ok);
    }

    public static void main(String[] args) {
        System.out.println("=== LCS ===");
        check("identical strings", LCS.length("ABCDEF", "ABCDEF") == 6);
        check("completely different strings", LCS.length("ABC", "XYZ") == 0);
        check("empty string", LCS.length("", "ABC") == 0);
        check("both empty", LCS.length("", "") == 0);
        check("classic example", LCS.length("ABCBDAB", "BDCABA") == 4);

        System.out.println("\n=== Edit Distance ===");
        check("identical strings", EditDistance.distance("kitten", "kitten") == 0);
        check("one empty string", EditDistance.distance("", "abc") == 3);
        check("substitution", EditDistance.distance("cat", "cut") == 1);
        check("insertion", EditDistance.distance("cat", "cats") == 1);
        check("deletion", EditDistance.distance("cats", "cat") == 1);
        check("kitten -> sitting", EditDistance.distance("kitten", "sitting") == 3);

        System.out.println("\n=== String Matching ===");
        checkMatching("pattern at beginning", "abcdef", "abc", List.of(0));
        checkMatching("pattern in middle", "abcdef", "cd", List.of(2));
        checkMatching("pattern at end", "abcdef", "def", List.of(3));
        checkMatching("multiple occurrences", "abababab", "aba", List.of(0, 2, 4));
        checkMatching("pattern absent", "abcdef", "xyz", List.of());
        checkMatching("pattern longer than text", "abc", "abcd", List.of());
        checkMatching("empty pattern", "abc", "", List.of());

        System.out.println("\n=== Kasai (banana) ===");
        String s = "banana";
        int[] expectedSA = {5, 3, 1, 0, 4, 2};
        int[] expectedLCP = {0, 1, 3, 0, 0, 2};
        int[] sa = Kasai.buildSuffixArray(s);
        check("suffix array " + Arrays.toString(sa), Arrays.equals(sa, expectedSA));
        int[] lcp = Kasai.buildLCP(s, expectedSA);
        check("LCP array " + Arrays.toString(lcp), Arrays.equals(lcp, expectedLCP));

        System.out.println("\nPassed: " + passed + ", Failed: " + failed);
    }
}