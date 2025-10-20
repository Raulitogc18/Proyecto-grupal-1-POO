import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthController {
    private static final Map<String, String> credentials = new ConcurrentHashMap<>(); // correo(lower) -> hashed password
    private static final Map<String, String> resetTokens = new ConcurrentHashMap<>(); // correo(lower) -> token
    private static final SecureRandom rnd = new SecureRandom();

    public static boolean registrarCuenta(String correo, String password) {
        if (correo == null || password == null) return false;
        String key = correo.trim().toLowerCase();
        if (credentials.containsKey(key)) return false;
        String hashed = hash(password);
        if (hashed == null) return false;
        credentials.put(key, hashed);
        return true;
    }

    public static boolean autenticar(String correo, String password) {
        if (correo == null || password == null) return false;
        String key = correo.trim().toLowerCase();
        String stored = credentials.get(key);
        if (stored == null) return false;
        String hashed = hash(password);
        return hashed != null && stored.equals(hashed);
    }

    public static boolean existeCuenta(String correo) {
        if (correo == null) return false;
        return credentials.containsKey(correo.trim().toLowerCase());
    }

    // Genera un token numérico (para demo; en un sistema real se enviaría por email)
    public static String generarTokenReset(String correo) {
        if (!existeCuenta(correo)) return null;
        String token = String.format("%06d", rnd.nextInt(1_000_000));
        resetTokens.put(correo.trim().toLowerCase(), token);
        return token;
    }

    public static boolean resetPasswordConToken(String correo, String token, String nuevaContrasena) {
        if (correo == null || token == null || nuevaContrasena == null) return false;
        String key = correo.trim().toLowerCase();
        String t = resetTokens.get(key);
        if (t != null && t.equals(token)) {
            String hashed = hash(nuevaContrasena);
            if (hashed == null) return false;
            credentials.put(key, hashed);
            resetTokens.remove(key);
            return true;
        }
        return false;
    }

    private static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            // Si falla el hash, preferimos no almacenar la contraseña en claro
            return null;
        }
    }
}