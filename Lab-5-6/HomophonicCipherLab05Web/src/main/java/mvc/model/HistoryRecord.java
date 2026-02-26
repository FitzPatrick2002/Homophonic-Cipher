/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.model;

/**
 * Stores information about single history record, put in the history table.
 * 
 * @see mvc.model.Model
 * @param idx Index of a record. Records are indexed sequentially as they are created.
 * @param message Contents of a record.
 * @author Milosz Liniewiecki
 * @version 1.0
 */
public record HistoryRecord (int idx, String message){}


