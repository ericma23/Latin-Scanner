public class Vowel {
    private String letter;
    private boolean isLong;
    private boolean isLongByRule;
    private boolean isShortByRule;
    private boolean isDiphthong;
    private int indexInWord;



    public int getIndexInWord() {
        return indexInWord;
    }

    public void setIndexInWord(int indexInWord) {
        this.indexInWord = indexInWord;
    }

    public Vowel(String letter, boolean isLong, boolean isDiphthong, int indexInWord) {
        this.letter = letter;
        this.isLong = isLong;
        this.isDiphthong = isDiphthong;
        this.indexInWord = indexInWord;
        this.isLongByRule = false;
        this.isShortByRule = false;
    }

    public Vowel(String letter, boolean isLong, boolean byRule) {
        this.letter = letter;
        if (byRule) {
            this.isLong = false;
            this.isLongByRule = isLong;
        }
        else {
            this.isLong = isLong;
        }
        this.isShortByRule = false;
    }



    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public boolean isLong() {
        return isLong;
    }

    public void setLong(boolean aLong) {
        isLong = aLong;
    }

    public boolean isDiphthong() {
        return isDiphthong;
    }

    public void setDiphthong(boolean diphthong) {
        isDiphthong = diphthong;
    }

    public String toString() {

        if(isShortByRule) return letter + ": short by rule";
        if(isLongByRule) return letter + ": " + "true by rule";
        return letter + ": " + isLong;
    }

    public boolean isLongByRule() {
        return isLongByRule;
    }

    public void setLongByRule(boolean longByRule) {
        isLongByRule = longByRule;
    }

    public boolean isShortByRule() {
        return isShortByRule;
    }

    public void setShortByRule(boolean shortByRule) {
        isShortByRule = shortByRule;
    }
}
