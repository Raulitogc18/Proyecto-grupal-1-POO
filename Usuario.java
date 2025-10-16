public class Usuario {
    protected int carnet;
    protected String nombre;
    protected String correo;
    protected String contrasena;

    public Usuario(int carnet, String nombre) {
        this.carnet = carnet;
        this.nombre = nombre;
    }

    public boolean login() {
        return true;
    }

    public void logout() {
    }

    public int getCarnet() { return carnet; }

    public String getNombre() { return nombre; }

    public String getCorreo() { return correo; }

    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }

    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
