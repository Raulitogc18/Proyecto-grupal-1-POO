package modelos;

public class Usuario {
    private int camet;
    private String nombre;
    private String correo;
    private String contraseña;
    private boolean loggedIn;
    
    public Usuario(int camet, String nombre) {
        this.camet = camet;
        this.nombre = nombre;
        this.loggedIn = false;
    }
    
    public boolean login(String correo, String contraseña) {
        // Verificar que sea correo UVG
        if (correo != null && correo.toLowerCase().endsWith("@uvg.edu.gt")) {
            // En un sistema real, aquí se verificaría contra una base de datos
            this.correo = correo;
            this.contraseña = contraseña;
            this.loggedIn = true;
            System.out.println("✅ Login exitoso: Bienvenido " + this.nombre);
            return true;
        } else {
            System.out.println("❌ Error: Solo se permiten correos de la UVG (@uvg.edu.gt)");
            return false;
        }
    }
    
    public void logOut() {
        this.loggedIn = false;
        System.out.println("👋 Sesión cerrada: " + this.nombre);
    }
    
    public boolean verificarCorreoUVG() {
        return correo != null && correo.toLowerCase().endsWith("@uvg.edu.gt");
    }
    
    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    // Getters y Setters
    public int getCamet() { return camet; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
}