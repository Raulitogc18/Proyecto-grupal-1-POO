public class Salon {
    private String ubicacion;
    private int capacidad;

    public Salon(String ubicacion, int capacidad) {
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getNombre() {
        // Puedes usar la ubicación como nombre
        return ubicacion;
    }

    // Método para chequear disponibilidad (simulado)
    public boolean disponibilidad(Horario h) {
        return true; // puedes agregar la lógica más adelante
    }

    @Override
    public String toString() {
        return ubicacion + " (" + capacidad + " personas)";
    }
}
