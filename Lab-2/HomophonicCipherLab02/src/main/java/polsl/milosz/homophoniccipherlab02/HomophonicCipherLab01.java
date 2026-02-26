/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package polsl.milosz.homophoniccipherlab02;

import mvc.controller.Controller;

/**
 * Wrapper class of the application.
 * Contains the main() method.
 * Creates instance of the Controller from the MVC pattern and calls it runApp() function
 * which starts the app.
 * All command line arguemnts are concatenated and passed into the running application.
 * 
 * @author Milosz Liniewiecki
 * @version 1.0
 */
public class HomophonicCipherLab02 {
    
    /**
     * Default constructor of the HomophonicCipherLab01 class.
     * Initiates the class instance. 
     */
    public HomophonicCipherLab02(){
        // Some text
    }
    
    /**
     * The main() function of the program.
     * Instantiates the Controller and concatenates the command line arguments.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Create instanc eof the controller
        Controller controller = new Controller();
        // Fuse all command line args into a single string
        
        String allCmdArgs = "";
        for(String s : args){
            allCmdArgs += (s + " ");
        }
        controller.runApp(allCmdArgs);
        
    }
}
