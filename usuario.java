public class usuario {
    private int carnet;
    private String nombre;
    private String correo;
    private String contrasena;
    private boolean loggedIn;

    public usuario(int carnet, String nombre) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.loggedIn = false;
    }

    public boolean login(String correo, String contrasena) {
        if (correo != null && correo.toLowerCase().endsWith("@uvg.edu.gt")) {
            // En un sistema real se validaría contra un servicio/BD y se usaría hash para la contraseña
            this.correo = correo;
            this.contrasena = contrasena;
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
    public int getCarnet() { return carnet; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}