import java.lang.reflect.Array;
import java.util.ArrayList;

public class Word {
    private String spelling;
    private ArrayList<ArrayList<Vowel>> possibleScansions;
    private boolean isEllided;
    private int numVowels;

    public Word (String spelling, ArrayList<ArrayList<Vowel>> vowels) {
        this.spelling = spelling;
        this.possibleScansions = vowels;
        this.numVowels = vowels.get(0).size();
    }

    public String getSpelling() {
        return spelling;
    }

    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }

    public ArrayList<ArrayList<Vowel>> getPossibleScansions() {
        return possibleScansions;
    }

    public void setPossibleScansions(ArrayList<ArrayList<Vowel>> possibleScansions) {
        this.possibleScansions = possibleScansions;
    }

    public boolean isEllided() {
        return isEllided;
    }

    public void setEllided(boolean ellided) {
        isEllided = ellided;
    }
    public int getNumVowels() {
        if(isEllided) return numVowels - 1;
        return numVowels;
    }

    public ArrayList<Vowel> getCleanScansion() {
        ArrayList<Vowel> cleanScansion = new ArrayList<>();
        for(Vowel vowel : possibleScansions.get(0)) {
            cleanScansion.add(new Vowel(vowel.getLetter(), false, vowel.isDiphthong(), vowel.getIndexInWord()));
        }
        return cleanScansion;
    }

    public void skimVowel() {
        for(ArrayList<Vowel> scansion : possibleScansions) {
            if(!scansion.isEmpty()) {
                scansion.remove(scansion.size() - 1);
            }
        }
    }
}
