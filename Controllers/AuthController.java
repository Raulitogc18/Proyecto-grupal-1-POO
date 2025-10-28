package Controllers;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

public class AuthController {

    private static final SecureRandom rnd = new SecureRandom();

    // REGISTRO DE NUEVA CUENTA
    public static boolean registrarCuenta(String nombre, String carnet, String correo, String password, String salonRentado) {
        if (correo == null || password == null || nombre == null) return false;

        try (Connection conn = conexion.conectar()) {
            if (conn == null) return false;

            // Verificar si el correo ya existe
            String checkSql = "SELECT correo FROM usuarios WHERE correo = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, correo);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                System.out.println("El correo ya está registrado.");
                return false;
            }

            // Insertar nuevo usuario
            String sql = "INSERT INTO usuarios (nombre, carnet, correo, contrasena, salon_rentado) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, carnet);
            ps.setString(3, correo);
            ps.setString(4, hash(password));
            ps.setString(5, salonRentado);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar: " + e.getMessage());
            return false;
        }
    }

    // Nueva sobrecarga: crear cuenta sin salón
    public static boolean registrarCuenta(String nombre, String carnetStr, String correo, String pass) {
        return registrarCuenta(nombre, carnetStr, correo, pass, null);
    }

    // Legacy non-database registration removed; use the database-backed registrarCuenta methods above.

    // AUTENTICAR LOGIN
    public static boolean autenticar(String correo, String password) {
        if (correo == null || password == null) return false;

        try (Connection conn = conexion.conectar()) {
            if (conn == null) return false;

            String sql = "SELECT contrasena FROM usuarios WHERE correo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String stored = rs.getString("contrasena");
                String hashed = hash(password);
                return stored.equals(hashed);
            } else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error al autenticar: " + e.getMessage());
            return false;
        }
    }

    // VERIFICAR SI EXISTE EL CORREO
    public static boolean existeCuenta(String correo) {
        try (Connection conn = conexion.conectar()) {
            if (conn == null) return false;

            String sql = "SELECT id_usuarios FROM usuarios WHERE correo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error al verificar existencia: " + e.getMessage());
            return false;
        }
    }

    // RESETEAR CONTRASEÑA (sin token real, solo ejemplo)
    public static boolean resetPassword(String correo, String nuevaContrasena) {
        try (Connection conn = conexion.conectar()) {
            if (conn == null) return false;

            String sql = "UPDATE usuarios SET contrasena = ? WHERE correo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, hash(nuevaContrasena));
            ps.setString(2, correo);
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al resetear contraseña: " + e.getMessage());
            return false;
        }
    }

    private static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            return null;
        }
    }
}
