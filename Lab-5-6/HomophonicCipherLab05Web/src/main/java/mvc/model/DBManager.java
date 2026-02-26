/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// TODO:
// 1. Change all of SQL to use the EntityManager (slide 10/11 on the lecture .persist, .find, ...

/**
 * Serves as a communication device between servlets and the DB.
 * Manages operations on the DB like persisting objects and their retrieval.
 * 
 * @author Milosz Liniewiecki
 * @version 1.0
 */
public class DBManager {
    
    /**
     * Entity Manager Factory allows to create Entity  Managers which mediate in communication with DB.
     */
    private final EntityManagerFactory emf;
    
    /**
     * Constructor of the DBManager class.
     * Creates the Entity Manager Factory for the given named persistance unit.
     * 
     * @param puUnit Name of the persistence unit.
     */
    public DBManager(String puUnit){
        this.emf = Persistence.createEntityManagerFactory(puUnit);
    }

    /**
     * Stores given object in the DB.
     * 
     * @param object Object which will be persisted in the DB.
     */
    public void persistObject(Object object){
        // Create the entity manager to gain access to the db
        EntityManager em = this.emf.createEntityManager();
        em.getTransaction().begin();
        
        try{
            // Persist the object in database
            em.persist(object);
            em.getTransaction().commit();
        }
        catch (PersistenceException e){
            //e.printStackTrace();
            em.getTransaction().rollback();
        }
        finally{
            em.close();
        }
    }
    
    /**
     * Returns the contents of the inputs table.
     * 
     * @return List of inputs form the input table.
     */
    public List<TableInput> findInputs() {
        EntityManager em = this.emf.createEntityManager();
        List<TableInput> inputs;
        
        try{
            TypedQuery<TableInput> query = em.createQuery("SELECT i from TableInput i", TableInput.class);
            inputs = query.getResultList();
        }
        catch (PersistenceException e){
            return new ArrayList<>();
        }
        finally {
            if(em.isOpen())
                em.close();
        }
        
        return inputs;
    }
    
    /**
     * Parses the outputs table and returns its contents.
     * 
     * @return List of elements from the outputs table.
     */
    public List<TableOutput> findOutputs() {
        List<TableOutput> outputsList = null;
        EntityManager em = this.emf.createEntityManager();
        
        em.getTransaction().begin();
        try {
            Query query = em.createQuery("SELECT o FROM TableOutput o"); //WHERE p.name LIKE '7%'
            outputsList = query.getResultList();
        } catch (PersistenceException e) {
            //e.printStackTrace(); // replace with proper message for the client
            em.getTransaction().rollback();
            
        } finally {
            em.close();
        }
        return outputsList;
    }
    
    
}
