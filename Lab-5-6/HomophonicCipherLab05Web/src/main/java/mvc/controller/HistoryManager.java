/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import mvc.model.HistoryRecord;
import mvc.model.Model;

import mvc.model.DBManager;
import mvc.model.TableOutput;
import mvc.model.TableInput;
/**
 * Displays history of operations which is stored in the Model from MVC pattern.
 * Data is displayed in tabular form: [ idx | Info ].
 * 
 * @author Milosz Liniewiecki
 * @version 1.0
 */
@WebServlet(name = "History", urlPatterns = {"/History"})
public class HistoryManager extends HttpServlet {
    
    /**
     * Reference to the Model in MVC pattern which stores the historical data which we need.
     */
    private Model model;
    
    /**
     * Database manager takes care of all communication with the DB.
     */
    private DBManager dbManager;
    
    /**
     * Cosntructor of the HistoryManager is empty.
     */
    public HistoryManager(){
        // Short comment about life
    }
    
    /**
     * Constructs the history table based on the information from {@link #model}.
     * 
     * @param out Should be the writer which writes out to the html file which will be displayed as the website.
     */
    private void printHistoryTable(PrintWriter out){
        
        this.model.addHistoryRecord("History accessed");
        
        // Access the model
        try{
            // Print the desscription of the servlet
            
            out.println("<h3>");
            out.println("Instruction");
            out.println("</h3>");
            
            out.println("<section>");
            
            out.println("<ul>");
            
            out.println("<li><strong>History table: </strong>Hisotry table stores the hisotry of all operations in the format: [ entry-number | message ], where message records what happened.</li>");
            out.println("<li><strong>Databse Entries: </strong>Contain the history of succesfull operations.</li>");
            
            out.println("</ul>");
            
            out.println("</section>");
            
            // ------------------------------------ //
            
            out.println("<h3>");
            out.println("History");
            out.println("</h3>");
            
            out.println("<section>");
            
            // Create the table
            out.println("<table>");

            // Write out a new row | Record | Message |
            out.println("<tr>");

            out.println("<th>Record</th>");
            out.println("<th>Message</th>");

            out.println("</tr>");
            
            for(HistoryRecord record : model.getHistoryList()){
                // Write out a new row | idx | message |
                out.println("<tr>");

                out.println("<td>" + record.idx() + "</td>");
                out.println("<td>" + record.message() + "</td>");

                out.println("</tr>");
            }
            out.println("</table>");
            
            out.println("</section>");
            
            // ------------------------------------ //
            
            out.println("<h3>");
            out.println("Database Entries");
            out.println("</h3>");
            
            out.println("<section>");
            
            out.println("<br><p>DB contents contain only successfull inputs and outputs.</p>");
            out.println("<table>");

            // Write out a new row | Record | Message |
            out.println("<tr>");

            out.println("<th>Input</th>");
            out.println("<th>Output</th>");

            out.println("</tr>");
            
            List<TableInput> inputs = dbManager.findInputs();
            List<TableOutput> outputs = dbManager.findOutputs();
            
            for(int i = 0; i < inputs.size(); i++){
                out.println("<tr>");

                out.println("<td>" + inputs.get(i).getInputValue() + "</td>");
                out.println("<td>" + outputs.get(i).getOutputValue() + "</td>");

                out.println("</tr>");
            }
            out.println("</table>");
                        
            out.println("</section>");

        }
        catch (Exception e){
            this.model.addHistoryRecord("Could not access the history");
            out.println("<h1>History could not have been accessed from the Model. Error info: " + e.getMessage() + "</h1>");
        }
    }
    
    /**
     * Inits the HistoryManger servlet.
     * Assings a refernce to {@link #model} from the servlet context attribute.
     * 
     * @throws ServletException In case initialization fails, exception is thrown.
     */
    @Override
    public void init() throws ServletException{
        super.init();
        
        this.model = (Model) this.getServletContext().getAttribute("model");
        this.dbManager = (DBManager)this.getServletContext().getAttribute("dbManager");
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * Obtains the Writer for the response and writed the html table into it 
     * using {@link #printHistoryTable}.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet HistoryManager</title>");
            out.println("</head>");
            out.println("<body>");
            
            // Print the history table 
            this.printHistoryTable(out);
            System.out.println("HERE I AM");
            System.out.println("Size: " + Integer.toString(this.model.getHistoryList().size()));

            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @see #processRequest(HttpServletRequest, HttpServletResponse)
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @see #processRequest(HttpServletRequest, HttpServletResponse)
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description.
     */
    @Override
    public String getServletInfo() {
        return "Displays history of operations done in the app.";
    }// </editor-fold>
}
