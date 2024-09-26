import java.io.*;
import java.util.*;

public class AnagramSolv {
    private static TrieSTNew<String> dict = new TrieSTNew<>();
    private static String dictonary = "dictionary.txt";
    private static String inputFileName = "";
    private static String outputFileName = "";
    private static File inputFile = null;
    private static File outputFile = null;

    // Main Method
    public static void main(String[] args) throws IOException {
        processFiles();
        Scanner fileScanner = new Scanner(new FileInputStream(dictonary));
        while (fileScanner.hasNext()) {
            String word = fileScanner.next();
            dict.put(word, word);
        }
        fileScanner.close();
        loadAnagrams();
    }

    // Ask for chars and output files
    private static void processFiles() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter input filename: ");
        String inputFileName = sc.nextLine();
        System.out.print("Enter output filename: ");
        String outputFileName = sc.nextLine();
        sc.close();
        inputFile = new File(inputFileName);
        outputFile = new File(outputFileName);
        if (new File(outputFileName).exists()) {
            new File(outputFileName).delete();
        }
    }

    // Method to print and alter .toString() method
    private static void loadAnagrams() throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new FileInputStream(inputFile));
        PrintWriter writer = new PrintWriter(new FileOutputStream(outputFile));
        while (fileScanner.hasNextLine()) {
            String word = fileScanner.nextLine();
            writer.println("Here are the results for " + "'" + word + "':");
            word = word.toLowerCase().replaceAll("\\s", "");
            printAnagrams(word, writer);
            writer.println();
        }
        fileScanner.close();
        writer.close();
    }

    // Method to find all the anagrams of a given word
    public static HashSet<List<String>> anagramSolver(String chars) {
        HashSet<List<String>> anagrams = new HashSet<>();
        ArrayList<String> currentAnagram = new ArrayList<>();
        generateAnagrams(chars.toCharArray(), new StringBuilder(), currentAnagram, new boolean[chars.length()],
                anagrams, 0);
        return anagrams;
    }

    private static void generateAnagrams(char[] chars, StringBuilder currWord, ArrayList<String> currentAnagram,
            boolean[] wordIsUsed, HashSet<List<String>> anagrams, int currWordLength) {
        if (currWordLength == chars.length) {
            String currentStr = currWord.toString();
            if (dict.contains(currentStr)) {
                currentAnagram.add(currentStr);
                boolean allContained = true;
                for (String word : currentAnagram) {
                    if (!dict.contains(word)) {
                        allContained = false;
                        break;
                    }
                }
                if (allContained) {
                    anagrams.add(new ArrayList<>(currentAnagram));
                }
                currentAnagram.remove(currentAnagram.size() - 1);
            }
            return;
        }
        for (int i = 0; i < chars.length; i++) {
            if (wordIsUsed[i]) {
                continue;
            }
            currWord.append(chars[i]);
            wordIsUsed[i] = true;
            if (dict.searchPrefix(currWord.toString()) != 0) {
                if (dict.contains(currWord.toString())) {
                    currentAnagram.add(currWord.toString());
                    generateAnagrams(chars, new StringBuilder(), currentAnagram, wordIsUsed, anagrams,
                            currWordLength + 1);
                    currentAnagram.remove(currentAnagram.size() - 1);
                }
                generateAnagrams(chars, currWord, currentAnagram, wordIsUsed, anagrams, currWordLength + 1);
            }
            currWord.deleteCharAt(currWord.length() - 1);
            wordIsUsed[i] = false;
        }
    }

    // Helper method to Print Anagrams & bulk of sorting and comparing
    private static void printAnagrams(String word, PrintWriter writer) {
        int totalAnagrams = 0;
        int singleWordSolutions = 0;
        int multiWordSolutions = 0;
        Set<List<String>> anagrams = anagramSolver(word);
        class AnagramComparator implements Comparator<List<String>> {
            @Override
            public int compare(List<String> listOne, List<String> listTwo) {
                int listComparison = Integer.compare(listOne.size(), listTwo.size());
                if (listComparison != 0) {
                    return listComparison;
                }

                // Compare the words in the lists alphabetically to sort them
                for (int i = 0; i < listOne.size(); i++) {
                    int wordComp = listOne.get(i).compareTo(listTwo.get(i));
                    if (wordComp != 0) {
                        return wordComp;
                    }
                }
                // If the lists are equal return 0
                return 0;
            }
        }
        List<List<String>> sortedAnagrams = new ArrayList<>(anagrams);
        Collections.sort(sortedAnagrams, new AnagramComparator());
        List<List<String>> tempAnagram = new ArrayList<>();
        List<List<String>> tempMultiAnagrams = new ArrayList<>();
        for (List<String> anagram : sortedAnagrams) {
            if (anagram.size() == 1) {
                tempAnagram.add(anagram);
                singleWordSolutions++;
                totalAnagrams++;
            }
        }
        if (singleWordSolutions > 0) {
            writer.println("There were " + singleWordSolutions + " 1-word solutions:");
            for (List<String> anagram : tempAnagram) {
                for (String wordAnagram : anagram) {
                    writer.println(wordAnagram);
                }
            }
        }
        for (int i = 2; i <= sortedAnagrams.size(); i++) {
            tempMultiAnagrams.clear();
            for (List<String> anagram : sortedAnagrams) {
                if (anagram.size() == i) {
                    tempMultiAnagrams.add(anagram);
                    multiWordSolutions++;
                    totalAnagrams++;
                }
            }
            if (multiWordSolutions > 0) {
                writer.println("There were " + multiWordSolutions + " " + i + "-word solutions:");
                for (List<String> anagram : tempMultiAnagrams) {
                    for (String wordAnagram : anagram) {
                        writer.print(wordAnagram + " ");
                    }
                    writer.println();
                }
            }
            multiWordSolutions = 0;
        }
        // Print the total number of solutions
        writer.println("There were a total of " + totalAnagrams + " solutions");
    }

    // Helper method to compare
    public int compareAnagrams(List<String> listOne, List<String> listTwo) {
        int listComparison = Integer.compare(listOne.size(), listTwo.size());
        if (listComparison != 0) {
            return listComparison;
        }

        // Compare the words in the lists alphabetically to sort them
        for (int i = 0; i < listOne.size(); i++) {
            int wordComp = listOne.get(i).compareTo(listTwo.get(i));
            if (wordComp != 0) {
                return wordComp;
            }
        }
        // If the lists are equal return 0
        return 0;
    }

}
