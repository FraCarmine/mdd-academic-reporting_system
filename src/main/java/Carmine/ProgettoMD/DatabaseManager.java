package Carmine.ProgettoMD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    // L'URL completo con utente e password integrati
    private static final String URL = "jdbc:mariadb://localhost/progettomd?user=root";

    private static DatabaseManager instance;
    private Connection connection;

    /**
     * Costruttore privato del Singleton
     */
    private DatabaseManager() {
        try {

            this.connection = DriverManager.getConnection(URL);
            System.out.println("Connessione al database stabilita con successo!");
            
        } catch(SQLException ex) {
            System.out.println("SqlException: " + ex.getMessage()); 
            System.out.println("Sql State: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode()); 
        }   
    }

    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

//restituisce connessione e la riapre se dovesse cadere
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
            }
        } catch(SQLException ex) {
            System.out.println("SqlException: " + ex.getMessage()); 
            System.out.println("Sql State: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode()); 
        }
        return connection;
    }

  
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connessione chiusa in modo sicuro.");
            }
        } catch(SQLException ex) {
            System.out.println("SqlException: " + ex.getMessage()); 
            System.out.println("Sql State: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode()); 
        }
    }
}