package Model;
public class Salon {
    private int id;
    private String nombre;
    private String ubicacion;
    private String capacidad;

    public Salon() {}
    public Salon(String ubicacion, String capacidad) { this.ubicacion = ubicacion; this.capacidad = capacidad; }
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public String getNombre(){ return nombre; }
    public void setNombre(String n){ this.nombre = n; }
    public String getUbicacion(){ return ubicacion; }
    public String getCapacidad(){ return capacidad; }
    // Método simple de disponibilidad para que Controlador compile
    public boolean disponibilidad(Object horario){ return true; }
}
