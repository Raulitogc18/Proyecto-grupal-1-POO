public class usuario {
    private int id;
    private String nombre;
    private int carnet;
    private String correo;
    private String contrasena;

    // Constructor vacío necesario para DAO
    public usuario() {}

    // Constructor para autenticación
    public usuario(int id, String correo) {
        this.id = id;
        this.correo = correo;
    }

    // Constructor completo
    public usuario(String nombre, int carnet, String correo, String contrasena) {
        this.nombre = nombre;
        this.carnet = carnet;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCarnet() { return carnet; }
    public void setCarnet(int carnet) { this.carnet = carnet; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public boolean login(String correo, String password) {
        return this.correo.equals(correo) && this.contrasena.equals(password);
    }
}