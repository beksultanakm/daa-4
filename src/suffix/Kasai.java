package suffix;

import java.util.Arrays;

public class Kasai {

    public static long comparisons = 0;

    public static int[] buildSuffixArray(String s) {
        int n = s.length();
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) {
            idx[i] = i;
        }
        Arrays.sort(idx, (a, b) -> compareSuffixes(s, a, b));
        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = idx[i];
        }
        return sa;
    }

    private static int compareSuffixes(String s, int a, int b) {
        int n = s.length();
        while (a < n && b < n) {
            if (s.charAt(a) != s.charAt(b)) {
                return s.charAt(a) - s.charAt(b);
            }
            a++;
            b++;
        }
        return (n - a) - (n - b);
    }
    public static int[] buildLCP(String s, int[] sa) {
        comparisons = 0;
        int n = s.length();
        int[] lcp = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }

        int h = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1];
                while (i + h < n && j + h < n) {
                    comparisons++;
                    if (s.charAt(i + h) != s.charAt(j + h)) {
                        break;
                    }
                    h++;
                }
                lcp[rank[i]] = h;
                if (h > 0) {
                    h--;
                }
            } else {
                h = 0;
            }
        }
        return lcp;
    }
}