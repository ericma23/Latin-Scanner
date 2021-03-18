import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Driver {

    public static String[] letters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x","y", "z", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "ɡ"};
    public static String[] vowels = new String[]{"a", "e", "i", "o", "u", "y", "A", "E", "I", "O", "U", "Y"};
    public static String[] consonants = new String[]{"b", "c", "d", "f", "g", "j", "k", "l", "m", "n", "p"
            ,"q", "r", "s", "t", "v", "w", "x", "z", "B", "C", "D", "F", "G", "J", "K", "L", "M", "N", "P", "Q", "R"
            , "S", "T", "V", "W", "X", "Z"};
    public static String[] diphthongs = new String[] {"ae", "au", "oe", "ei", "eu", "ui", "Ae", "Au", "Oe", "Ei", "Eu",
            "Ui"};
    public static String[] likelyDiphthongs = new String[] {"ae", "au", "oe", "Ae", "Au", "Oe"};
    public static String[] liquidRule = new String[] {"bl", "cl", "fl", "dl", "gl", "pl", "tl", "br", "cr", "fr", "dr", "gr", "pr", "tr", "Bl", "Cl", "" +
            "Fl", "Dl", "Gl", "Pl", "Tl", "Br", "Cr", "Fr", "Dr", "Gr", "Pr", "Tr"};
    public static HashMap<String, String> longVowels = new HashMap<String, String>() {{
       put("a", "ā");
       put("e", "ē");
       put("i", "ī");
       put("o", "ō");
       put("u", "ū");
       put("A", "Ā");
       put("E", "Ē");
       put("I", "Ī");
       put("O", "Ō");
       put("U", "Ū");
    }};
    public static HashMap<String, String> shortVowels = new HashMap<String, String>() {{
       put("a", "ă");
       put("e", "ĕ");
       put("i", "ĭ");
       put("o", "ŏ");
       put("u", "ŭ");
       put("A", "Ă");
       put("E", "Ĕ");
       put("I", "Ĭ");
       put("O", "Ŏ");
       put("U", "Ŭ");
    }};

    public static String scanLine (String line) throws Exception {
        ArrayList<Word> words = convertToWords(line);
        ArrayList<ArrayList<Vowel>> allVowels = comparePossibleScansToSoft(words, getPossibleScans(words));
        String scansion = "";
        for(ArrayList<Vowel> vowels : allVowels) {
            int index = 0;
            for (Word word : words) {
                String newWord = word.getSpelling();
                for (Vowel vowel : word.getCleanScansion()) {
                    if (vowels.get(index).isLong())
                        newWord = newWord.substring(0, vowel.getIndexInWord()) + longVowels.get(vowel.getLetter()) +
                                newWord.substring(vowel.getIndexInWord() + 1);
                    else {
                        newWord = newWord.substring(0, vowel.getIndexInWord()) + shortVowels.get(vowel.getLetter()) +
                                newWord.substring(vowel.getIndexInWord() + 1);
                    }
                    index++;
                }
                scansion += " " + newWord;
            }
            scansion += "\n";
        }
        return scansion;
    }

    public static ArrayList<ArrayList<Vowel>> comparePossibleScansToSoft(ArrayList<Word> words, ArrayList<ArrayList<Vowel>> possibleScans) {
        ArrayList<ArrayList<Vowel>> correctScans = new ArrayList<>();
        for(ArrayList<Vowel> scan : possibleScans) {
            boolean isCorrectScan = true;
            int index1 = 0; //index of possible scans
            for(Word word : words) {
                if(!isCorrectScan) {
                    break;
                }
                ArrayList<Boolean> notWorkingScansions = new ArrayList<>();
                for (ArrayList<Vowel> softScansion : word.getPossibleScansions()) {
                    int index = index1;
                    for (int i = 0; i < softScansion.size(); i++) {
                        if (softScansion.get(i).isLong() && !scan.get(index).isLong() && !scan.get(index).isLongByRule()
                                && !scan.get(index).isShortByRule()) {
                            notWorkingScansions.add((Boolean) false);
                            break;
                        }
                        index++;
                    }
                }
                if(notWorkingScansions.size() == word.getPossibleScansions().size()) isCorrectScan = false;
                index1 += word.getNumVowels();
            }
            if(isCorrectScan) correctScans.add(scan);
        }
        return correctScans;
    }

    public static ArrayList<ArrayList<Vowel>> getPossibleScans (ArrayList<Word> line) {
        ArrayList<Vowel> prelimLongs = getPrelimLongs(line);
        ArrayList<ArrayList<Vowel>> possibleScans = new ArrayList<>();

        boolean[][] allScans = DactylicHex.getScansOf(prelimLongs.size());
        for(int j = 0; j < allScans.length; j++) {
            boolean isPossibleScan = true;
            ArrayList<Vowel> scanAtIndex = new ArrayList<>();
            for (int i = 0; i < prelimLongs.size(); i++) {
                if (prelimLongs.get(i).isLongByRule() && !allScans[j][i]) {
                    isPossibleScan = false;
                    break;
                }
                if(prelimLongs.get(i).isLongByRule()) scanAtIndex.add(new Vowel(prelimLongs.get(i).getLetter(), true, true, prelimLongs.get(1).getIndexInWord()));
                else {scanAtIndex.add(new Vowel(prelimLongs.get(i).getLetter(), allScans[j][i] , false, prelimLongs.get(i).getIndexInWord()));}
            }
            if(isPossibleScan) {
                scanAtIndex.get(1).setShortByRule(true);
                scanAtIndex.get(2).setShortByRule(true);
                possibleScans.add(scanAtIndex);
            }

        }
        return possibleScans;
    }

    public static ArrayList<Word> convertToWords(String line) throws Exception {
        line += " ";
        int startOfWord = 0;
        ArrayList<Word> words = new ArrayList<>();
        for(int i = 0; i < line.length(); i++) {
            if(line.substring(i, i+1).equals(" ")) {
                words.add(scanWord(line.substring(startOfWord, i)));
                startOfWord = i + 1;
            }
        }

        markEllisions(words);
        return words;
    }

    public static ArrayList<Vowel> getPrelimLongs(ArrayList<Word> line) {

        ArrayList<Vowel> allVowels = new ArrayList<>();
        for(int i = 0; i < line.size(); i++) {
            Word word= line.get(i);
            for(int j = 0 ; j < word.getCleanScansion().size(); j++) {
                Vowel vowel = word.getCleanScansion().get(j); //


                if(vowel.getIndexInWord() == word.getSpelling().length() - 2) {

                    if(i == line.size() - 1) {}
                    else if(contains(consonants, word.getSpelling().substring(vowel.getIndexInWord() + 1, vowel.getIndexInWord() + 2)) &&
                            (contains(consonants, line.get(i+1).getSpelling().substring(0, 1))) || isConsonantalI(line.get(i+1).getSpelling(), 0)) {
                        vowel.setLongByRule(true);
                    }
                }
                else if(vowel.getIndexInWord() == word.getSpelling().length() - 1) {

                    if(i == line.size() - 1) {}
                    else if (contains(consonants, line.get(i+1).getSpelling().substring(1, 2)) || isConsonantalI(line.get(i+1).getSpelling(), 0) &&
                            contains(consonants, line.get(i+1).getSpelling().substring(0, 1)) && !contains(liquidRule, line.get(i+1).getSpelling().substring(0, 2))) {
                        vowel.setLongByRule(true);
                    }
                }
                else {

                    if(i == line.size() - 1) {}
                    else if (contains(consonants, word.getSpelling().substring(vowel.getIndexInWord() + 1, vowel.getIndexInWord() + 2)) &&
                            contains(consonants, word.getSpelling().substring(vowel.getIndexInWord() + 2, vowel.getIndexInWord() + 3)) &&
                            !contains(liquidRule, word.getSpelling().substring(vowel.getIndexInWord() + 1, vowel.getIndexInWord() + 3))) {
                        vowel.setLongByRule(true);
                    }
                }
                allVowels.add(vowel);
            }
        }

        allVowels.get(0).setLongByRule(true);
        allVowels.get(1).setLong(false);
        allVowels.get(2).setLong(false);
        allVowels.get(1).setLongByRule(false);
        allVowels.get(2).setLongByRule(false);
        allVowels.get(allVowels.size() - 2).setLongByRule(true);
        allVowels.get(allVowels.size() - 1).setLongByRule(true);

        return allVowels;
    }




    public static void markEllisions(ArrayList<Word> line) {
        for(int i = 0; i < line.size() - 1; i++) {

            Word wordAtIndex = line.get(i);
            boolean endsWithVowel = contains(vowels,wordAtIndex.getSpelling().substring(wordAtIndex.getSpelling().length() - 1))
                    || (contains(vowels, wordAtIndex.getSpelling().substring(wordAtIndex.getSpelling().length() - 2, wordAtIndex.getSpelling().length() - 1)) &&
                    wordAtIndex.getSpelling().substring(wordAtIndex.getSpelling().length() - 1).equals("m"));
            boolean nextWordStartWithVowel = (contains(vowels, line.get(i+1).getSpelling().substring(0, 1)) ||
                    (contains(vowels, line.get(i+1).getSpelling().substring(1, 2)) && line.get(i+1).getSpelling().substring(0, 1).equals("h")) ||
                    (contains(vowels, line.get(i+1).getSpelling().substring(1, 2)) && line.get(i+1).getSpelling().substring(0, 1).equals("H"))) &&
                    !isConsonantalI(line.get(i+1).getSpelling(), 0);

           // System.out.println("Ellisions: " + wordAtIndex.getSpelling() + " " + endsWithVowel + " " + nextWordStartWithVowel);
            if(endsWithVowel && nextWordStartWithVowel) {
                wordAtIndex.setEllided(true);
                wordAtIndex.skimVowel();
            }
        }
    }

    public static Word scanWord(String word) throws Exception {
        Document doc = null;
        try {
            doc = Jsoup.connect(
                    "https://en.wiktionary.org/w/index.php?title=" + word).get();
        }
        catch(Exception e) {
            if (isQueEnclitic(word)) {
                try {
                    String stemWord = word.substring(0, word.length() - 3);
                    doc = Jsoup.connect(
                            "https://en.wiktionary.org/w/index.php?title=" + stemWord).get();
                }
                catch (Exception ex) {
                    Exception wordNotFound = new Exception("word not found " + word);
                    throw(wordNotFound);
                }
            }
            else {
                Exception wordNotFound = new Exception("word not found" + word);
                throw (wordNotFound);
            }
        }
        Elements pronunciationList = null;
        try {
            pronunciationList = doc.getElementsByAttributeValue("title", "Appendix:Latin pronunciation").parents().next();
        }
        catch(Exception e) {}
        ArrayList<Vowel> vowelsOfWord = new ArrayList<>();
        if(!(pronunciationList == null) && !pronunciationList.isEmpty()) { //if the syllable breakdown is on wiktionary
            String pronunciation = pronunciationList.get(0).text(); //get unparsed pronunciation
            String syllables = "";
            for(int i = 0; i < pronunciation.length(); i++) {
                String letterAtIndex = pronunciation.substring(i, i+1);
                //System.out.println("Letter at index: " + letterAtIndex + " " + contains(letters, letterAtIndex));
                if(contains(letters, letterAtIndex) || letterAtIndex.equals(".") ||
                        letterAtIndex.equals("ˈ")) {
                    syllables += letterAtIndex;

                }
            } //parse pronunciation into syllables separated by .

            syllables += ".";
            int startSyllableIndex = 0; //beginning of each syllable
            int periodCounter = 0;
            for(int i = 0; i < syllables.length(); i++) {
                if(i == 0 && syllables.substring(0, 1).equals("ˈ")) {
                    startSyllableIndex++;
                    periodCounter++;
                    continue;
                } //skip over beginning apostrophe
                if(syllables.substring(i, i+1).equals(".") || syllables.substring(i, i+1).equals("ˈ")) {
                    String syllable = syllables.substring(startSyllableIndex, i);
                    for(Vowel vowel : findVowels(syllable, diphthongs)) {
                        vowelsOfWord.add(vowel);
                        vowel.setIndexInWord(vowel.getIndexInWord() + startSyllableIndex - periodCounter);
                        if(isConsonantalU(word, vowel.getIndexInWord()))
                            vowel.setIndexInWord(vowel.getIndexInWord() + 1);
                    }
                    startSyllableIndex = i + 1;
                    periodCounter++;
                } //add vowels
            }
        }
        else { //if pronunciation is not on wiktionary, then find vowels myself -- less accurate
            vowelsOfWord = findVowels(word, likelyDiphthongs);
        }
        Elements htmlScansion = doc.getElementsByClass("Latn headword"); //get scansions from wiktionary
        ArrayList<String> possibeScansions = new ArrayList<>();
        for(Element headWord : htmlScansion) {
            if (headWord.attr("lang").equals("la")) possibeScansions.add(headWord.text());
        }

        ArrayList<ArrayList<Vowel>> allScansions = new ArrayList<>();
        for(int i = 0; i < possibeScansions.size(); i++) { //iterate through possible scansions on wiktionary
            String scannedWord = possibeScansions.get(i);
            int index = 0; //index for individual vowels

            for (int j = 0; j < scannedWord.length(); j++) { //iterate through each invidual word + mark up vowels
                if (index >= vowelsOfWord.size()) break;
                String letterAtIndex = scannedWord.substring(j, j + 1);
                String vowelChecked = vowelsOfWord.get(index).getLetter();
                if ((contains(vowels, letterAtIndex) && vowelChecked.equals(letterAtIndex))
                        || !contains(letters, letterAtIndex) && !isConsonantalI(scannedWord, j)) {
                    if (!letterAtIndex.equals(vowelChecked)) {
                        vowelsOfWord.get(index).setLong(true);
                    }
                    index++;
                }
            }

            allScansions.add(cloneVowels(vowelsOfWord));
            for(Vowel vowel : vowelsOfWord) {
                if(vowel.isDiphthong() == false) vowel.setLong(false);
            } //reset the long marks of vowels for next possible scansion
        }

        return new Word(word, allScansions);
    }

    public static boolean isQueEnclitic(String word) {
        if(word.length() < 3) return false;
        String enclitic = word.substring(word.length() - 3);
        if(enclitic.equals("que")) {
            return true;
        }
        return false;
    }


    public static boolean isConsonantalI(String word, int index) {
        if(word.substring(index, index + 1).equals("i") || word.substring(index, index + 1).equals("I")) {

            if(word.length() == 1) return false;
            if(index == 0) return contains(vowels, word.substring(1, 2));
            if(index == word.length() - 1) return false;
            if(contains(vowels, word.substring(index - 1, index)) &&
                    contains(vowels, word.substring(index + 1, index + 2)))
                return true;
        }
        return false;
    }

    public static boolean isConsonantalU(String word, int index) {

        if(word.length() == 1) return false;
        if(index == word.length() - 1) return false;
        if(index == 0) return false;
        if(word.substring(index, index + 1).equals("u") &&
                (word.substring(index - 1, index).equals("q") || word.substring(index - 1, index).equals("Q"))) {
            return true;
        }
        if(word.substring(index, index + 1).equals("u") && (word.substring(index - 1, index).equals("g")) &&
                (contains(vowels, word.substring(index + 1, index + 2))))
            return true;
        return false;
    }

    public static ArrayList<Vowel> findVowels(String word, String[] diphthongsToUse) {
        ArrayList<Vowel> vowelBreakdown = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            String letterAtIndex = word.substring(i, i+1);
            if (contains(vowels, letterAtIndex) && !isConsonantalI(word, i) && !isConsonantalU(word, i)) {
                if (i <= word.length() - 2 &&
                        contains(diphthongsToUse, word.substring(i, i + 2))) {
                    vowelBreakdown.add(new Vowel(letterAtIndex, true, true, i));
                    i++;
                }
                else {
                    vowelBreakdown.add(new Vowel(letterAtIndex, false, false, i));
                }
            }
        }
        return vowelBreakdown;
    }

    public static boolean contains(String[] arr, String stringChecked) {
        for(int i = 0; i < arr.length; i++) {
            if(arr[i].equals(stringChecked)) return true;
        }
        return false;
    }

    public static ArrayList<Vowel> cloneVowels(ArrayList<Vowel> vowels) {
        ArrayList<Vowel> clonedVowels = new ArrayList<>();
        for(Vowel vowel: vowels) {
            clonedVowels.add(new Vowel(vowel.getLetter(), vowel.isLong(), vowel.isDiphthong(), vowel.getIndexInWord()));
        }
        return clonedVowels;
    }
}