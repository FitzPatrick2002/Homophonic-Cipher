/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package mvc.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.*;

/**
 * Performs parameterized tests of the Model class.
 * Tests all public methods of the Model class. 
 * List of tested methods:
 * <ul>
 * <li>init</li>
 * <li>encrypt</li>
 * <li>decrypt</li>
 * <li>testEncryptedString</li>
 * <li>clearKey</li>
 * <li>generateKey</li>
 * <li>generateDefaultKey</li>
 * <li>isPhonogramsNumSufficient</li>
 * 
 * 
 * 
 * </ul>
 * Methods have been tested for the correct values, which they expect to receive,
 * the invalid values, borderline cases and for cases when they throw excpetions.
 * @author Milosz Liniewiecki
 */
public class ModelTest {
    
    /**
     * Constructor is empty.
     */
    public ModelTest(){
        // Short comment about life.
    }
    
    // # ----------------- INIT TESTS ----------------- # //
    
    /**
     * Test of init method, of class Model.
     * Tests if the init() can take regular integers as parameters.
     * 
     * @param phonogramLen  Used to init the phonogramLength field of the keyParameters record passed into init().
     * @param minPhonograms Used to init the minPhonograms field of the keyParameters record passed into init().
     * @param maxPhonograms Used to init the maxPhonograms field of the keyParameters record passed into init().
     */
    @ParameterizedTest 
    @CsvSource(value = {"3,3,3"})
    public void testInit(Integer phonogramLen, Integer minPhonograms, Integer maxPhonograms) {
        System.out.println("init");
        Model instance = new Model();
        
        // Assert no exception is thrown for correct input
        assertDoesNotThrow​(() -> {
            KeyParameters params = new KeyParameters(phonogramLen, minPhonograms, maxPhonograms);
            instance.init(params);
        });
    }
    
    /**
     * Test of init method, of class Model.
     * Tests if the method throws NullPointerException when it receives null values.
     * 
     * @param nullValue Should be null.
     */
    @ParameterizedTest 
    @NullSource
    public void testInitThrow(KeyParameters nullValue) {
        System.out.println("init");
        Model instance = new Model();
        
        // Test if passing of null values will casue a null pointer exception
        assertThrows(NullPointerException.class, () -> {
            //KeyParameters params = new KeyParameters(nullValue, nullValue, nullValue);
            instance.init(nullValue);
        }, "init() should throw a NullPointerException when null values are passed as parameters");
    }
    
    // # ----------------- TEST addHistoryRecord ----------------- # //
    
    /**
     * Test of addHistoryRecord method of class Model.
     * When given null it should throw a NullPointerException.
     * 
     * @param nullValue 
     */
    @ParameterizedTest
    @NullSource
    public void testAddHistoryRecordThrow(String nullValue){
        System.out.println("addHistoryRecord() null input");
        Model instance = new Model();
        
        assertThrows(NullPointerException.class, () -> {
            instance.addHistoryRecord(nullValue);
        }, "addHistoryRecord() should throw a NullPointerException when given null input.");
    }
    
    /**
     * Test of addHistoryRecord method of class Model.
     * When given non-null string it should not throw any exception
     * and the number of history records is increased by one.
     * 
     * @param message 
     */
    @ParameterizedTest
    @ValueSource(strings={"Record 0", "Record 1", "Record 2"})
    public void testAddHistoryRecord(String message){
        System.out.println("addHistoryRecord()");
        Model instance = new Model();
        
        int initialRecordsNumber = instance.getHistoryList().size();
        
        assertDoesNotThrow(() -> {
            instance.addHistoryRecord(message);
        }, "addHistoryRecord should not throw an error for normal string input. Input: " + message);
        
        int newRecordsNumber = instance.getHistoryList().size();
        assertEquals(initialRecordsNumber + 1, newRecordsNumber);
    }
    
    // # ----------------- TEST clearHistoryList ----------------- # //
    
    /**
     * Test of clearHistoryList method of class Model.
     * Tests if after using the method the history list will contain 0 records.
     */
    @ParameterizedTest
    @ValueSource(strings={"Any"})
    public void testClearHistoryList(String someInput){
        Model instance = new Model();
        
        // Add some dummy records
        instance.addHistoryRecord(someInput);
        instance.addHistoryRecord(someInput);
        instance.addHistoryRecord(someInput);
        instance.addHistoryRecord(someInput);
        
        // Make sure there is stuff in the list
        int recordsNumber = instance.getHistoryList().size();
        assertTrue(recordsNumber == 4);
        
        // Clear the hisotry and test if the list is empty
        instance.clearHistoryList();
        recordsNumber = instance.getHistoryList().size();
        
        assertEquals(recordsNumber, 0);
    }
    
    // # ----------------- TEST appendKeyToHistory ----------------- # //
    
    /**
     * Test of appendKeyToHistory method of class Model.
     * Tests if the history list will contain the full generated key in correct order.
     */
    @ParameterizedTest
    @ValueSource(strings={"Any"})
    public void testAppendKeyToHistory(String anyInput){
        Model instance = new Model();
        
        instance.clearKey();
        instance.generateDefaultKey();
        instance.appendKeyToHistory();
        
        int i = 0;
        Map<String, List<String>> key = instance.getKey();
        List<HistoryRecord> history = instance.getHistoryList();
        
        System.out.println("Size: " + key.size());
        System.out.println("History size: " + history.size());
        
        for(Map.Entry<String, List<String>> entry : key.entrySet()){
            String letter = entry.getKey();
            List<String> phonograms = entry.getValue();
            
            HistoryRecord record = history.get(i);
            String content = record.message();
            // Content should be in form: 'letter: ph1 | ph2 | ph3 ...'
            
            String expectedContent = phonograms.stream().collect(Collectors.joining(" | "));
            
            assertTrue(content.equals(letter + ": " + expectedContent));
            i++;
        }
    }
    
    // # ----------------- TEST checkStringForEncoding ----------------- # //
    
    /**
     * Test of checkStringForEncoding method of class Model.
     * checkStringForEncoding should throw a NullPointerException in case null
     * is passed as an argument.
     * 
     * @param nullInput Null value.
     */
    @ParameterizedTest
    @NullSource
    void testCheckStringForEncodingNull(String nullInput){
        Model instance = new Model();
        
        assertThrows(NullPointerException.class, () ->{
            instance.checkStringForEncoding(nullInput);
        }, "checkStringForEncoding() should throw a NullPointerException for null input.");
    }
    
    /**
     * Test of checkStringForEncoding method of class Model.
     * checkStringForEncoding should not throw any exception and return true
     * for all valid strings.
     * 
     * @param goodInput String with valid character inside
     */
    @ParameterizedTest
    @ValueSource(strings={"Some really GooD 1npu7.", 
        "1234567890", 
        "    Some space there",
        "and there as well    ",
        "   So     many     of     them    ",
        "Meow"})
    void testCheckStringForEncodingGood(String goodInput){
        Model instance = new Model();
        instance.clearKey();
        instance.generateDefaultKey();
        
        assertTrue(instance.checkStringForEncoding(goodInput));
    }
    
    /**
     * Test of checkStringForEncoding method of class Model.
     * checkStringForEncoding should not throw any exception and return false
     * for all strings which contain illegal characters.
     * 
     * @param goodInput String with valid character inside
     */
    @ParameterizedTest
    @CsvSource(value={"That is not good @", 
        "Nor is $% this (", 
        "And that *)",
        "@", "#", "$", "%", "^", "&", "*", "(", ")", "=", "+"
    })
    void testCheckStringForEncodingBad(String badInput){
        Model instance = new Model();
        instance.clearKey();
        instance.generateDefaultKey();
        
        assertFalse(instance.checkStringForEncoding(badInput));
    }
    
    // # ----------------- TEST encrypt ----------------- # //
    
    /**
     * Test of encrypt method, of class Model.
     * Tests if no excpetion is thrown in case valid input is given and if the outcome corresponds to the expected results.
     * Tests are carried only for the default key.
     * Custom key is randomized so validation of outcomes is a little bit troublesome. 
     * 
     * @param input Input string which will be encrypted.
     * @param expOutput Expected string after encryption. 
     */
    @ParameterizedTest
    @CsvSource(value={
        "a   bc  ,002 019011",  // Valida parameters with spaces
        "   Input     Area    ,026005018012001 028008000002",  // Valida parameters with spaces
        "qwertyuiopasdfghjklzxcvbnm1234567890!.,024014000008001017012004003018002006009015016007022021010025023011020019005013053054055056057058059060061052065062" // All possible valid parameters
    })
    public void testEncrypt(String input, String expOutput) {
        System.out.println("encrypt");
        
        Model instance = new Model();
        
        // Generate the default key
        instance.clearKey();
        instance.generateDefaultKey();
        
        // Test the encoding, it should not throw any exceptions
        assertDoesNotThrow(() -> {
            String result = instance.encrypt(input);
            assertEquals(expOutput, result);
        }, "Exception should not be thrown by encrypt() for valid input: " + input);
    }
    
    /**
     * Test of borderline conditions of the encrypt method, of class Model.
     * Test if when method is given an empty string, it will not throw an exception 
     * and the outcome will be also an empty string.
     * Tests are carried for both default and custom key.
     * 
     * @param input A blank string.
     */
    @ParameterizedTest
    @EmptySource
    public void testEncryptBorder(String input) {
        System.out.println("encrypt");
        
        // Create the model
        Model instance = new Model();

        // Generate the default key
        instance.clearKey();
        instance.generateDefaultKey();

        // Test the encoding, it should not throw any exceptions
        assertDoesNotThrow(() -> {
            String result = instance.encrypt(input);
            assertEquals("", result);
        }, "Exception should not be thrown by encrypt() for empty string input: " + input);
        
        // Generate custom key
        instance.clearKey();
        instance.init(new KeyParameters(4, 4, 5));
        instance.generateKey();
        
        // Test the encoding, it should not throw any exceptions
        assertDoesNotThrow(() -> {
            String result = instance.encrypt(input);
            assertEquals("", result);
        }, "Exception should not be thrown by encrypt() for empty string input: " + input);
    }
    
    /**
     * Test of exception thrown by the encrypt method, of class Model.
     * Tests if method will throw a NullPointerException when null is passed into it.
     * 
     * @param input Input string (in this case null).
     */
    @ParameterizedTest
    @NullSource
    public void testEncryptThrow(String input) {
        System.out.println("encrypt");
        Model instance = new Model();
        
        // Generate the default key
        instance.clearKey();
        instance.generateDefaultKey();
        // Test the encoding, it should throw a nullptr exception
        assertThrows(NullPointerException.class, () -> {
            instance.encrypt(input);
        }, "encrypt() should throw a NullPointerException when null is passed to it.");
        
        // Generate custom key
        instance.clearKey();
        instance.init(new KeyParameters(4, 4, 5));
        instance.generateKey();
        // Test the encoding, it should not throw any exceptions
        assertThrows(NullPointerException.class, () -> {
            instance.encrypt(input);
        }, "encrypt() should throw a NullPointerException when null is passed to it.");
    }

    // # ----------------- TEST decrypt ----------------- # //

    /**
     * Test of decrypt method, of class Model for valid input cases.
     * Tests if decryption produces correct results when default key is in use.
     * Method should not throw any exception in this test.
     * 
     * @param input String encrypted with the default key.
     * @param expResult Expected result of decoding.
     */
    @ParameterizedTest
    @CsvSource(value={
        "002019011,abc", // Normal input
        "002  019011   ,a bc", // Inpute with space
        "026005018012001 028008000002,Input Area", // Some longer input
        "024014000008001017012004003018002006009015016007022021010025023011020019005013053054055056057058059060061052065062,qwertyuiopasdfghjklzxcvbnm1234567890!."
    })
    public void testDecrypt(String input, String expResult) {
        System.out.println("decrypt");
        Model instance = new Model();
        instance.generateDefaultKey();
        
        assertDoesNotThrow(() -> {
            String result = instance.decrypt(input);
            assertEquals(expResult, result);
        }, "decrypt() should not throw any exception for valid input: " + input); 
    }
    
    /**
     * Test of decrypt method for invalid input cases, of class Model.
     * Tests if decrypt method will throw an exception when it is given an invalid encrypted string.
     * Decryption is tested for the default key.
     * The thrown exception should be either of type UnknownPhonogramException or StringIndexOutOfBounds.
     * 
     * @param input String with invalid default encoding. Easier speaking some string of digits without much order.
     */
    @ParameterizedTest
    @ValueSource(strings={"00000100222", "090019011", "0260050180112001", "010094021000", "911002001006"}) // Length not matching
    public void testDecryptThrows(String input) {
        System.out.println("decrypt");
        Model instance = new Model();
        instance.generateDefaultKey();
        
        Exception ex = assertThrows(Exception.class, () -> {
            instance.decrypt(input);
        }, "decrypt() should throw StringIndexOutOfBoundsException or UnknownPhonogramException when it encounters a string of invalid length.");
        
        // Check if thrown exception is of correct type
        String exceptionName = ex.getClass().getSimpleName();
        if(!(exceptionName.equals("StringIndexOutOfBoundsException") || exceptionName.equals("UnknownPhonogramException"))){
            fail("Exception thrown by decrypt() should be of type StringIndexOutOfBoundsException or UnknownPhonogramException when invalid string is given.");
        }
    }

    /**
     * Test of decrypt method for invalid input cases, of class Model.
     * Tests if decrypt method will throw NullPointerException when it is given null value.
     * Decryption is tested for the default key.
     * 
     * @param input String with invalid default encoding. Easier speaking some string of digits without much order.
     */
    @ParameterizedTest
    @NullSource
    public void testDecryptThrowNullptr(String input) {
        System.out.println("decrypt");
        Model instance = new Model();
        instance.generateDefaultKey();
        
        assertThrows(NullPointerException.class, () -> {
            instance.decrypt(input);
        }, "decrypt() should throw NullPointerException for null input.");
    }
    
    /**
     * Test of decrypt method of class Model, when input is an empty string.
     * Tests the decrypt method when input is an empty string.
     * No exception should be thrown and outcome should be also an empty string.
     * 
     * @param input An empty string.
     */
    @ParameterizedTest
    @EmptySource
    public void testDecryptEmpty(String input) {
        System.out.println("decrypt");
        Model instance = new Model();
        instance.generateDefaultKey();
        
        assertDoesNotThrow(() -> {
            String outcome = instance.decrypt(input);
            assertEquals(outcome, ""); 
        }, "decrypt() should not throw errors for empty strings as input");
    }

    
    // # ----------------- TEST TestEncryptedString ----------------- # //
    
    /**
     * Test of testEncryptedString method, of class Model.
     * Tests strings encrypted strings if they are correctly encrypted with the default key.
     * That is if phonograms from the string are contained by the default key.
     * Method should not throw any exceptions for valid encoded strings.
     * 
     * @param input Phonogram encoded with default key.
     * @param expOutcome Expected outcome should be true.
     */
    @ParameterizedTest
    @CsvSource(value = {
        "000001002003004005006007008009010011012013014015016017018019020021022023024025026027028029030031032033034035036037038039040041042043044045046047048049050051052053054055056057058059060061062063064065066,true",
        "   000001   001    000  ,true",
    })
    public void testTestEncryptedString(String input, boolean expOutcome) {
        System.out.println("testEncryptedString");
        Model instance = new Model();
        
        // Generate the key
        instance.clearKey();
        instance.generateDefaultKey();
        
        // Assert there is no throw and also that the validation works
        assertDoesNotThrow(() -> {
            boolean result = instance.testEncryptedString(input);
            assertEquals(result, expOutcome);
        }, "testEncryptedString() throws an exception for correctly encoded string: " + input);
    }
    
    /**
     * Test of testEncryptedString method, of class Model for empty string input.
     * Tests if the testEncryptedString method validates the empty string for decoding. 
     * 
     * @param input Should be an empty string.
     */
    @ParameterizedTest
    @EmptySource
    public void testTestEncryptedStringBorder(String input) {
        System.out.println("testEncryptedString");
        Model instance = new Model();
        
        // Generate the key
        instance.clearKey();
        instance.generateDefaultKey();
        
        // Assert there is no throw and also that the validation works
        assertDoesNotThrow(() -> {
            boolean result = instance.testEncryptedString(input);
            assertEquals(result, true);
        }, "testEncryptedString() throws and exception for empty string input when it should accept it and return true.");
    }
    
    /**
     * Test of testEncryptedString method, of class Model when input is invalid for decryption.
     * testEncryptedString should throw an exception StringIndexOutOfBoundsException when 
     * the length of encoded string is not a multiple of phongram length
     * and UnknownPhonogramException when it contains an unknown phonogram.
     * Tests for default key.
     * 
     * @param input An invalidly encoded string.
     */
    @ParameterizedTest
    @ValueSource(strings={
        "0000450120", // String index out of bounds
        "026 010004021000 01100200100665"    
    })
    public void testTestEncryptedStringExceptionsStringOutOfBounds(String input) {
        System.out.println("testEncryptedString");
        Model instance = new Model();
        
        // Generate the key
        instance.clearKey();
        instance.generateDefaultKey();
        
        assertThrows(StringIndexOutOfBoundsException.class, () -> {
        instance.testEncryptedString(input);
        }, "testEncryptedString() for input of invalid length should throw StringIndexOutOfBoundsException");
    }
    
    /**
     * Test of testEncryptedString method, of class Model when input is invalid for decryption.
     * testEncryptedString should throw an exception StringIndexOutOfBoundsException when 
     * the length of encoded string is not a multiple of phongram length
     * and UnknownPhonogramException when it contains an unknown phonogram.
     * Tests for default key.
     * 
     * @param input An invalidly encoded string.
     */
    @ParameterizedTest
    @ValueSource(strings={
        "000045068", // Unknown phonogram exception
        "026 010094021000 011002001006"
    })
    public void testTestEncryptedStringExceptionsUnknownPhonogram(String input) {
        System.out.println("testEncryptedString");
        Model instance = new Model();
        
        // Generate the key
        instance.clearKey();
        instance.generateDefaultKey();
        
        assertThrows(UnknownPhonogramException.class, () -> {
            instance.testEncryptedString(input);
        }, "testEncryptedString() for input which contains phonogram unknonw in default key should throw UnknownPhonogramException");
    }
    
    /**
     * Test of testEncryptedString method, of class Model when input is null.
     * When testEncryptedString receives null input it should throw NullPointerException.
     * 
     * @param input Should be null.
     */
    @ParameterizedTest
    @NullSource
    public void testTestEncryptedStringThrowsNull(String input) {
        System.out.println("testEncryptedString");
        Model instance = new Model();
        
        // Generate the key
        instance.clearKey();
        instance.generateDefaultKey();
        
        // Assert method throws an exception when invalid input is given
        assertThrows(NullPointerException.class, () -> {
            try{
                instance.testEncryptedString(input);
            }
            catch (UnknownPhonogramException ex){
                fail("testEncryptedString() should not throw UnknownPhonogramException when input is null.");
            }
        }, "testEncryptedString() should throw NullPointerException when input is null.");
    }

    // # ----------------- TEST testClearKey ----------------- # //
    
    /**
     * Test of clearKey method, of class Model.
     * Tests if the size of phonograms table is zero after clearing the key.
     * 
     * @param expResult Expected size of key map after clearing should be 0.
     */
    @ParameterizedTest
    @ValueSource(ints={0})
    public void testClearKey(int expResult) {
        System.out.println("clearKey");
        Model instance = new Model();
        
        instance.clearKey();
        assertEquals(expResult, instance.getKey().size());
    }
    
    // # ----------------- TEST generateKey & generateDefaultKey ----------------- # //

    /**
     * Test of generateKey method, of class Model for valid inputs.
     * Use of generateKey should be preceded by the use of isPhonogramsNumSufficient and check 
     * if min is not greater than max number of phonograms.
     * Tests if all letters from the frequency table have a sufficient number of phonograms assigned.
     * 
     * @param phonLen Number of digits in a sinlge phonogram.
     * @param minPhon Minimal number of phonograms per single letter.
     * @param maxPhon Maximal number of phonograms per single letter.
     */
    @ParameterizedTest
    @CsvSource(value={
        "4,4,5",
        "3,2,5",
        "4,7,11",
        "3,2,20",
        "4,1,5",
    })
    public void testGenerateKey(int phonLen, int minPhon, int maxPhon) {
        System.out.println("generateKey");
        Model instance = new Model();
        
        // Setup environment for new key generation
        instance.clearKey();
        KeyParameters params = new KeyParameters(phonLen, minPhon, maxPhon);
        instance.init(params);
        
        // Generate the key
        instance.generateKey();
        
        // Test the size
        assertEquals(instance.getAllEncodedCharacters().size(), instance.getKey().size());
        
        // Test if each entry is okay (present in the encoded characters list list
        for(var e : instance.getKey().entrySet()){
            // Test if assigned number of phonograms is within min and max bounds
            List<String> phonograms = e.getValue();
            assertTrue(phonograms.size() >= minPhon && phonograms.size() <= maxPhon, 
                    "Phonogram number is out of bounds for encoded character.");
            
            // Test if length of phonograms is the same as specified
            for (String p : phonograms){
                assertTrue(p.length() == phonLen, "Phonogram length incorrect");
            }
            
            assertEquals(true, instance.getAllEncodedCharacters().contains(e.getKey()));
        }
    }

    /**
     * Test of generateDefaultKey method, of class Model.
     * Tests if the default key is correctly generated by the generateDefaultKey() method.
     * 
     * @param input All characters that should be encoded by the default key.
     * @param expOutcome Input encoded with the use of default key.
     */
    @ParameterizedTest
    @CsvSource(value={
        "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM!.,024014000008001017012004003018002006009015016007022021010025023011020019005013053054055056057058059060061052051030036040027048046026037034028029038041042031045044039050049033047035043032065062"
    })
    public void testGenerateDefaultKey(String input, String expOutcome) {
        System.out.println("generateDefaultKey");
        Model instance = new Model();
        instance.generateDefaultKey();
        
        String outcome = instance.encrypt(input);
        assertEquals(expOutcome, outcome);
    }

    // # ----------------- TEST isPhonogramsNumberSufficient ----------------- # //
    
    /**
     * Test of isPhonogramsNumSufficient method, of class Model for non null inputs.
     * Tests if isPhonogramsNumSufficient() correctly estimates if given combination of phonogram length,
     * minimal and maximal number of phonograms per character is valid.
     * 
     * @param phonLen Length of a single phonogram.
     * @param minPhon Minimal number of phonograms assigend to a character.
     * @param maxPhon Maximal number of phonograms assigned to a character.
     * @param expOutcome Specifies if we expect given combination of parameters to be valid or not.
     */
    @ParameterizedTest
    @CsvSource(value={
        "3,3,3,true",      // Min and max number of phongrams is lesser than 10^phonLen
        "3,7,20,true",     // Min and max number of phongrams is greater than 10^phonLen
        "-3,7,-20,true",   // Negaitve value should be made absolute. 
        "3,-7,20,true",    // Negaitve value should be made absolute. 
        "3,200,300,false"  // Min and Max number of phonograms is lower than 10^phonLen
    })
    public void testIsPhonogramsNumSufficient(int phonLen, int minPhon, int maxPhon, boolean expOutcome) {
        System.out.println("isPhonogramsNumSufficient");
        Model instance = new Model();
        
        // Test a valid combination of parameters
        boolean result = instance.isPhonogramsNumSufficient(new KeyParameters(phonLen, minPhon, maxPhon));
        assertEquals(expOutcome, result);
    }

    /**
     * Test of isPhonogramsNumSufficient method, of class Model for null input.
     * NullPointerException should be thrown when null is passed to isPhonogramsNumSufficient.
     * 
     * @param value Should be null.
     */
    @ParameterizedTest
    @NullSource
    public void testIsPhonogramsNumSufficientThdrows(KeyParameters value) {
        System.out.println("isPhonogramsNumSufficient");
        Model instance = new Model();
        
        // Test if nullptr exception is thrown if we pass null
        assertThrows(NullPointerException.class, () -> {
            instance.isPhonogramsNumSufficient(value);
        }, "isPhonogramsNumSufficient should throw a null pointer exception when null is passed to it.");
    }

}
