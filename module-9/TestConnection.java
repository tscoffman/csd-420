package testconnection;

/*
Trenten Coffman
November 8, 2026
Assignment 9.2

Tests connection to MySQL 5.7 database: databasedb using user: student1
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConnection {

    public static void main(String[] args) {
        // changed port from 3306 to 3307 due to having another installation
        // of MySQL already
        String url = "jdbc:mysql://localhost:3307/databasedb";
        String user = "student1";
        String password = "pass";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM address33");
 
                System.out.println("Connected to databasedb as " + user);
                System.out.println("MySQL server version: " + conn.getMetaData().getDatabaseProductVersion());
                System.out.println("----------------------------------------");
 
                while (rs.next()) {
                    System.out.println(
                        rs.getInt("ID") + " | "
                        + rs.getString("FIRSTNAME") + " "
                        + rs.getString("LASTNAME") + " | "
                        + rs.getString("STREET") + ", "
                        + rs.getString("CITY") + ", "
                        + rs.getString("STATE") + " "
                        + rs.getString("ZIP"));
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL driver not found. Add the Connector/J JAR to the project Libraries.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
