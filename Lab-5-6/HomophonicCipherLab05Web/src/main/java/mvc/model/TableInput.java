/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Table stores the "input" strings.
 * Inputs are understood as oepration undertaken by users / mode of operation in which app currently is.
 * 
 * @author Milosz Liniewiecki
 * @version 1.0
 */
@Entity
@Getter
@Setter
@Table(name = "INPUTS")
public class TableInput implements Serializable {
    
    /**
     * The primary key of a row in the table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /**
     * Value stored in the table entry.
     */
    @Column(name = "INPUTS")
    private String inputValue;
    
    /**
     * Default constructor is empty.
     */
    public TableInput(){
        // Short comment about life
    }

    /**
     * Hash function for this object.
     * 
     * @return Hashcode of the object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        if (this.id != null)
            hash = id.hashCode();
        
        return hash;
    }

    /**
     * Comparison function for two objects of type TableInput.
     * 
     * @param obj Other object which will be checked against this one.
     * @return True if objects have the same contents.
     */
    @Override
    public boolean equals(Object obj) {
        
        if(obj == null)
            return false;
        
        if (!(obj instanceof TableInput))
            return false;
        
        TableInput casted = (TableInput) obj;
        
        if((this.id != null && casted.id != null) && this.id.equals(casted.id))
            return true;
        
        return false;
    }

}
