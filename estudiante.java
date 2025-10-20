public class estudiante {
    private int carnet;
    private String nombre;
    private String correo;

    public estudiante(int carnet, String nombre) {
        this.carnet = carnet;
        this.nombre = nombre;
    }

    public int getCarnet() {
        return carnet;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return nombre + " (carnet: " + carnet + ")" + (correo != null ? " <" + correo + ">" : "");
    }
}
