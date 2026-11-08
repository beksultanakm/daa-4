package matching;

import java.util.ArrayList;
import java.util.List;

public class RabinKarpMatcher {

    private static final long BASE = 256;
    private static final long MOD = 1_000_000_007L;

    public static long comparisons = 0;
    public static long collisions = 0;

    public static List<Integer> search(String text, String pattern) {
        comparisons = 0;
        collisions = 0;
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m == 0 || m > n) {
            return result;
        }

        long high = 1;
        for (int i = 0; i < m - 1; i++) {
            high = (high * BASE) % MOD;
        }

        long patternHash = 0;
        long windowHash = 0;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
            windowHash = (windowHash * BASE + text.charAt(i)) % MOD;
        }

        for (int i = 0; i <= n - m; i++) {
            if (patternHash == windowHash) {
                int j = 0;
                while (j < m) {
                    comparisons++;
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        break;
                    }
                    j++;
                }
                if (j == m) {
                    result.add(i);
                } else {
                    collisions++;
                }
            }

            if (i < n - m) {
                windowHash = (windowHash - text.charAt(i) * high % MOD + MOD) % MOD;
                windowHash = (windowHash * BASE + text.charAt(i + m)) % MOD;
            }
        }
        return result;
    }
}