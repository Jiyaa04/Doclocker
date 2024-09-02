/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jiya
 */
public class ShowFilesServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        Integer user_id = (Integer) session.getAttribute("user_id");
        
         String dbURL = "jdbc:mysql://localhost:3306/doclocker";
        String dbUser = "root";
        String dbPassword = "";

        try (PrintWriter out = response.getWriter()) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try ( 
                    Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            
                String sql = "SELECT Id, file_name FROM documents where user_id = '"+user_id+"'";
                PreparedStatement statement = conn.prepareStatement(sql);
                
                ResultSet resultSet = statement.executeQuery();
                
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Your Documents</title>");
                out.println("<link rel='stylesheet' href='newcss.css'>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='container'>");
                out.println("<h2>Your Uploaded Documents</h2>");
                out.println("<table>");
                out.println("<tr><th>File Name</th><th>Preview</th><th>Delete</th></tr>");
                
                boolean found = false;
                while (resultSet.next()) {
                    found = true;
                    int id = resultSet.getInt("Id");
                    String fileName = resultSet.getString("file_name");
                    
                    out.println("<tr>");
                    //out.println("<td>" + id + "</td>");
                    out.println("<td>" + fileName + "</td>");
                    out.println("<td><a href='PreviewServlet?id=" + id + "'>View</a></td>");
                    out.println("<td><a href='deleteServlet?id=" +id+ "'>Delete</a></td>");
                    out.println("</tr>");
                }
                
                out.println("</table>");
                if (!found) {
                    out.println("<p>No documents found.</p>");
                }
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }

        } catch (SQLException | ClassNotFoundException e) {
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
