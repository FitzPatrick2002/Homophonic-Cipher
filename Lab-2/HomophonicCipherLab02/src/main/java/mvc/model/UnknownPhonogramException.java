/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.model;

/**
 * Thrown when an unknown encryption character (phongram)is encoutered during decding process.
 * 
 * @author Milosz Liniewiecki
 * @version 1.0
 */
public class UnknownPhonogramException extends Exception{
    /**
     * Constructs a new exception with {@code null} as its internal message.
     */
    public UnknownPhonogramException(){
        // Some comment to keep world spinning
    }
    
    /**
     * Constructs the new exception with specified detailed message.
     * 
     * @param message the detailed message
     */
    public UnknownPhonogramException(String message){
        super(message);
    }
}
