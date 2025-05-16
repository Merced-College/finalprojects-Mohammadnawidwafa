/**
 * Class: CPSC-39
 * FINAL PROJECT: SCRABBLE-LIKE GAME
 * Name: Mohammad Nawid Wafa
 * Date: April 30, 2025
 */
package scrabblegame;

import java.io.*;
import java.util.*;

public class ScrabbleGame {
	// List of all the valid words from the dictionary
    private ArrayList<Word> words; 
    // This Keeps track of all words that was already used during the game
    private HashSet<String> usedWords = new HashSet<>(); 
    // Stack is used to store the history of words for the undo feature in the game 
    private Stack<String> wordHistory = new Stack<>();
    // Queue is used to manage player turns going from player 1 to two to 1 and so on 
    private Queue<String> playerQueue = new LinkedList<>();
    // List of vowels to make sure that each random letter set from dictionary has at least a vowel
    private LinkedList<Character> vowelPool = new LinkedList<>();
    private int player1Score = 0; // Tracks the score for player 1
    private int player2Score = 0; // Tracks the score for player 2

    public ScrabbleGame() throws IOException {
    	// creates an empty list to hold valid words
        words = new ArrayList<>();
        // loads words from dictionary file to the list
        loadWords("CollinsScrabbleWords_2019.txt");
        // fills the vowel pool with vowels to use when generating random letters
        vowelPool.addAll(Arrays.asList('a', 'e', 'i', 'o', 'u'));
    }
    //*From my Scrabble- like Game: Start//
    private void loadWords(String fileName) throws IOException {
    	// opens the file for reading using BufferedReader 
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        // reads each line from the file and each line is a word
        while ((line = br.readLine()) != null) {
        	// it gets rid of any extra spaces and adds the word to the list
            words.add(new Word(line.trim()));
        }
        br.close(); // closes the file
        Collections.sort(words); // sorts the list of words alphabetically 
    }
    // it checks if the input word exists in the loaded word list
    private boolean isValidWord(String input) {
    	// it converts the input to lowercase and searches for it in the sored word list using the bionary search
    	// returns true if the word is found otherwise false 
        return Collections.binarySearch(words, new Word(input.toLowerCase())) >= 0;
    }
    
    /** Can Form Word Algorithm
     * Convert the array of letters to a string.
     * Loop through each character in the input word.
     * Check if the character exists in temporary string. 
     * if not found, return false.
     * if found, remove that character from temporary string.
     * After the loop, return true. 
     */
    // Checks if the given word can be formed using the letters list that are available 
    private boolean canFormWord(String word, char[] chars) {
    	// Converts the character array to a temporary string for easy editing 
        String temp = new String(chars);
        // Loops through each letter in the players word 
        for (char c : word.toCharArray()) {
        	// find the index of the letter in the temporary string
            int index = temp.indexOf(c);
            // if the word is not found it can not be formed 
            if (index == -1) return false;
            // remove the used letter from the temporary string
            temp = temp.substring(0, index) + temp.substring(index + 1);
        }
        return true; //if all the letters were found and removed, the word can be formed 
    }
    //* From my Scrabble - like Game: end//
    //* My codes//
    
    // Generates an array with 5 random letters, and makes sure there is at least one vowel 
    private char[] generateLettersWithVowel(Random random) {
    	// creates an array to hold 5 letters
        char[] letters = new char[5];
        // makes sure that the first letter is a vowel 
        letters[0] = vowelPool.get(random.nextInt(vowelPool.size()));
        // fill the remaining 4 letters with random letters from the alphabet 
        for (int i = 1; i < 5; i++) {
            letters[i] = (char) ('a' + random.nextInt(26));
        }
        return letters; // returns the 5 letter set 
    }
    //*From my Scrabble- like Game//
    
    /** Word Scoring Algorithm
     * Get the length of the submitted word
     * if length is 2-3 assign 1 point. 
     * if length is exactly 4 assign 3 points. 
     * if length is 5 or more, assign 5 points. 
     * Return the score
     */
    
    // calculates the score for a word based on its length (1-5)
    private int calculatePoints(String word) {
        int len = word.length(); // get the length of the word
        if (len <= 3) return 1; // 1-3 letter words are worth 1 point
        else if (len == 4) return 3; // words with 4 letters exact are 3 points
        else return 5; // 5 letter words are worth 5 points 
    }
    //* From my Scrabble- like Game end code//
    //* my codes: //
    
    // adds both players to the queue to manage their turns 
    private void addPlayers() {
        playerQueue.add("Player 1");
        playerQueue.add("Player 2");
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        addPlayers(); // add both players to the turn queue 
        int rounds = 6; // total of 6 rounds 
        
        // loops through each round 
        for (int currentRound = 1; currentRound <= rounds; currentRound++) {
            System.out.println("--- Round " + currentRound + " of " + rounds + " ---");
            
            // each player takes one turn per round 
            for (int p = 0; p < 2; p++) {
            	// Gets the next player in line and add them back to the end of the queue
                String currentPlayer = playerQueue.poll();
                playerQueue.add(currentPlayer);

                // Generates  a random set of 5 letters with one vowel at least 
                char[] letters = generateLettersWithVowel(random);
                System.out.println(currentPlayer + "'s turn.");
                System.out.println("Your letters are: " + Arrays.toString(letters));

                // Asks the player to enter a word or type "undo"
                System.out.print("Enter a word using these letters (or type 'undo'): ");
                String userWord = scanner.nextLine().toLowerCase();

                // If the player types "undo", remove the last word they played
                if ("undo".equals(userWord) && !wordHistory.isEmpty()) {
                    String removed = wordHistory.pop();
                    usedWords.remove(removed);
                    System.out.println("Last word '" + removed + "' has been undone.");
                    continue; // Skips the rest of this turn
                }

                // Checks if the word uses only the given letters
                if (!canFormWord(userWord, letters)) {
                    System.out.println("Invalid word: uses letters not in the set.");
                    continue; // Skip scoring
                }

                // Checks if the word has already been used
                if (usedWords.contains(userWord)) {
                    System.out.println("Word already used.");
                    continue; // Skip scoring
                }

                // Checks if the word is in the dictionary
                if (isValidWord(userWord)) {
                    usedWords.add(userWord); // Marks word as used
                    wordHistory.push(userWord); // Saves word to history for undo

                    // Calculates points and adds to the correct player's score
                    int points = calculatePoints(userWord);
                    if (currentPlayer.equals("Player 1")) {
                        player1Score += points;
                    } else {
                        player2Score += points;
                    }

                    System.out.println("Valid word! You scored " + points + " point(s)!");
                } else {
                    System.out.println("That word is not in the Scrabble dictionary.");
                }
            }
        }

        System.out.println("--- Game Over ---"); // final results 
        System.out.println("Player 1 final score: " + player1Score);
        System.out.println("Player 2 final score: " + player2Score);
        if (player1Score > player2Score) System.out.println("Player 1 wins!");
        else if (player2Score > player1Score) System.out.println("Player 2 wins!");
        else System.out.println("It's a tie!");
        scanner.close();
    }

    

    /**
     * This method finds all possible letter combinations from a given string.
     * It adds each combination to the 'results' set.
     */
    // Recursive Substring Generator
    private void buildSubstrings(String prefix, String rest, Set<String> results) {

        // If prefix is not empty, save it as a possible word
        if (!prefix.isEmpty()) results.add(prefix);

        // Loop through each letter in the rest of the string
        for (int i = 0; i < rest.length(); i++) {
            // Add the current letter to the prefix
            // Remove that letter from the rest
            // Call the method again with the new values
            buildSubstrings(prefix + rest.charAt(i), rest.substring(i + 1), results);
        }
    }
 // Finds and displays all valid words that can be made from the given letters
    public void showAllValidWordsFromLetters(String letters) {
        // Creates a set to store all possible combinations of letters 
        Set<String> allCombos = new HashSet<>();

        // Uses the recursive method to generate all combinations from the letters
        buildSubstrings("", letters, allCombos);

        // Checks each combination and print it if it's a valid dictionary word
        System.out.println("Valid words you could have made:");
        for (String word : allCombos) {
            if (isValidWord(word)) {
                System.out.println(word);
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Creates a new Scrabble game and start it
            ScrabbleGame game = new ScrabbleGame();
            game.play();
        } catch (IOException e) {
            // Shows an error if the word list couldn't be loaded
            System.out.println("Error loading dictionary: " + e.getMessage());
        }
    }
}