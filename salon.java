public class salon {
    private int id;
    private String nombre;
    private String ubicacion;
    private int capacidad;

    public salon(String ubicacion, int capacidad) {
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
        this.nombre = null;
    }

    public salon(String ubicacion, String capacidadStr) {
        this.ubicacion = ubicacion;
        try {
            this.capacidad = Integer.parseInt(capacidadStr);
        } catch (NumberFormatException e) {
            this.capacidad = 0;
        }
        this.nombre = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getNombre() {

        return (nombre == null || nombre.trim().isEmpty()) ? ubicacion : nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean disponibilidad(horario h) {
        return true; // lógica a implementar según reservas y horario
    }

    @Override
    public String toString() {
        return getNombre() + " - " + ubicacion + " (" + capacidad + " personas)";
    }
}
