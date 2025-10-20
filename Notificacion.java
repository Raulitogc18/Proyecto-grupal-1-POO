import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class Notificacion {

    public static void verificarReservas(List<reserva> reservas) {
        if (reservas == null || reservas.isEmpty()) return;

        LocalDateTime ahora = LocalDateTime.now();

        for (reserva reserva : reservas) {
            if (reserva == null || reserva.getHorario() == null || reserva.getHorario().getHoraStart() == null)
                continue;

            Date fechaInicio = reserva.getHorario().getHoraStart();
            LocalDateTime inicio = LocalDateTime.ofInstant(fechaInicio.toInstant(), ZoneId.systemDefault());

            long horasRestantes = ChronoUnit.HOURS.between(ahora, inicio);

            if (horasRestantes <= 48 && horasRestantes > 1) {
                System.out.println("🔔 Recordatorio: Faltan menos de 48 horas para tu reserva del salón "
                        + reserva.getSalon().getNombre());
            } else if (horasRestantes <= 1 && horasRestantes >= 0) {
                System.out.println("⚠️ Aviso: Falta 1 hora o menos para tu reserva del salón "
                        + reserva.getSalon().getNombre());
            }
        }
    }
}
