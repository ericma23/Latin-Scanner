public class DactylicHex {
    public static final boolean[][] fifteenVowels = {{true, false, false, true, false, false, true, false, false, true,
            true, true, true, true, true}, {true, false, false, true, false, false, true, true, true, false, false, true,
            true, true, true}, {true, false, false, true, false, false, true, true, true, true, true, false, false,
            true, true}, {true, false, false, true, true, true, true, true, false, false, true, false, false, true,
            true}, {true, false, false, true, true, true, false, false, true, true, true, false, false, true,
            true}, {true, false, false, true, true, true, false, false, true, false, false, true, true, true, true}};
    public static final boolean[][] thirteenVowels = {{true, false, false, true, true, true, true, true, true, true, true, true, true}};
    public static final boolean[][]sixteenVowels = {{true, false, false, true, false, false, true, false, false, true,
            false, false, true, true, true, true}, {true, false, false, true, false, false, true, false, false, true, true,
            true, false, false, true, true}, {true, false, false, true, false, false, true, true, true, false, false,
            true, false, false, true, true}, {true, false, false, true, true, true, false, false, true, false, false,
            true, false, false, true, true}};
    public static final boolean[][] fourteenVowels = {{true, false, false, true, true, true, true, true, true, true,
            false, false, true, true}, {true, false, false, true, true, true, true, true, false, false, true, true, true, true},
            {true, false, false, true, true, true, false, false, true, true, true, true, true, true}, {true, false,
            false, true, false, false, true, true, true, true, true, true, true, true}};
    public static final boolean[][] seventeenVowels = {{true, false, false, true, false, false, true, false, false,
            true, false, false, true, false, false, true, true}};

    public static boolean[][] getScansOf(int numVowels) {
        if(numVowels == 13) return thirteenVowels;
        else if(numVowels == 14) return fourteenVowels;
        else if(numVowels == 15) return fifteenVowels;
        else if(numVowels == 16) return sixteenVowels;
        else if(numVowels == 17) return seventeenVowels;

        return null;
    }
}
