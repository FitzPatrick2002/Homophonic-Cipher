/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.stream.*;
import mvc.model.KeyParameters;
import mvc.model.KeyParameters;
import mvc.model.FrequencyPair;
import java.util.Optional;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.stream.*;
import java.lang.Math;

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
 * @version 1.2
 */

@Getter 
@FieldDefaults(level=AccessLevel.PRIVATE) // By default all fields are private
@AllArgsConstructor() // AllArgsConstructor(onConstructor=@__(@Deprecated)) Creates a constructor with all arguments and deprecates it. The default one, implemented is now more important
@EqualsAndHashCode // If somehow I get to the point where I'll be having 2 models, then this will let me compare them easily
public class Model {
    
    /**
     * The main homophony encoding table. 
     * Maps a single encoded character (String) to a List of its encryption characters (List of Strings).
     */
    final  Map<String, List<String>> key;
    
    /**
     * Total number of all phonograms in the {@link #key}.
     */
    int totalPhonogramsNum;

    /**
     * Stores parameters necessary for key generation.
     */
    KeyParameters keyParameters;

    /**
     * Array of all characters that can be encoded (e.g., [A-Za-z0-9]).
     * Includes special characters: ? !.: and space.
     */
    final private ArrayList<String> allEncodedCharacters;
    
    /**
     * Array of all phonograms assigned to letters in {@link #key}.
     * Encryption characters are strings of letters of fixed length given by{@link #keyParameters} phonogramLength.
     */
    final private ArrayList<String> allPhonograms;
    
    /**
     * Table which maps letters of english alphabet to their frequency of occuring in the written language. 
     * Frequencies are expresed in %. 
     */
    //final private Map<String, Double> frequencyTable;
    final private List<FrequencyPair> frequencyTable;
    
    /**
     * A compiled {@link Pattern} which specifies characters not valid for encoding. 
     * The pattern is {@code [^A-Za-z0-9:_.! ]}.
     */
    final private Pattern nonHandledCharacters;
    
    /**
     * Stores history records of all operations performed in the app.
     */
    private List<HistoryRecord> historyList;
    
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
        
        this.historyList = new ArrayList<>();
        
        // Frequency table of all letters in the english alphabet
        this.frequencyTable = new ArrayList<>();
        
        // Lower case letters
        frequencyTable.add(new FrequencyPair("e", 12.7));
        frequencyTable.add(new FrequencyPair("t", 9.1));
        frequencyTable.add(new FrequencyPair("a", 8.2));
        frequencyTable.add(new FrequencyPair("o", 7.5));
        frequencyTable.add(new FrequencyPair("i", 7.0));
        frequencyTable.add(new FrequencyPair("n", 6.7));
        frequencyTable.add(new FrequencyPair("s", 6.3));
        frequencyTable.add(new FrequencyPair("h", 6.1)); 
        frequencyTable.add(new FrequencyPair("r", 6.0)); 
        frequencyTable.add(new FrequencyPair("d", 4.3)); 
        frequencyTable.add(new FrequencyPair("l", 4.0));
        frequencyTable.add(new FrequencyPair("c", 2.8));
        frequencyTable.add(new FrequencyPair("u", 2.8));
        frequencyTable.add(new FrequencyPair("m", 2.4));
        frequencyTable.add(new FrequencyPair("w", 2.4));
        frequencyTable.add(new FrequencyPair("f", 2.2));
        frequencyTable.add(new FrequencyPair("g", 2.0));
        frequencyTable.add(new FrequencyPair("y", 2.0));
        frequencyTable.add(new FrequencyPair("p", 1.9));
        frequencyTable.add(new FrequencyPair("b", 1.5)); 
        frequencyTable.add(new FrequencyPair("v", 0.98)); 
        frequencyTable.add(new FrequencyPair("k", 0.77));
        frequencyTable.add(new FrequencyPair("j", 0.15));
        frequencyTable.add(new FrequencyPair("x", 0.15)); 
        frequencyTable.add(new FrequencyPair("q", 0.095)); 
        frequencyTable.add(new FrequencyPair("z", 0.074));
        
        // Capital letters
        frequencyTable.add(new FrequencyPair("I", 0.65));
        frequencyTable.add(new FrequencyPair("T", 0.4));
        frequencyTable.add(new FrequencyPair("A", 0.35));
        frequencyTable.add(new FrequencyPair("S", 0.30));
        frequencyTable.add(new FrequencyPair("W", 0.25));
        frequencyTable.add(new FrequencyPair("H", 0.20));
        frequencyTable.add(new FrequencyPair("M", 0.18));
        frequencyTable.add(new FrequencyPair("C", 0.17)); 
        frequencyTable.add(new FrequencyPair("P", 0.15)); 
        frequencyTable.add(new FrequencyPair("B", 0.13)); 
        frequencyTable.add(new FrequencyPair("E", 0.12));
        frequencyTable.add(new FrequencyPair("O", 0.11));
        frequencyTable.add(new FrequencyPair("D", 0.10));
        frequencyTable.add(new FrequencyPair("L", 0.08));
        frequencyTable.add(new FrequencyPair("R", 0.07));
        frequencyTable.add(new FrequencyPair("F", 0.06));
        frequencyTable.add(new FrequencyPair("G", 0.05));
        frequencyTable.add(new FrequencyPair("N", 0.04));
        frequencyTable.add(new FrequencyPair("K", 0.03));
        frequencyTable.add(new FrequencyPair("J", 0.02)); 
        frequencyTable.add(new FrequencyPair("U", 0.01)); 
        frequencyTable.add(new FrequencyPair("V", 0.008));
        frequencyTable.add(new FrequencyPair("Y", 0.007));
        frequencyTable.add(new FrequencyPair("X", 0.002)); 
        frequencyTable.add(new FrequencyPair("Z", 0.001)); 
        frequencyTable.add(new FrequencyPair("Q", 0.001)); 
        
        // Numbers 
        frequencyTable.add(new FrequencyPair("0", 0.001));
        frequencyTable.add(new FrequencyPair("1", 0.001));
        frequencyTable.add(new FrequencyPair("2", 0.001));
        frequencyTable.add(new FrequencyPair("3", 0.001));
        frequencyTable.add(new FrequencyPair("4", 0.001));
        frequencyTable.add(new FrequencyPair("5", 0.001));
        frequencyTable.add(new FrequencyPair("6", 0.001));
        frequencyTable.add(new FrequencyPair("7", 0.001));
        frequencyTable.add(new FrequencyPair("8", 0.001));
        frequencyTable.add(new FrequencyPair("9", 0.001));
        
        // Special characters
        frequencyTable.add(new FrequencyPair(".", 0.001));
        frequencyTable.add(new FrequencyPair(":", 0.001));
        frequencyTable.add(new FrequencyPair("_", 0.001));
        frequencyTable.add(new FrequencyPair("!", 0.001));
        frequencyTable.add(new FrequencyPair("?", 0.001));
        
        // Create regex for non handled characters
        this.nonHandledCharacters = Pattern.compile("[^A-Za-z\\d!.:?_ ]");
        
        // Parse the frequency table and save all characters
        for(FrequencyPair e : this.frequencyTable){
            this.allEncodedCharacters.add(e.character());
        }
    }
    
    // --------- Methods --------- //
    
    /**
     * Initilizes fields related to key generation.
     * 
     * @param params Parameters needed to initiate key generation.
     */
    public void init(@NonNull KeyParameters params){
        this.keyParameters = params;
        this.totalPhonogramsNum = 0;
    }
    
    // --------- History Saving --------- //
    
    /**
     * Adds a history record to {@link #historyList}.
     * Id of the record is determiend based on the number of already existing records in the list.
     * 
     * @param info Stirng wih information that will be stored.
     */
    public void addHistoryRecord(@NonNull String info){
        int id = this.historyList.size();
        HistoryRecord record = new HistoryRecord(id, info);
        
        this.historyList.add(record);
    }
    
    /**
     * Getter for the {@link #historyList}.
     * 
     * @return Returns the history list.
     */
    public List<HistoryRecord> getHistoryList(){
        return this.historyList;
    }
    
    /**
     * Clears the contents of the {@link #historyList}.
     */
    public void clearHistoryList(){
        this.historyList.clear();
    }
    
    /**
     * Adds all key entries into the history list {@link #historyList}.
     * Each letter and its phonograms is a single new record.
     */
    public void appendKeyToHistory(){
        //Map<String, List<String>> key = this.getKey();
        
        this.key.entrySet().stream().map(element -> {
            String letter = element.getKey();
            
            String phongrams = element.getValue().stream().collect(Collectors.joining(" | "));
            return letter + ": " + phongrams;
        }).forEach(this::addHistoryRecord);
    }
    
    // --------- Encryption and decryption --------- //
    
    /**
     * Accepts a message string and encrypts it according to {@link #key}.
     * Trailing and preceding whitespaces are removed if they are present. 
     * Whitespaces are treates as plain text separator and thus are not encoded.
     * Function iterates over all letters from the input string
     * and assignes each one a randomly selected phonogram according to the key.
     * 
     * @param inputString string that is to be encrypted.
     * @return Encrypted string.
     */
    public String encrypt(@NonNull String inputString){        
        // Remove front and trailing spaces and convert multiple spaces to single space only
        inputString = inputString.strip();
        inputString = inputString.replaceAll("\\s{2,}", " "); // Convert multiple whitespace into single only
        
        // Convert the characters from the input string to appropriate phonograms
        // 1. Convert it to Stream<String>
        // 2. Convert each element to appropriate phonogram
        
        String outcome = inputString.chars()
                .mapToObj(e -> Character.toString((char) e))
                .map(this::encryptCharacter)
                .collect(Collectors.joining());
        
        return outcome.trim();
    }
    
    /**
     * Accepts a message string, composed of phonograms and decodes it to english alphabet.
     * Removes leading and trailing whitespaces.
     * Whitespaces are not decoded, they remain unchanged.
     * Function iterates over phongrams, {@link #keyParameters} field phonogramLength specify single phonogram length.
     * Each phonogram is mapped to letter from english alphabet present in the key.
     * 
     * @param inputString encoded string should contain only digits and spaces.
     * @return input string decrypted to english alphabet.
     * @throws UnknownPhonogramException in case an unknwn phonogram has been encountered during decoding.
     * @throws StringIndexOutOfBoundsException in case the decoded words length was different than multiple of {@link #keyParameters} phonogramLength.
     */
    public String decrypt(@NonNull String inputString) throws UnknownPhonogramException, StringIndexOutOfBoundsException{
        String outcome = "";
        
        // Remove front and trailing spaces and convert multiple spaces to single space only
        inputString = inputString.strip();
        inputString = inputString.replaceAll("\\s{2,}", " "); // Convert multiple whitespace into single only
        
        // Split the input on spaces
        String[] words = inputString.split("\\s+");
        String phonogramLengthStr = Integer.toString(this.keyParameters.phonogramLength());
        Pattern phonogramPattern = Pattern.compile("[0-9]{1," + phonogramLengthStr + "}");
        
        for(String word : words){
            // Split the word into phonograms (equal sized strings. size = phonogramLength)
            Matcher matcher = phonogramPattern.matcher(word);
            while(matcher.find()){
                // Read the phonogram
                String phonogram = matcher.group();
                
                // Check if phonogram length is valid
                if(phonogram.length() != this.keyParameters.phonogramLength()){
                    throw new UnknownPhonogramException("Phonogram is too short in word: " + word +" at: " + phonogram);
                }
                
                // Decrypt the phonogram and return
                String decryptedChar = this.decryptCharacter(phonogram);
                outcome += decryptedChar;
            }
            // Separate words with spaces
            outcome += " ";
        }
        return outcome.trim();
    }
    
    /**
     * Accepts a single letter and assigns it a randomly selected phonogram according to the {@link #key}.
     * 
     * @param character english alphabet letter or special character present in {@link #allEncodedCharacters}.
     * @return phonogram assigned to the input letter.
     */
    private String encryptCharacter(String character){
        if(!character.equals(" ")){
            List<String> arr = this.key.get(character);
            int randomIdx = (int)(Math.random() * arr.size());
            return arr.get(randomIdx);
        }
        else{
            return " ";
        }
    }
    
    /**
     * Decrypts a phongram back to english letter.
     * Iterates over all letters in the {@link #key} and checks if the given phonogram
     * is contained in the array of phonograms assigned to that letter.
     * @param codedCharacter Encoded character, which will be decoded according to {@link #key}.
     * @return Decrypted character.
     * @throws UnknownPhonogramException in case the specified codedCharacter was not in {@link #allEncodedCharacters}.
     * @throws StringIndexOutOfBoundsException in case the codedCharacter did not have appropriate length, specified by {@link #keyParameters} phonogramLength.
     */
    private String decryptCharacter(String codedCharacter) throws UnknownPhonogramException, StringIndexOutOfBoundsException{
        // Check if character is in valid form 
        // In each phonograms array check if it contains the codedCharacter
        Optional<String> outcome = this.key.entrySet().stream()
                                       .filter(element -> element.getValue().contains(codedCharacter))
                                       .map(Map.Entry::getKey)
                                       .findFirst();

        if(outcome.isPresent()){
            return outcome.get();
        }
        else{
            throw new UnknownPhonogramException("Unknown character has been specified in the encrypted text: " + codedCharacter);
        }
    }
    
    /**
     * Validates encrypted string, if it contains legal phonogram characters (phonogram must belong to {@link #allPhonograms}.
     * Leading and trailing whitespaces are removed. Spaces that take more than one space are normalised.
     * Splits the given string on whitespaces and iterates over specific words.
     * Extracts phonograms of length phonogramLength ({@link #keyParameters}) and checks if {@link #allPhonograms} contains it.
     * 
     * @param inputString Encoded string that we want to decode.
     * @throws UnknownPhonogramException in case an unknown phonogram has been found in the string.
     * @return true if the string is valid and contains only known phongrams. Otherwise exception is thrown.
     */
    public boolean testEncryptedString(@NonNull String inputString) throws UnknownPhonogramException, StringIndexOutOfBoundsException{
        // Split the string into separate words 
        
        // Remove front spaces and convert multiple spaces to single space only
        inputString = inputString.strip();
        inputString = inputString.replaceAll("\\s{2,}", " "); // Convert multiple whitespace into single only
        List<String> arr = Arrays.asList(inputString.split("[ ]"));
        
        // Iterate over words from provided string, check if symbols in them correspond to those that are availabale in the key
        int cursor = 0;
        for(String word : arr){
            // Try to read character
            while(cursor < word.length()){
                String character = word.substring(cursor, cursor + this.keyParameters.phonogramLength());
                cursor += this.keyParameters.phonogramLength();
                if(this.allPhonograms.contains(character) == false){
                    throw new UnknownPhonogramException("Unknown character has been specified in the encrypted text: " + character);
                }
            }
            cursor = 0; // Reset cursor for a new word
        }
        return true;
    }
    
    // --------- Input String Validation ----------- //
    
    /**
     * Validates the string for encoding process.
     * Removes multiple, leading and trailing whitespaces from the given string and validates if it is good for encoding.
     * 
     * @param inputString Input string which is to be encoded.
     * @return True in case string is good for encoding, False otherwise.
     */
    public boolean checkStringForEncoding(@NonNull String inputString){
        // Test if the input string contains any illegal characters
        // If so, inform the user and come back to menu
        
        // Clear whitespaces from front and back, convert mutiplie whitespace into singular
        inputString = inputString.strip();
        inputString = inputString.replaceAll("\\s{2,}", " ");
        
        Matcher m = this.getNonHandledCharacters().matcher(inputString);
        
        //return !(boolean)(m.find());
        if(m.find()){
            return false;
        }
        return true;
    }
    
    // --------- Key generation --------- //
    
    /**
     * Clears the contents of {@link #key}.
     */
    public void clearKey(){
        this.key.clear();
    }
    
    /**
     * Converts integer number to String of length given by {@link #keyParameters} phonogramLength.
     * If number of digits is lesser than the length of phonogram, leading 0's are added.
     * e.x. if input number equal 45 and phongram length is 3, '045' string is returned.
     * 
     * @param num interger which will be converted to phonogram.
     * @return integer converted to string.
     */
    private String generatePhongram(int num){
        String value = Integer.toString(num);
        String prefix = String.valueOf('0').repeat(this.keyParameters.phonogramLength() - value.length());
        return prefix + value;
    }
    
    /**
     * For each letter from {@link #allEncodedCharacters} an array of encryption charaters is generated.
     * Number of phonograms assigned to each letter depends on the frequency of that letter {@link #frequencyTable}.
     * Phonograms are generated as integer numbers of fixed length.
     */
    public void generateKey(){        
        // Populate the list of numbers
        int phonogramsNumber = (int)Math.pow(10, this.keyParameters.phonogramLength());
        List<Integer> integers = IntStream.range(0, phonogramsNumber)
                .boxed()
                .collect(Collectors.toList());
        
        // Generate phonograms based on frequency of letters
        for(FrequencyPair e : this.frequencyTable){
            String letter = e.character();
            double frequency = e.frequency();
            
            // How many phonograms will be assigned to this letter
            int numPhonograms = this.keyParameters.minPhonograms() + (int)((frequency / 12.7) * (double)(this.keyParameters.maxPhonograms() - this.keyParameters.minPhonograms()));
            
            // Poll numbers from the list, turn them into phonograms and assign to code the letter.
            List<String> letterPhonograms = new ArrayList<>();
            
            IntStream.range(0, numPhonograms) // Generate integers for each phongram for given letter
            .map(i -> (int)(Math.random() * integers.size())) // Get radnom index aomg the generated integers
            .map(integers::remove) // Remove this integer from the pool of possible values phongram may take
            .mapToObj(this::generatePhongram)
            .forEach(phonogram -> {
                letterPhonograms.add(phonogram);
                this.allPhonograms.add(phonogram); // Save the phonogram also in exernal list
            }); // Generate phonogram strings

            this.totalPhonogramsNum += letterPhonograms.size(); // Count how many phonograms are in use.
            this.key.put(letter, letterPhonograms);
        }
    }
    
    /**
     * Assigns each letter from {@link #allEncodedCharacters} a single encryption character.
     * Length of a phonogram is specified by {@link #keyParameters} phonogramLength = 3. 
     */
    public void generateDefaultKey(){
        this.keyParameters = new KeyParameters(3, 1, 1);
        
        // Generate numbers which will be used to encode each letter from the frequency table
        // Each letter is assigned single phonogram
        IntStream.range(0, this.frequencyTable.size())
                .forEach(idx -> {
                    String letter = this.frequencyTable.get(idx).character();
                    
                    // Generate phongram for selected letter and store it in the list of phonograms
                    String phonogram = this.generatePhongram(idx);
                    this.allPhonograms.add(phonogram);
                    
                    // Update the key
                    this.key.put(letter, Arrays.asList(phonogram));
                });
    }
    
    /**
     * Calculates the nuber of phongrams that will be needed to encode the whole alphabet.
     * Iterates over the frequency table {@link #frequencyTable} and calculates number of phongrams that
     * will be generated for each letter. 
     * Sum of these values is returned.
     * 
     * @param suggestedMin minimal number of phonograms per letter.
     * @param suggestedMax maximal number of phongrams per letter.
     * @return total number of phonograms that will be generated for the alphabet.
     * @see isPhonogramsNumSufficient
     */
    private int suggestedNumberOfPhonograms(int suggestedMin, int suggestedMax){
        // Sum all frequencies and check how many phonograms will be generated
        int totalTheoretical = this.frequencyTable.stream()
                                   .mapToInt(p -> suggestedMin + (int)((p.frequency() / 12.7) * (double)(suggestedMax - suggestedMin)))
                                   .sum();

        return totalTheoretical;
    }
    
    /**
     * Checks if provided combination of minimal and maximal numbers of phongrams, assigned to a single letter 
     * as well as the length of a phonogram are valid.
     * Total number of generated phonogram characters is equal 10^N, where N = phonogramLength, from {@link #keyParameters}.
     * It must exceed value returned by {@link #suggestedNumberOfPhonograms}.
     * If so, the combination of parameters is considered valid.
     * 
     * @param suggestedParams Suggested values for the key parameters.
     * @return true in case of valid parameter combination.
     */
    public boolean isPhonogramsNumSufficient(@NonNull KeyParameters suggestedParams){
        
        suggestedParams = new KeyParameters(Math.abs(suggestedParams.phonogramLength()), Math.abs(suggestedParams.minPhonograms()), Math.abs(suggestedParams.maxPhonograms()));
        int suggestedAmount = (int)Math.pow(10, suggestedParams.phonogramLength());
        int totalTheoretical = this.suggestedNumberOfPhonograms(suggestedParams.minPhonograms(), suggestedParams.maxPhonograms());
        return totalTheoretical < suggestedAmount;
    }
}