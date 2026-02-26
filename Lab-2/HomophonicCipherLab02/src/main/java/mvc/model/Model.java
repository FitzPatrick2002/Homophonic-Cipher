/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.model;

//import java.util.Map;
import java.util.HashMap;
//import java.lang.Math;
//import java.util.Set;
import java.util.Map;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Represents the Model component in the MVC pattern.
 * Its primary task is the implementation of the homophonic substitution cipher.
 * This includes:
 * - Homophonic key generation. Key is generated based on customizable parameters such as 
 *   single phonogram length, maximal and minimal number of phonograms per letter.
 * - Encoding of strings.
 * - Decoding of strings according to generated key as well as the default key.
 * Encoding characters (phonograms) length and number assinged to letters can be customized,
 * however combination of these values must meet specific requirements so that a valid key can be generated.
 * 
 * @author Milosz Liniewiecki
 * @version 1.0
 */
public class Model {
    
    /**
     * The main homophony encoding table. 
     * Maps a single encoded character (String) to array of its encryption characters (String[]).
     */
    private Map<String, String[]> key;
    
    /**
     * Total number of all phonograms in the {@link #key}.
     */
    private int totalPhonogramsNum;
    
    /**
     * Fixed number of digits composing a phongram e.g., 3 means '042' is a valid phonogram.
     * Determines the span of phonograms (10^N).
     */
    private int phonogramLength;
    
    /**
     * Maximum number of phonograms assigned to single letter.
     * Letters with higher frequencies {@link #frequencyTable} will have 
     * number of phonograms close to this maximum.
     */
    private int maxPhonograms;
    
    /**
     * Minimum number of phonograms assigned to single letter.
     * Letters with lower frequencies {@link #frequencyTable} will have 
     * number of phonograms close to this minimum.
     */
    private int minPhonograms;
    
    /**
     * Array of all characters that can be encoded (e.g., [A-Za-z0-9]).
     * Includes special characters: ? !.: and space.
     */
    private ArrayList<String> allEncodedCharacters;
    
    /**
     * Array of all phonograms assigned to letters in {@link #key}.
     * Encryption characters are strings of letters of fixed length {@link #phonogramLength}.
     */
    private ArrayList<String> allPhonograms;
    
    /**
     * Table which maps letters of english alphabet to their frequency of occuring in the written language. 
     * Frequencies are expresed in %. 
     */
    private Map<String, Double> frequencyTable;
    
    /**
     * A compiled {@link Pattern} which specifies characters not valid for encoding. 
     * The pattern is {@code [^A-Za-z0-9:_.! ]}.
     */
    private Pattern nonHandledCharacters;
    
    // --------- Constructor --------- //
    
    /**
     * Constructs the Model instances and initilizes internal parameters: 
     * {@link #allEncodedCharacters}, {@link #allPhonograms}, {@link #key}, 
     * {@link #frequencyTable}, {@link #nonHandledCharacters}.
     * The frequency table is initilized with letters from the english alphabet.
     */
    public Model(){
        this.allEncodedCharacters = new ArrayList<>();
        this.allPhonograms = new ArrayList<>();
        this.key = new HashMap<>();
        
        // Frequency table of all letters in the english alphabet
        this.frequencyTable = new HashMap<>();
        
        // Lower case letters
        frequencyTable.put("e", 12.7);
        frequencyTable.put("t", 9.1);
        frequencyTable.put("a", 8.2);
        frequencyTable.put("o", 7.5);
        frequencyTable.put("i", 7.0);
        frequencyTable.put("n", 6.7);
        frequencyTable.put("s", 6.3);
        frequencyTable.put("h", 6.1); 
        frequencyTable.put("r", 6.0); 
        frequencyTable.put("d", 4.3); 
        frequencyTable.put("l", 4.0);
        frequencyTable.put("c", 2.8);
        frequencyTable.put("u", 2.8);
        frequencyTable.put("m", 2.4);
        frequencyTable.put("w", 2.4);
        frequencyTable.put("f", 2.2);
        frequencyTable.put("g", 2.0);
        frequencyTable.put("y", 2.0);
        frequencyTable.put("p", 1.9);
        frequencyTable.put("b", 1.5); 
        frequencyTable.put("v", 0.98); 
        frequencyTable.put("k", 0.77);
        frequencyTable.put("j", 0.15);
        frequencyTable.put("x", 0.15); 
        frequencyTable.put("q", 0.095); 
        frequencyTable.put("z", 0.074);
        
        // Capital letters
        frequencyTable.put("I", 0.65);
        frequencyTable.put("T", 0.4);
        frequencyTable.put("A", 0.35);
        frequencyTable.put("S", 0.30);
        frequencyTable.put("W", 0.25);
        frequencyTable.put("H", 0.20);
        frequencyTable.put("M", 0.18);
        frequencyTable.put("C", 0.17); 
        frequencyTable.put("P", 0.15); 
        frequencyTable.put("B", 0.13); 
        frequencyTable.put("E", 0.12);
        frequencyTable.put("O", 0.11);
        frequencyTable.put("D", 0.10);
        frequencyTable.put("L", 0.08);
        frequencyTable.put("R", 0.07);
        frequencyTable.put("F", 0.06);
        frequencyTable.put("G", 0.05);
        frequencyTable.put("N", 0.04);
        frequencyTable.put("K", 0.03);
        frequencyTable.put("J", 0.02); 
        frequencyTable.put("U", 0.01); 
        frequencyTable.put("V", 0.008);
        frequencyTable.put("Y", 0.007);
        frequencyTable.put("X", 0.002); 
        frequencyTable.put("Z", 0.001); 
        frequencyTable.put("Q", 0.001); 
        
        // Numbers 
        frequencyTable.put("0", 0.001);
        frequencyTable.put("1", 0.001);
        frequencyTable.put("2", 0.001);
        frequencyTable.put("3", 0.001);
        frequencyTable.put("4", 0.001);
        frequencyTable.put("5", 0.001);
        frequencyTable.put("6", 0.001);
        frequencyTable.put("7", 0.001);
        frequencyTable.put("8", 0.001);
        frequencyTable.put("9", 0.001);
        
        // Special characters
        frequencyTable.put(".", 0.001);
        frequencyTable.put(":", 0.001);
        frequencyTable.put("_", 0.001);
        frequencyTable.put("!", 0.001);
        frequencyTable.put("?", 0.001);
        
        // Create regex for non handled characters
        this.nonHandledCharacters = Pattern.compile("[^A-Za-z\\d!.:?_ ]");
        
        // Parse the frequency table and save all characters
        for(Map.Entry<String, Double> e : this.frequencyTable.entrySet()){
            String character = e.getKey();
            this.allEncodedCharacters.add(character);
        }
    }
    
    // --------- Methods --------- //
    
    /**
     * Initilizes fields related to key generation and generates the key (or uses default one).
     * Clears the key before generating it.
     * 
     * @param phonogramLength value assinged to {@link #phonogramLength}
     * @param maxPhonograms value assinged to {@link #maxPhonograms}
     * @param minPhonograms value assinged  to {@link #minPhonograms}.
     * @param useDefault if true, default key is used.
     */
    public void init(int phonogramLength, int maxPhonograms, int minPhonograms, boolean useDefault){
        
        // Check in here if the phonogramLength is sufficient for handling phonogramsPerCharacter
        // ex. len = 2 -> nums 00-99 -> 100 option, 26 letters, we cannot have > 99 / 26 phonograms per character
        this.phonogramLength = phonogramLength;
        this.maxPhonograms = maxPhonograms;
        this.minPhonograms = minPhonograms;
        this.totalPhonogramsNum = 0;
        
        // Clear the key before regenerating it
        this.key.clear();
        
        if(useDefault == true){
            this.generateDefaultKey();
        }
        else{
            this.generateKey();   
        }
    }
    
    // --------- Encryption and decryption --------- //
    
    /**
     * Accepts a message string and encrypts it according to {@link #key}.
     * Trailing and preceding whitespaces are removed if they are present. 
     * Whitespaces are not encoded.
     * Function iterates over all letters from the input string
     * and assignes each one a randomly selected phonogram according to the key.
     * 
     * @param inputString string that is to be encrypted.
     * @return Encrypted string.
     */
    public String encrypt(String inputString){
        String outcome = "";
        
        // Remove front and trailing spaces and convert multiple spaces to single space only
        inputString = inputString.strip();
        inputString = inputString.replaceAll("\\s{2,}", " "); // Convert multiple whitespace into single only
        
        // Convert each character from the input string to the corresponding homophony.
        for(int i = 0; i < inputString.length(); i++){
            // Get character at the current position
            String character = Character.toString(inputString.charAt(i)); 
            
            // If the current character is a whitespace, leave it be and append.
            // If not, encrypt it and append.
            String newChar = " ";
            if(!character.equals(" ")){
                newChar = this.encryptCharacter(character);
            }
            outcome = outcome + newChar;   
        }
        
        return outcome;
    }
    
    /**
     * Accepts a message string, composed of phonograms and decodes it to english alphabet.
     * Removes leading and trailing whitespaces.
     * Whitespaces are not decoded, they remain unchanged.
     * Function iterates over phongrams, {@link #phonogramLength} specify single phonogram length.
     * Each phonogram is mapped to letter from english alphabet present in the key.
     * 
     * @param inputString encoded string should contain only digits and spaces.
     * @return input string decrypted to english alphabet.
     */
    public String decrypt(String inputString){
        String outcome = "";
        
        // Remove front and trailing spaces and convert multiple spaces to single space only
        inputString = inputString.strip();
        inputString = inputString.replaceAll("\\s{2,}", " "); // Convert multiple whitespace into single only
        
        for(int i = 0; i < inputString.length(); i += this.phonogramLength){
            // If the first character is space, omit it.
            if(inputString.charAt(i) == ' '){
                i += 1;
                outcome += " ";
            }
            // Get the full coded character
            String encryptedChar = inputString.substring(i, i + this.phonogramLength);
            String decryptedChar = this.decryptCharacter(encryptedChar);
            
            outcome += decryptedChar;
        }
        
        return outcome;
    }
    
    /**
     * Accepts a single letter and assigns it a randomly selected phonogram according to the {@link #key}.
     * 
     * @param character english alphabet letter or special character present in {@link #allEncodedCharacters}.
     * @return phonogram assigned to the input letter.
     */
    private String encryptCharacter(String character){
        String[] arr = this.key.get(character);
        int randomIdx = (int)(Math.random() * arr.length);
        return arr[randomIdx];
    }
    
    /**
     * Decrypts a phongram back to english letter.
     * Iterates over all letters in the {@link #key} and checks if the given phonogram
     * is contained in the array of phonograms assigned to that letter.
     * @return decrypted character.
     */
    private String decryptCharacter(String codedCharacter){
        // Check if character is in valid form 
        //(length is a multiple of a single coded character length, every char in the input string is a number, etc.)
        
        // Parse the entire hash table and look, which ASCII character contains given number in its coding table. 
        for(Map.Entry<String, String[]> e : this.key.entrySet()){
            // Get the ASCII character and array of coding numbers.
            String keyLetter = e.getKey();
            String[] arr = e.getValue();
            
            // Check if the coded character is in the list of coding numbers. 
            if(Arrays.asList(arr).contains(codedCharacter)){
                return keyLetter;
            }
        }
        
        // Later make a try catch and throw an error if none character matches
        return "=";
    }
    
    /**
     * Validates encrypted string, if it contains legal phonogram characters (phonogram must belong to {@link #allPhonograms}.
     * Leading and trailing whitespaces are removed. Spaces that take more than one space are normalised.
     * Splits the given string on whitespaces and iterates over specific words.
     * Extracts phonograms of length {@link #phonogramLength} and checks if {@link #allPhonograms} contains it.
     * 
     * @param inputString Encoded string that we want to decode.
     * @throws UnknownPhonogramException in case an unknown phonogram has been found in the string.
     * @return true if the string is validand contains only known phongrams. Otherwise exception is thrown.
     */
    public boolean testEncryptedString(String inputString) throws UnknownPhonogramException, StringIndexOutOfBoundsException{
        // Split the string into separate words 
        
        // Remove front spaces and convert multiple spaces to single space only
        inputString = inputString.strip();
        inputString = inputString.replaceAll("\\s{2,}", " "); // Convert multiple whitespace into single only
        String[] arr = inputString.split("[ ]");
        
        // Iterate over words from provided string, check if symbols in them correspond to those that are availabale in the key
        int cursor = 0;
        for(String word : arr){
            // Try to read character
            while(cursor < word.length()){
                String character = word.substring(cursor, cursor + this.phonogramLength);
                cursor += this.phonogramLength;
                if(this.allPhonograms.contains(character) == false){
                    throw new UnknownPhonogramException("Unknown character has been specified in the encrypted text: " + character);
                }
            }
            cursor = 0; // Reset cursor for a new word
        }
        return true;
    }
    
    // --------- Key generation --------- //
    
    /**
     * Converts integer number to String of length {@link #phonogramLength}.
     * If number of digits is lesser than the length of phonogram, leading 0's are added.
     * e.x. if input number equal 45 and phongram length is 3, '045' string is returned.
     * 
     * @param num interger which will be converted to phonogram.
     * @return integer converted to string.
     */
    private String generatePhongram(int num){
        String value = Integer.toString(num);
        String prefix = String.valueOf('0').repeat(this.phonogramLength - value.length());
        return prefix + value;
    }
    
    /**
     * For each letter from {@link #allEncodedCharacters} an array of encryption charaters is generated.
     * Number of phonograms assigned to each letter depends on the frequency of that letter {@link #frequencyTable}.
     * Phonograms are generated as integer numbers of fixed length.
     */
    private void generateKey(){        
        // Populate the list of numbers
        LinkedList<Integer> integers = new LinkedList<>();
        int phonogramsNumber = (int)Math.pow(10, this.phonogramLength);
        for(int i = 0; i < phonogramsNumber; i++){
            integers.add(i);
        }
        
        // Generate phonograms based on frequency of letters
        for(Map.Entry<String, Double> e : this.frequencyTable.entrySet()){
            String letter = e.getKey();
            double frequency = e.getValue();
            
            // How many phonograms will be assigned to this letter
            int numPhonograms = this.minPhonograms + (int)((frequency / 12.7) * (double)(this.maxPhonograms - this.minPhonograms));
            
            // Poll numbers from the list, turn them into phonograms and assign to code the letter.
            ArrayList<String> letterPhonograms = new ArrayList<>();
            for(int i = 0; i < numPhonograms; i++){
                int idx = (int)(Math.random() * integers.size());
                int num = integers.remove(idx);
                String phonogram = this.generatePhongram(num);
                
                letterPhonograms.add(phonogram);
                this.allPhonograms.add(phonogram); // Save the phonogram also in exernal list
            }
            
            this.totalPhonogramsNum += letterPhonograms.size(); // Count how many phonograms are in use.
            this.key.put(letter, letterPhonograms.toArray(String[]::new));
        }
    }
    
    /**
     * Assigns each letter from {@link #allEncodedCharacters} a single encryption character.
     * Length of a phonogram is specified by {@link #phonogramLength}. 
     */
    private void generateDefaultKey(){
        int num = 0;
        for(Map.Entry<String, Double> e : this.frequencyTable.entrySet()){
            String letter = e.getKey();
            String phonogram = this.generatePhongram(num);
            
            ArrayList<String> letterPhonograms = new ArrayList<>();
            letterPhonograms.add(phonogram);
            
            this.allPhonograms.add(phonogram);
            this.key.put(letter, letterPhonograms.toArray(String[]::new));
            num++;
        }
    }
    
    /**
     * Returns number of all phonograms.
     * 
     * @return value of {@link #totalPhonogramsNum}.
     */
    public int getTotalPhonogramsNum(){
        return this.totalPhonogramsNum;
    }
    
    /**
     * Returns the pattern, which specifies the illegal characters.
     * 
     * @return pattern {@link #nonHandledCharacters}.
     */
    public Pattern getNonHandledCharacters(){
        return this.nonHandledCharacters;
    }
    
    /**
     * Returns the array of valid characters that can be encoded.
     * 
     * @return valid characters from {@link #allEncodedCharacters}.
     */
    public String[] getAllEncodedCharacters(){
        return this.allEncodedCharacters.toArray(String[]::new);
    }
    
    /**
     * Returns all encryption characters.
     * 
     * @return encryption characters from {@link #allPhonograms}.
     */
    public String[] getAllPhonograms(){
        return this.allPhonograms.toArray(String[]::new);
    }
    
    /**
     * Calculates the nuber of phongrams that will be needed to encode the whole alphabet.
     * Iterates over the frequency table {@link #frequencyTable} and calculates number of phongrams that
     * will be generated for each letter. 
     * Sum of these values is returned.
     * 
     * 
     * @param suggestedMin minimal number of phonograms per letter.
     * @param suggestedMax maximal number of phongrams per letter.
     * @return total number of phonograms that will be generated for the alphabet.
     * @see isPhonogramsNumSufficient
     */
    public int suggestedNumberOfPhonograms(int suggestedMin, int suggestedMax){
        int totalTheoretical = 0;
        // Sum all frequencies and check how many phonograms will be generated
        for(Map.Entry<String, Double> e : this.frequencyTable.entrySet()){
            double frequency = e.getValue();
            totalTheoretical += suggestedMin + (int)((frequency / 12.7) * (double)(suggestedMax - suggestedMin));
        }
        
        return totalTheoretical;
    }
    
    /**
     * Checks if provided combination of minimal and maximal numbers of phongrams, assigned to a single letter 
     * as well as the length of a phonogram are valid.
     * Total number of generated phonogram characters is equal 10^N, where N = {@link #phonogramLength}.
     * It must exceed value returned by {@link #suggestedNumberOfPhonograms}.
     * If so, the combination of parameters is considered valid.
     * 
     * @param suggestedMax suggested value for {@link #maxPhonograms}.
     * @param suggestedMin suggested value for {@link #minPhonograms}.
     * @param suggestedLength suggested value for {@link #phonogramLength}.
     * @return true in case of valid parameter combination.
     */
    public boolean isPhonogramsNumSufficient(int suggestedMin, int suggestedMax, int suggestedLength){
        int suggestedAmount = (int)Math.pow(10, suggestedLength);
        int totalTheoretical = this.suggestedNumberOfPhonograms(suggestedMin, suggestedMax);
        return totalTheoretical < suggestedAmount;
    }
    
    /**
     * Returns the main homophony encoding table.
     * Encoded letter (String) is mapped to an array of encoding characters (String[]).
     * 
     * @return homophony map {@link #key}. 
     */
    public Map<String, String[]> requestKey(){
        return this.key;
    }
}

