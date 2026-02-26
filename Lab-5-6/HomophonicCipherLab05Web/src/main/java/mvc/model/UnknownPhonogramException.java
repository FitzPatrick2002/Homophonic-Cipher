/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.model;

import lombok.*;

/**
 * Thrown when an unknown encryption character (phongram)is encoutered during decding process.
 * 
 * @author Milosz Liniewiecki
 * @version 1.1
 */
@NoArgsConstructor
public class UnknownPhonogramException extends Exception{
    
    /**
     * Constructs the new exception with specified detailed message.
     * 
     * @param message the detailed message
     */
    public UnknownPhonogramException(String message){
        super(message);
    }
}
