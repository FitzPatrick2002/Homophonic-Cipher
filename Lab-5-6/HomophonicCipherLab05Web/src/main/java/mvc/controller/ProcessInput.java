/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package mvc.controller;

import jakarta.persistence.PersistenceException;
import mvc.model.Model;
import mvc.model.UnknownPhonogramException;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mvc.model.KeyParameters;

import java.util.HashMap;
import lombok.NonNull;

import mvc.model.DBManager;
import mvc.model.TableInput;
import mvc.model.TableOutput;

/**
 * Servlet responsible for processing user input (encoding / decoding of strings).
 * Accepts POST and GET requests from the main form. 
 * In case string not suitable for encoding / decoding is given, status 400 response is returned
 * with explanation what went wrong.
 * Servlet uses cookies to store statistical data about like number of encodings / decodings and errors.
 * 
 * @author Milosz Liniewiecki
 * @version 1.0
 */
@WebServlet(name = "ProcessInput", urlPatterns = {"/ProcessInput"})
public class ProcessInput extends HttpServlet {
    
    /**
     * Reference to the Model instance, which is created in the ContextListener.
     */
    private Model model;
    
    /**
     * Special message generated in case there was problem with persisting data in the DB.
     */
    private String dbErrorMessage;
    
    /**
     * Database Manager mediates in communication between servlets and the database.
     */
    private DBManager dbManager;

    /**
     * Stores values of statistics cookies so that they can be use d
     * without iterating over the list of cookies.
     * Stored values are: number of encodings, number of decodings, number of errors.
     * Names of keys in the map correspond to the names of cookies.
     */
    private HashMap<String, Integer> stats; 
    
    /**
     * ProcessInput servlet constructor is empty.
     */
    public ProcessInput(){
        // Short comment about life
    }
    
    /**
     * Initiates the servlet. 
     * Gets the reference to the {@link #model} from the servlet context.
     * and creates entries in the {@link #stats} map.
     * 
     * @throws ServletException In case method failed.
     */
    @Override 
    public void init() throws ServletException{
        super.init();

        // Get the reference to the model 
        this.model = (Model)this.getServletContext().getAttribute("model");
        this.stats = new HashMap<>();
        
        this.stats.put("error", 0);
        this.stats.put("encode", 0);
        this.stats.put("decode", 0);
        
        // Init the DB manager
        dbManager = (DBManager)this.getServletContext().getAttribute("dbManager");
    }
    
    /**
     * Increments one of the statistics counters and adds the cookie to the response.
     * 
     * @see #stats
     * @param cookieName Name of the cookie must be the same as key in the {@link #stats} map.
     * @param response The http response to which cookie will be added.
     * @param request The http request from which cookie will be taken.
     */
    private void updateStatisticCookie(@NonNull String cookieName, @NonNull HttpServletRequest request, @NonNull HttpServletResponse response){
        int currentValue = 0;
        
        Cookie[] cookies = request.getCookies();

        // Check the cookies before taking any action
        if(cookies != null){
            for(Cookie cookie : cookies){
                if (cookie.getName().equals(cookieName)){
                    System.out.println("Cookie [" + cookie.getName() + "] = " + cookie.getValue());
                    try{
                        currentValue = Integer.parseInt(cookie.getValue());
                    }
                    catch (NumberFormatException e){
                        currentValue = 0;
                    }
                }
            }
        }
        
        int newValue = currentValue + 1;
        this.stats.put(cookieName, newValue);
        
        Cookie cookie = new Cookie(cookieName, Integer.toString(currentValue + 1));
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);
    }
    
    /**
     * Returns value of a specified cookie.
     * If cookie does nto exist (yet) "0" is returned.
     * 
     * @param name Name of the cookie.
     * @return Value of the cookie or "0" if cookie does not exist.
     */
    private String getCookieValue(@NonNull String name, @NonNull HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for(Cookie cookie : cookies){
                if (cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        
        // If cookie hasn't been found just assume it hasn't been yet created.
        return "0";
    }
    
    /**
     * Persists information in the DB.
     * Specific kind of input is linked with some sort of output.
     * 
     * 
     * @param input Input action like string encoding or cipher generation
     * @param output Output generated by the action (might be an error, some change in the system or confiramtion of succefull operation).
     */
    private void storeInDB(@NonNull String input, @NonNull String output){
        // Store the user action in the DB
        try{
            TableInput persistedInput = new TableInput();
            persistedInput.setInputValue(input);

            TableOutput persistedOutput = new TableOutput();
            persistedOutput.setOutputValue(output);

            // Persist the input and output in the DB
            persistedOutput.setTableInput(persistedInput);
            this.dbManager.persistObject(persistedInput);
            this.dbManager.persistObject(persistedOutput);
        }
        catch (PersistenceException e){
            this.model.addHistoryRecord("Could not persist data in the DB. Input: " + input + ", output: " + output);
            dbErrorMessage += "DB error: " + e.getMessage() + "\n";
        }
    }
    
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * In case appropriate options are selected, generates a new key or uses the old one.
     * When errors during ecnryption / decryption occur, error status response is sent back.
     * 
     * @param request servlet request.
     * @param response servlet response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.setContentType("text/html; charset=ISO-8859-2"); 

        // Read the mode (encryption / decryption)
        String useDefaultKeyString = request.getParameter("use-default-cipher");
        String preserveKeyString = request.getParameter("preserve-key");
        
        //boolean useDefaultKey = true;

        // Check if preserve-key checkbox is checked if so, don't cahnge the key
        // If none key is generated, then generate the default one
        if(preserveKeyString == null){
            // If the use-default-key checkbox is not checked, clear the key and generate new one
            // If it is chekced then clear the key and generate a the default one
            if(useDefaultKeyString == null){
                //useDefaultKey = true;
                model.clearKey();

                this.model.addHistoryRecord("Custom key is being generated");

                // Prepare the parameters
                Integer phonogramLength = Integer.valueOf(request.getParameter("phonograms-length"));
                Integer minPhonograms = Integer.valueOf(request.getParameter("min-phonograms-num"));
                Integer maxPhonograms = Integer.valueOf(request.getParameter("max-phonograms-num"));
                
                // Persist the action in the DB
                this.storeInDB("New key generation", "Parameters: " + phonogramLength + " | " + minPhonograms + " | " + maxPhonograms);
                
                /*
                if(phonogramLength == null){
                    response.sendError(response.SC_BAD_REQUEST, "Error 11: Information about the phongram length for custom key is missing.");
                    this.storeInDB("New key generation", "Error 11: Information about the phongram length for custom key is missing.");
                    return;
                }
                if(minPhonograms == null){
                    response.sendError(response.SC_BAD_REQUEST, "Error 12: Information about the minimal number of phongorams for cusotm key is missing.");
                    this.storeInDB("New key generation", "Error 12: Information about the minimal number of phongorams for cusotm key is missing.");
                    return;
                }
                if(maxPhonograms == null){
                    response.sendError(response.SC_BAD_REQUEST, "Error 13: Information about maximal number of phonograms for custom key is missing.");
                    this.storeInDB("New key generation", "Error 13: Information about maximal number of phonograms for custom key is missing.");
                    return;
                }
                */

                KeyParameters keyParams = new KeyParameters(phonogramLength, minPhonograms, maxPhonograms);

                // Check if the combination of parameters is okay
                if(this.model.isPhonogramsNumSufficient(keyParams) == false){
                    this.model.addHistoryRecord("Custom key parameters were ill formulated, no key generated. Params: " + keyParams.toString());
                    response.sendError(response.SC_BAD_REQUEST, "Error 5: Phongram length, min and max num of phonograms are ill formulate and do not cover all cases. Params:" + keyParams.toString());
                    this.storeInDB("New key generation", "Error 5: Phongram length, min and max num of phonograms are ill formulate and do not cover all cases. Params:" + keyParams.toString());
                    return;
                }

                // If the min > max, swap values
                if(keyParams.maxPhonograms() < keyParams.minPhonograms()){
                    // Save the info to the history
                    this.model.addHistoryRecord("Warning: Max < Min number of phonograms.\n Values swapped.");
                    this.storeInDB("New key generation", "Warning: Max < Min number of phonograms.\n Values swapped.");
                    
                    // Swapping the values
                    int temp = keyParams.maxPhonograms();
                    keyParams = new KeyParameters(keyParams.phonogramLength(), keyParams.minPhonograms(), keyParams.minPhonograms());
                    keyParams = new KeyParameters(keyParams.phonogramLength(), temp, keyParams.maxPhonograms());
                }

                // Generate the custom key
                this.model.init(keyParams);
                this.model.generateKey();

                // Generate the custom key reference table
            }
            else{
                this.model.addHistoryRecord("Default key is selected.");
                this.storeInDB("New key generation", "Default key is selected");

                // Generate the deafult key
                this.model.clearKey();
                this.model.generateDefaultKey();
            }

            // Append the newly generated key to the history
            this.model.appendKeyToHistory();
        }
        else{
            // preserve-key is checked
            // Check if any key is present if so just leave, if no generate default
            if(this.model.getKey().isEmpty()){
                this.model.clearKey();
                this.model.generateDefaultKey();
            }
        }

        // Get the mode: Encrypt / Decrypt
        
        String mode = request.getParameter("mode-input");
        String userInput = request.getParameter("input-area");
        
        // If mode is missing raise an error beacuse it is not known what is supposed to be done
        if(mode == null){
            response.sendError(response.SC_BAD_REQUEST, "Error 7: mode is missing");
            this.storeInDB("Mode Selection", "Error 7: mode is missing");
            return;
        }
        
        // If there is no input well, there is no input
        if (userInput == null){
            response.sendError(response.SC_BAD_REQUEST, "Error 7: user input is missing");
            this.storeInDB("User Input", "Error 7: user input is missing");
            return;
        }

        String outputString; // Contains the message. It can be encoded / decoded string or an error code
        String messageType; // Informs user about the type of the message which will be displayed in the <textarea>

        switch(mode){
            case "Decrypt":
                try{
                    // Test the stirng if it does not contain any illegal phongrams
                    this.model.addHistoryRecord("Testing the input string designated for decryption");
                    this.model.testEncryptedString(userInput);
                    this.storeInDB("Decryption,", "Testing the input string designated for decryption");

                    // Attempt to decrypt the string
                    this.model.addHistoryRecord("Attempting to decrypt: " + userInput);
                    this.storeInDB("Decryption", "Attempting to decrypt: " + userInput);
                    
                    outputString = model.decrypt(userInput);
                    messageType = "Decrypted String: ";
                    this.model.addHistoryRecord("Decrypted string: " + outputString);
                    this.storeInDB("Decryption", "Decrypted String: " + outputString);

                    // If decryption succeeded, increment the decryption cookie
                    this.updateStatisticCookie("decode", request, response);
                }
                catch (UnknownPhonogramException e){
                    this.model.addHistoryRecord("Invalid phonogram for encryption.\n" + e.getMessage());
                    this.storeInDB("Decryption Error", "Invalid phonogram for encryption.\n" + e.getMessage());

                    this.updateStatisticCookie("error", request, response);
                    response.sendError(response.SC_BAD_REQUEST, "Error 1: Invalid phonogram for decryption");
                    return;
                    //outputString = "Error: Invalid phonogram for encryption.";
                }
                catch (StringIndexOutOfBoundsException e){
                    this.model.addHistoryRecord("Invalid phonogram element.\nString index out of bounds. String length is not multiple of phonogram length.");
                    this.storeInDB("Decryption Error", "Invalid phonogram element.\nString index out of bounds. String length is not multiple of phonogram length.");
                    
                    this.updateStatisticCookie("error", request, response);
                    response.sendError(response.SC_BAD_REQUEST, "Error 2: Invalid phonogram for decryption. String length is not a multiple of phonogram length");
                    return;
                    //outputString = "Error: Invalid phonogram element.\nString index out of bounds. String length is not multiple of phonogram length.";
                }
                break;

            case "Encrypt":
                this.model.addHistoryRecord("Attepmting to encode: " + userInput);
                this.storeInDB("Encryption", "Attepmting to encode: " + userInput);

                // Try to encode the string if that fails, show appropriate messages
                if(this.model.checkStringForEncoding(userInput) == true){
                    messageType = "Encrypted String: ";
                    outputString = model.encrypt(userInput);
                    
                    this.model.addHistoryRecord("Encoded string: " + outputString);
                    this.storeInDB("Encryption", "Encoded String: " + outputString);
                    this.updateStatisticCookie("encode", request, response);
                }
                else{
                    //messageType = "Encryption error: ";
                    //outputString = "Encoding failed, please use only supported characters";

                    this.model.addHistoryRecord("Encoding failed, please use only supported characters");
                    this.storeInDB("Encryption error", "Encoding failed, please use only supported characters");

                    // Update cookies and send an error reponse
                    this.updateStatisticCookie("error", request, response);
                    response.sendError(response.SC_BAD_REQUEST, "Error 3: Encoding failed, please use only supported characters");
                    return;
                }

                break;

            default:
                messageType = "Error occured: ";
                outputString = "Unknown mode: " + mode;

                this.model.addHistoryRecord("Error occured, unknown mode of oepration encountered: " + mode);
                this.storeInDB("Mode", "Error occured, unknown mode of oepration encountered: " + mode);

                this.updateStatisticCookie("error", request, response);
                response.sendError(response.SC_BAD_REQUEST, "Error 4: Unknown mode selected" + mode);
                break;
        }

        // If the output is of length zero then there was no data given
        if(outputString.length() == 0){
            this.model.addHistoryRecord("Error: Output string is empty, input should be given");
            this.storeInDB("Input", "Error: Output string is empty, input should be given");

            this.updateStatisticCookie("error", request, response);
            response.sendError(response.SC_BAD_REQUEST, "Input should be given");
        }
        else{

            try (PrintWriter out = response.getWriter()) {
                out.println("<html>");
                out.println("<body>");
                
                out.println("<h1>Instruction</h1>");
                out.println("<section>");
                
                out.println("<ul>");

                out.println("<li><strong>Outcomes textbox: </strong>Encoding / decoding outcomes are stored in this box.</li>");
                out.println("<li><strong>Main Page link: </strong>Redirects to the main page (theoritically).</li>");
                out.println("<li><strong>Statistics: </strong>Statistics gathered about the number of operations. Values based on cookie data.</li>");
                out.println("</ul>");
                
                out.println("</section>");
                
                out.println("<h1>Outcomes</h1>");
                out.println("<section>");

                out.println("<div class=\"output-box\">");

                // Message which informs user what sort of data is displayed
                out.println("<p>");
                out.println(messageType);
                out.println("</p>");

                // Textarea stored processed data or error code
                out.println("<textarea>");
                out.println(outputString);
                out.println("</textarea>");
                out.println("<br>");
                out.println("<br>");

                // Button which allows to go back to the 
                out.println("<a href='test.html'> Main Page </a><br>");
                out.println("</section>");
                
                out.println("<h1>Stats</h1>");
                out.println("<section>");
                
                out.println("<p>Encodings: " + this.getCookieValue("encode", request) + "</p><br>");
                out.println("<p>Decodings: " + this.getCookieValue("decode", request) + "</p><br>");
                out.println("<p>Errors: " + this.getCookieValue("error", request) + "</p>");

                out.println("</div>");
                
                out.println("</section>");
                
                // Special error section
                
                out.println("<h1>Special Info</h1>");
                out.println("<section>");
                
                out.println("<div style=\"color: white; background-color: red; padding: 10px; border-radius: 5px;\">");
                out.println("<strong>");
                out.println(this.dbErrorMessage);
                out.println("</strong>");
                out.println("</div>");
                
                out.println("</section>");

                out.println("</body>");
                out.println("</html>");
            }
            catch(Exception e){
                response.sendError(response.SC_BAD_REQUEST, "PrintWrite Writer could not have been obtained.");
            }
        }
    }
   
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @see #processRequest(HttpServletRequest, HttpServletResponse)
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @see #processRequest(HttpServletRequest, HttpServletResponse)
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet handles encoding and decoding of strings and shows simple statistical data how many times each operation has been done.";
    }
}