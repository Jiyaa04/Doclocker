<%-- 
    Document   : signup
    Created on : 06-Aug-2024, 2:07:45 am
    Author     : Jiya
--%>

<%--<%@page import="com.sun.jdi.connect.spi.Connection"%>--%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.SQLException"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DocLocker - Login</title>
    <link rel="stylesheet" href="newcss.css">
 
</head>
<body>
    <div class="container">
        <h1>Register yourself to your digital image locker<br><b>DocLocker!</b></h1>
        <form action="signup.jsp" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required><br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br>
        <label for="confirmPassword">Confirm Password:</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required><br>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br>
        <input type="submit" value="Register">
        <a href="index.html">Already registered?, click here to login</a>
        
    </form>
        <%
            if (request.getMethod().equalsIgnoreCase("post")) {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String confirmPassword = request.getParameter("confirmPassword");
                String email = request.getParameter("email");

                if (!password.equals(confirmPassword)) {
                    out.println("<h3>Passwords do not match. Please try again.</h3>");
                } else {
                    String hashedPassword = null;
                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        byte[] hashedBytes = md.digest(password.getBytes());
                        StringBuilder sb = new StringBuilder();
                        for (byte b : hashedBytes) {
                            sb.append(String.format("%02x", b));
                        }
                        hashedPassword = sb.toString();
                    } catch (NoSuchAlgorithmException e) {
                        out.println("<h3>Error hashing password. Please try again later.</h3>");
                    }

                    Connection conn = null;
                    PreparedStatement pstmt = null;
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doclocker", "root", "");

                        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
                        pstmt = conn.prepareStatement(query);
                        pstmt.setString(1, username);
                        pstmt.setString(2, hashedPassword);
                        pstmt.setString(3, email);

                        int result = pstmt.executeUpdate();

                        if (result > 0) {
                            out.println("<h3>Registration successful!</h3>");
                            out.println("<a href='index.html'>Click here to login</a>");
                        } else {
                            out.println("<h3>Registration failed. Please try again later.</h3>");
                        }
                    } catch (ClassNotFoundException e) {
                        out.println("<h3>Driver not found. Please contact support.</h3>");
                        e.printStackTrace();
                    } catch (SQLException e) {
                        out.println("<h3>Database error: " + e.getMessage() + ". Please try again.</h3>");
                        e.printStackTrace();
                    } finally {
                        try {
                            if (pstmt != null) pstmt.close();
                            if (conn != null) conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        %>
        <div class="footer">
            <p>Powered by <strong>DocLocker</strong> &copy; 2024</p>
            <div class="social-icons">
                <a href="#"><i class="facebook"></i></a>
                <a href="#"><i class="twitter"></i></a>
                <a href="#"><i class="linkedin"></i></a>
                <a href="#"><i class="instagram"></i></a>
            </div>
        </div>
    </div>
    
</body>
</html>