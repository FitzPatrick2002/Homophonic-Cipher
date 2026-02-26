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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Table stores outputs of user operations.
 * Outputs are understood either as literal outputs of for example encoding operations
 * or an error message produced in case the operation failed.
 * 
 * @author Milosz Liniewiecki
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "OUTPUTS")
public class TableOutput implements Serializable{
    
    /**
     * Primary key for a single entry in the table.
     */
    @Id
    private Long id;
    
    /**
     * Result of some operation.
     */
    @Column(name  = "OUTPUTS")
    private String outputValue;
    
    /**
     * A one-to-one mapping from the Inputs table to the outputs table.
     * Each output can be associated with only a single input.
     */
    @OneToOne
    @MapsId
    @JoinColumn(name="id")
    private TableInput tableInput;
    
    /**
     * Empty constructor.
     */
    public TableOutput(){
        // Short comment about life
    }

    /**
     * Hashing function of this object.
     * Hashing is based on the primary key.
     * 
     * @return Integer value which represents the hash code of this table.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        if(id != null)
            hash = id.hashCode();
        
        return hash;
    }

    /**
     * Compares the instance of the table with other object.
     * 
     * @param obj Object which we want to compare.
     * @return True in case the compared object has the same id and is of the same type.
     */
    @Override
    public boolean equals(Object obj) {

        if(obj == null)
            return false;
        
        if (!(obj instanceof TableInput))
            return false;
        
        TableOutput casted = (TableOutput) obj;
        
        if((this.id != null && casted.id != null) && this.id.equals(casted.id))
            return true;
        
        return false;
    }
    
}
