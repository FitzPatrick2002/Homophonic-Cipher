/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mvc.model.Model;
import mvc.view.View;
import java.util.InputMismatchException;
import mvc.model.UnknownPhonogramException;

/**
 * The controller object in the MVC pattern.
 * Mediates in communication between View and Model, creates and controls their instances.
 * Decides whether the full version of the app is launched or if only singular output is given.
 * In case user passes command line characters and a valid string for encryption / decryption, 
 * output is printed and app is closed.
 * If user does not specify the input string or the input string is invalid, full version of the app is lauched
 * and user can choose from menu options.
 * 
 * Available command line options:
 * <ul>
 * <li>-l specifies length of a single phongram. Takes numeric input.</li>
 * <li>-h specifies maximal number of phonograms assinged to a letter. Takes numeric input. </li>
 * <li>-m specifies minimal number of phonograms assigned to a letter. Takes numeric input. </li>
 * <li>-e specifies string for encryption. Takes text input e.g. -e "Some text." </li>
 * <li>-d specifies string for decryption. Takes text input e.g. -d "042013001 052".
 * If it is used, default key is used.</li>
 * <li>-c tells the program to use the default key. Takes no arguments</li>
 * <li>-k prints the used key. Takes no arguments</li>
 * </ul>
 * If user does not specify some of the parameters -l, -h, -m or their combination is invalid, they will be prompted to enter them again.
 * Use of -d does not require user to give -l, -h, -m since default key will be used.
 * 
 * @author Milosz Liniewiecki
 * @version 1.0
 */
public class Controller {
    /**
     * The View class instance (MVC pattern). 
     * Handles all I/O opertions.
     */
    private View view;
    
    /**
     * The Model class instance (MVC pattern).
     * Handles the logic, that is homophonic key generation, string validation,
     * encryption and decryption of strings. 
     */
    private Model model;
    
    /**
     * Default constructor of the Controller class.
     */
    public Controller(){
        // A short comment about life
    }
    
    /**
     * Main function, responsible for running the whole application.
     * Parses the allCmdArgs string and extracts parameters and their attributes.
     * By parameters we understand words like: '-d', followed by the argument (number or text enclosed in quotation marks.
     * Validates the user specified arguments of -l, -m, -h.
     * If some of them are not provided or their combination is invalid, user is prompted to enter them again.
     * Depending on the command line input, application is closed after performing the desired operation
     * or main menu is displayed.
     * @param allCmdArgs All command line arguments concatenated together into a single string.
     */
    public void runApp(String allCmdArgs){
        // Parameters needed to create an isntace of the Model class
        int phonogramLength = -1;
        int minNumPhonograms = -1;
        int maxNumPhonograms = -1;
        
        // Decide if the user specified them in the console commands
        // If not, user will be prompted to specify them later
        boolean phongramLengthSet = false;
        boolean minNumPhonogramsSet = false;
        boolean maxNumPhonogramsSet = false;
        
        boolean showCipher = false;
        boolean encryptSingle = false;
        boolean decryptSingle = false;
        
        boolean useDefaultKey = false;
        
        String inputString = "";
        
        // Regex parameters
        Pattern p = Pattern.compile("(-[lhm]\\s+\\d+)|(-k)|(-[ed]\\s+[a-zA-z0-9:_!?\\s]+)|(-c)");
        Matcher m = p.matcher(allCmdArgs);
        
        // Create the view
        this.view = new View();
        
        // Read the arguments and assign appropriate values to parameters needed for Model initilization
        while(m.find()){
            String argument = m.group();
            
            String[] values = argument.split("\\s+", 2);
            
            // In case of an error, catch it and let the user choose values of the params
            try{
            // Decide which argument to modify
            switch(values[0].charAt(1)){
                case 'l':
                    phonogramLength = Integer.parseInt(values[1]);
                    phongramLengthSet = true;
                break;
                case 'm':
                    minNumPhonograms = Integer.parseInt(values[1]);
                    minNumPhonogramsSet = true;
                break;
                case 'h':
                    maxNumPhonograms = Integer.parseInt(values[1]);
                    maxNumPhonogramsSet = true;
                break;
                case 'e':
                    encryptSingle = true;
                    inputString = values[1];
                break;
                case 'd':
                    decryptSingle = true;
                    useDefaultKey = true;
                    inputString = values[1];
                break;
                case 'k':
                    showCipher = true;
                break;
                case 'c':
                    useDefaultKey = true;
                break;
                default:
                    view.showMessage("Unknown argument: " + values[0] + " " + values[1]);
                    view.showMessage("Omitting...");
                break;
            } 
            }
            catch (NumberFormatException e){
                this.view.showMessage("Exception occured when reading cmd parameters");
                this.view.showMessage("Probably " + values[0] + " did not get integer parameter");
                this.view.showMessage(e.getMessage());
            }
            catch (ArrayIndexOutOfBoundsException e){
                this.view.showMessage("Exception occured when reading cmd parameters");
                this.view.showMessage("One of the parameters got too many or too few arguemnts");
                this.view.showMessage(e.getMessage());
            }
        }
        
        // Create the model
        this.model = new Model();
        
        // If some arguments weren't specified in the command line, ask for them now
        // Basically call function which would decide for which arguemtn to prompt the user. 
        
        boolean badParamsCombination = false;
        
        // If user used -d -> to decode some string, skip this step as the default key will be used instead
        // If -d is used, set default values for the homophony (phonogram length, min and max num of phonograms)
        if(useDefaultKey == false){            
            do{
                // Handle the single phonogram length
                while(phongramLengthSet == false || phonogramLength < 3 || phonogramLength > 5){
                    view.showMessage("Give length of a single phonogram [3 : 5]");
                    phonogramLength = view.promptForInteger();
                    phongramLengthSet = true;
                }

                // Handle the minimal number of phonograms
                while(minNumPhonogramsSet == false || minNumPhonograms < 1 || minNumPhonograms > 30){
                    view.showMessage("Give minimal number of phonograms per character [1 : 30]");
                    minNumPhonograms = view.promptForInteger();
                    minNumPhonogramsSet = true;
                }

                while(maxNumPhonogramsSet == false || maxNumPhonograms < 2 || maxNumPhonograms > 50){
                    view.showMessage("Give maximal number of phonograms per character [2 : 50]");
                    maxNumPhonograms = view.promptForInteger();
                    maxNumPhonogramsSet = true;
                }

                // Check if by generating phonograms of given length, there will be enough of them
                // to cover the entire alphabet with speciifed upper and lower bound of number
                // of phonograms per character
                badParamsCombination = !this.model.isPhonogramsNumSufficient(minNumPhonograms, maxNumPhonograms, phonogramLength);
                if(badParamsCombination == true){
                    // Message user about the error
                    this.view.showMessage("Parameters combination is inappropriate.");
                    this.view.showMessage("There can be: " + Integer.toString((int)(Math.pow(10, phonogramLength))) + " characters generated when phongram length is: " + Integer.toString(phonogramLength));
                    this.view.showMessage("The min and max values generate more phonograms than that and the whole alphabet cannot be covered");
                    this.view.showMessage("Please modify the parameters");

                    // Reset the parameters
                    phongramLengthSet = false;
                    minNumPhonogramsSet = false;
                    maxNumPhonogramsSet = false;
                }
            } while(badParamsCombination);
        }
        else{
            phonogramLength = 3;
            minNumPhonograms = 1;
            maxNumPhonograms = 1;
        }

        // Swap the upper and lower bound if lower is somehow bigger than the upper
        if(minNumPhonograms > maxNumPhonograms){
            int temp = minNumPhonograms;
            minNumPhonograms = maxNumPhonograms;
            maxNumPhonograms = temp;  
            view.showMessage("Swapping min and max number of phonograms, so that min < max");
        }
        
        // Initilize the Model class. 
        // If string for decryption is given, then only the default alphabet is used
        this.model.init(phonogramLength, maxNumPhonograms, minNumPhonograms, useDefaultKey);
        
        this.view.showMessage("Total num of phonogrmas: ");
        this.view.showMessage(Integer.toString(this.model.getTotalPhonogramsNum()));
        
        // Print the operation status
        this.view.showMessage("Phonogram length: " + Integer.toString(phonogramLength));
        this.view.showMessage("Min number of phonograms: " + Integer.toString(minNumPhonograms));
        this.view.showMessage("Max number of phonograms: " + Integer.toString(maxNumPhonograms));
        
        // If user gave such command, initially print the key.
        if(showCipher == true){
            this.view.showMessage("Key: ");
            this.view.showKey(this.model.requestKey());
            showCipher = false;
        }
        
        boolean runFullApp = true;
        
        // If an input string was provided in the cmd, run app once only (if it was valid)
        // In case execution failed due to some error, full version of the app will be run
        if(encryptSingle == true || decryptSingle == true){
            runFullApp = !runOnce(inputString, encryptSingle, decryptSingle);
        }
        
        if(runFullApp){
            runRepeatedly();
        }
    }
    
    /**
     * Validates the user input and runs the program.
     * If input string is valid, performs encoding / decoding of the string.
     * If string was not valid, returns false. 
     * encryptSingle and decryptSingle should have opposite logical values.
     * 
     * @param inputString input string which will be encoded / decoded.
     * @param encryptSingle if set to true, inputString will be encrypted.
     * @param decryptSingle if set to true, inputString will be decrypted.
     * @return true if the execution was successfull, false if string was invalid 
     * or combination of encryptSingle and decryptSingle was invalid.
     */
    private boolean runOnce(String inputString, boolean encryptSingle, boolean decryptSingle){
        if(encryptSingle == decryptSingle){
            return false;
        }
        if(encryptSingle == true){
            if(this.checkStringForEncoding(inputString) == false){
                view.showMessage("Your cmd input string was not okay, running full version of the program");
            }
            else{
                return true;
            }
        }
        else if(decryptSingle == true){
            if(this.checkStringForDecoding(inputString) == false){
                view.showMessage("Your cmd input string was not okay, running full version of the program");
            }
            else{
                return true;
            }
        }
        return false;
    }
    
    /**
     * Runs program repeatedly and allows user to use the main menu.
     * Main menu options consist of:
     * <ul>
     * <li>1. Encrypt - prompts user for input string which will be encrypted.</li>
     * <li>2. Decrypt - prompts user for string which will be decrypted.</li>
     * <li>3. Exit - ends program operation.</li>
     * <li>4. Show key - Shows the currently used key.</li>
     * </ul>
     * To choose an option user is prompted for integer input.
     * If such input is not given, user can correct their mistake.
     */
    private void runRepeatedly(){
        // In the main loop user can choose what to do during the execution of the program
        boolean runApp = true;
        String inputString;
        while(runApp) {
            this.view.showOptions();
            int option = this.view.promptForInteger();
            //String outcome;
            
            switch(option){
                case 1:
                    inputString = this.view.promptForInput();
                    // Test if the input string contains any illegal characters
                    // If so, inform the user and come back to menu
                    this.checkStringForEncoding(inputString);
                break;
                case 2:
                    inputString = this.view.promptForInput();
                    // Test if the input string contains any illegal phonograms
                    // If so, inform the user and come back to menu
                    this.checkStringForDecoding(inputString);
                break;
                case 3:
                    runApp = false;
                break;
                case 4: 
                    this.view.showMessage("Key: ");
                    this.view.showKey(model.requestKey());
                break;
                default:
                    this.view.showMessage("Unknown option: " + Integer.toString(option));
                break;
            }
        }
    }
    
    /**
     * Validates the input string if it contains only legal characters for encoding. 
     * Removes trailing and leading whitespaces and normalises multiple whitespaces.
     * Validates the string using Pattern specified by the {@link #mvc.model.Model#getNonHandledCharacters()}.
     * If input string is valid, it is encrypted and printed.
     * If the string is invalid, message is printed and function terminates, returning false.
     * 
     * @param inputString string undergoing validation and encryption process.
     * @return true in case execution succeeded (valid string has been succesfully encrypted).
     * False otherwise. 
     */
    private boolean checkStringForEncoding(String inputString){
        // Test if the input string contains any illegal characters
        // If so, inform the user and come back to menu
        
        // Clear whitespaces from front and back, convert mutiplie whitespace into singular
        inputString = inputString.strip();
        inputString = inputString.replaceAll("\\s{2,}", " ");
        
        Matcher m = this.model.getNonHandledCharacters().matcher(inputString);
        if(m.find()){
            this.view.showMessage("String: " + inputString + " contains not allowed characters");
            this.view.showMessage("Availabale characters set: ");
            String temp = "";
            for(String s : this.model.getAllEncodedCharacters()){
                temp += s;
            }
            this.view.showMessage(temp);
            return false;
        }
        else{
            String outcome = this.model.encrypt(inputString);
            this.view.showMessage("Encrypted string: " + outcome);
            return true;
        }
    }
    
    /**
     * Validates the input string according to rules specified by the {@link #model}.
     * Leading and trailing whitespaces are removed, multiple whitespaces are normalized.
     * String is split to separate words on whitespaces.
     * String is considered invalid if it contains words, whose length is not a multiple of 
     * single phonogram length {@link #model} and when unknown phonogram is encountered in word.
     * If string is valid it is decrypted according to key specified in {@link #mode} and printed. 
     * If string is considered invalid, message is printed and function terminates returning false.
     * 
     * @param inputString string composed of phongrams and spaces that will be validated and decoded.
     * @return outcome of function execution. If function suceeded at decoding the string, true is returned
     * and false otherwise. 
     */
    private boolean checkStringForDecoding(String inputString){
        boolean testOk;
        
        try{
            testOk = this.model.testEncryptedString(inputString);
        }
        catch(UnknownPhonogramException e){
            this.view.showMessage(e.getMessage());
            testOk = false;
        }
        catch(StringIndexOutOfBoundsException e){
            testOk = false;
        }
        if(testOk == false){
            this.view.showMessage("Encrypted string contains characters that are not supported by current key");
            this.view.showKey(this.model.requestKey());
            return false;
        }
        else{
            String outcome = this.model.decrypt(inputString);
            this.view.showMessage("Decrypted string: " + outcome);
            return true;
        }
    }
    
}
