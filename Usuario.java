public class Usuario {

    private String nombre;
    private int carnet;
    private String correo;
    private String contraseña;

    public Usuario(String nombre, int carnet, String correo, String contraseña) {
        this.nombre = nombre;
        this.carnet = carnet;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCarnet() {
        return carnet;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public boolean login(String correo, String contraseña) {
        return this.correo.equals(correo) && this.contraseña.equals(contraseña);
    }

    public void logout() {
    }

}
