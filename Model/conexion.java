package Model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/proyecto_poo?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // si usas contraseña, ponla aquí

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // fallar rápido si falta el driver
            throw new ExceptionInInitializerError("MySQL JDBC Driver no encontrado: " + e.getMessage());
        }
    }

    // Devuelve una conexión; lanza SQLException si falla para que el DAO lo maneje
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
