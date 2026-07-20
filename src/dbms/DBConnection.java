package dbms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBConnection {

    private static Connection conn;

    public static Connection getConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/bookhaven12";
            String username = "root"; 
            String password = ""; 
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed:\n" + e.getMessage());
        }
        return conn;
    }
}
