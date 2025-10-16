

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class NotificadorController {

    public static void verificarReservas(List<reserva> reservas) {
        LocalDateTime ahora = LocalDateTime.now();

        for (reserva reserva : reservas) {
            long horasRestantes = ChronoUnit.HOURS.between(ahora, reserva.getFechaHora());

            if (horasRestantes <= 48 && horasRestantes > 1) {
                System.out.println("🔔 Recordatorio: Faltan menos de 2 días para tu reserva del salón " 
                                   + reserva.getSalon().getNombre());
            } else if (horasRestantes <= 1 && horasRestantes >= 0) {
                System.out.println("⚠️ Aviso: Falta 1 hora o menos para tu reserva del salón " 
                                   + reserva.getSalon().getNombre());
            }
        }
    }
}
