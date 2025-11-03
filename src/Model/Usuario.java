package Model;
public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String contrasena;

    // Constructor vacío necesario para DAO
    public Usuario() {}

    // Constructor para autenticación
    public Usuario(int id, String correo) {
        this.id = id;
        this.correo = correo;
    }

    // Constructor completo
    public Usuario(String nombre, int carnet, String correo, String contrasena) {
        this.nombre = nombre;
        this.id = carnet;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    // Getters y setters
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }

    public String getNombre(){ return nombre; }
    public void setNombre(String n){ this.nombre = n; }

    public String getCorreo(){ return correo; }
    public void setCorreo(String c){ this.correo = c; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String p){ this.contrasena = p; }

    public boolean login(String correo, String password) {
        return this.correo.equals(correo) && this.contrasena.equals(password);
    }
}