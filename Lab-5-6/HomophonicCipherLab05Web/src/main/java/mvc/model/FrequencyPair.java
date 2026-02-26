/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.model;

/**
 * Pairs strings with doubles.
 * Intended use is to pair letters of the english alphabet with their frequency of occuring. 
 * 
 * @param character Letter from english alphabet (or some symbol like '!' or '.').
 * @param frequency Frequency of occuring expressed in %. 
 * @author Milosz Liniewiecki
 * @version 1.0
 */
public record FrequencyPair(String character, Double frequency){}
