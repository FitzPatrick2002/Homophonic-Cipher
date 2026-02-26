/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.view;

import java.util.Scanner;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.InputMismatchException;

// TO DO:
// Create javadoc documentation 

/**
 * Takes user input and handles displaying text in console.
 * Prints strings to standard io and reads strings from standard io.
 * @author Milosz Liniewiecki
 * @version 1.0
 */
public class View {
    
    /**
     * Scanner used for reading from standard input.
     */
    final private Scanner scanner;
    
    /**
     * Constructs the View and initilizes the internal {@link #scanner}
     * 
     * to read from standard input.
     */
    public View(){
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Prints a given string message followed by a new line character to standard output.
     * 
     * @param message String written to the standard output.
     */
    public void showMessage(String message){
        System.out.println(message);
    }
    
    /**
     * Prints the homophoninc key table.
     * After each character, the set of its encryption characters is printed
     * 
     * @param tab The homophonic key table, mapping String base characters to their potential encrypted forms.
     */
    public void showKey(Map<String, String[]> tab){
        for(Map.Entry<String, String[]> e : tab.entrySet()){
            String key = e.getKey();
            String[] encryption = e.getValue();
            
            System.out.print(key);
            for(String s : encryption){
                System.out.print(" | " + s);
            }
            System.out.println("");
        }
    }
    
    /**
     * Displays options availabale to the user in the main menu.
     * Currently displays 1. Encryption 2. Decryption 3. Exit 4. Show Key
     */
    public void showOptions(){
        System.out.println("1. Encryption");
        System.out.println("2. Decryption");
        System.out.println("3. Exit");
        System.out.println("4. Show Key");
    }
    
    /**
     * Prompts user for text input until non-blank text is given.
     * Reads text input from standard input. 
     * If it contains trailing and leading whitespaces. 
     * All other whitespaces that are doubled, tripled, etc. are changed into single whitespaces.
     * If user inputs a blank string, they will be prompted for input again.
     * 
     * @return Cleaned, non-blank text entered by the user.
     */
    public String promptForInput(){
        String userInput;
        do{
            System.out.print("Input: ");

            // Prompt user for input and consume it 
            userInput = this.scanner.nextLine(); 
        } while (userInput.isBlank());
        
        // Remove front spaces and convert multiple spaces to single space only
        userInput = userInput.strip();
        userInput = userInput.replaceAll("\\s{2,}", " "); // Convert multiple whitespace into single only
        
        return userInput;
    }
    
    /**
     * Prompts user for valid integer input until it's provided.
     * Firstly input text is obtained (using {@link #promptForInput() }), which is then parsed for integer input.
     * If the text is invalid (canno't be parsed for integer input), error message is displayed
     * and user is prompted again until valid integer input is given.
     * 
     * @return Integer enterd by the user.
     */
    public int promptForInteger(){
        // Try to get integer input from the user.
        while(true){
            try{
                String inputString = this.promptForInput();
                return Integer.parseInt(inputString);
            }
            catch (NumberFormatException  e){
                this.showMessage("Input must be integer");
            }
        }
    }
}
