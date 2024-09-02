import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
public class UploadServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        
        if (fileName == null || fileName.isEmpty()) {
            response.getWriter().println("No file selected.");
            return;
        }

        HttpSession session = request.getSession();
        Integer user_id = (Integer) session.getAttribute("user_id"); 
        
        if (user_id == null) {
            response.getWriter().println("User not logged in.");
            return;
        }

        String dbURL = "jdbc:mysql://localhost:3306/doclocker";
        String dbUser = "root";
        String dbPassword = "";

        Connection conn = null;
        PreparedStatement statement = null;
        InputStream inputStream = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            String sql = "INSERT INTO documents (user_id, file_name, image_data) VALUES (?, ?, ?)";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, user_id); 
            statement.setString(2, fileName);

            inputStream = filePart.getInputStream();
            statement.setBlob(3, inputStream);

            int result = statement.executeUpdate();

            if (result > 0) {
                response.getWriter().println("<center>Image uploaded successfully!</center><br><br>");
                request.getRequestDispatcher("user_home.html").include(request, response);
            } else {
                response.getWriter().println("Failed to upload image.");
                request.getRequestDispatcher("user_home.html").include(request, response);
            }

        } catch (SQLException | ClassNotFoundException e) {
            response.getWriter().println("Error: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    
                }
            }
        }    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Image Upload Servlet";
    }
}
