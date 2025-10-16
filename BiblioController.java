
import java.util.ArrayList;
import java.util.List;

public class BiblioController {
    private List<salon> salones = new ArrayList<>();
    private List<reserva> reservas = new ArrayList<>();

    public BiblioController() {
        salones.add(new salon("Sala A"));
        salones.add(new salon("Sala B"));
        salones.add(new salon("Sala C"));
    }

    public void mostrarDisponibilidad() {
        System.out.println("Disponibilidad de salones:");
        for (salon s : salones) {
            String estado = s.estaDisponible() ? "Disponible" : "Ocupado";
            System.out.println("- " + s.getNombre() + ": " + estado);
        }
    }

    public void reservarSalon(int indice, reserva reserva) {
        salon salon = salones.get(indice);
        if (salon.estaDisponible()) {
            salon.setDisponible(false);
            reservas.add(reserva);
            System.out.println("✅ Reserva realizada con éxito en " + salon.getNombre());
        } else {
            System.out.println("❌ El salón ya está ocupado.");
        }
    }

    public List<reserva> getReservas() {
        return reservas;
    }
}

