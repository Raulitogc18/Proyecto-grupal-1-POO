public class salon {
    private String nombre;
    private boolean disponible;

    public salon(String nombre) {
        this.nombre = nombre;
        this.disponible = true;
    }

    public String getNombre() { return nombre; }
    public boolean estaDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}