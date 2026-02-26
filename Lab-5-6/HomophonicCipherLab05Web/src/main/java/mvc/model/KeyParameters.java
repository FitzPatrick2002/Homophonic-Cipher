/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.model;

/**
 * Parameters necessary for the Model class, to generate a new custom key.
 * 
 * @param phonogramLength Number of digits present in a single phonogram.
 * @param minPhonograms Minimal number phonograms assigned to a character.
 * @param maxPhonograms Maximal number of phonograms assigned to a character.
 * @author Milosz Liniewiecki
 * @version 1.0
 */
public record KeyParameters(int phonogramLength, int minPhonograms, int maxPhonograms){}