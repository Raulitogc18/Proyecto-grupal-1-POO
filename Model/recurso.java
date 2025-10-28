package Model;
public class recurso {
    private String nombre;
    private String tipo;
    private String descripcion;
    private int cantidad;

    public recurso(String nombre, String tipo, String descripcion, int cantidad) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ") - Disponibles: " + cantidad;
    }
}
