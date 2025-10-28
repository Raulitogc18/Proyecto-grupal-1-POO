package Model;
public class salon {
    private int id;
    private String nombre;
    private String ubicacion;
    private int capacidad;

    public salon() {}

    public salon(String ubicacion, String capacidad) {
        this.ubicacion = ubicacion;
        this.capacidad = Integer.parseInt(capacidad);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public boolean disponibilidad(horario horario) {
        // Por ahora retorna true, implementa la lógica real según tus necesidades
        return true;
    }
}
