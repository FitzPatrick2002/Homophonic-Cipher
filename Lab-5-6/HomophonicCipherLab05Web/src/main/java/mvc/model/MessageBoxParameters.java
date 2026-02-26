/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.model;

/**
 * Holds data necessary for initialization of a message box (JOptionPane).
 * 
 * @param type Specifies the type of box to show. 0 - error, 1 - info, 2 - warning, 3 - question, 4 - plain.
 * @param title Title of the message box.
 * @param message Message printed in the message box.
 * @author Milosz Liniewiecki
 * @version 1.0
 */
public record MessageBoxParameters(int type, String title, String message) {}
