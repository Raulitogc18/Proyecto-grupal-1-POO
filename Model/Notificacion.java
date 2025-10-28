package Model;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Notificacion {
    private static final ReservaDAO dao = new ReservaDAO();
    // conjunto para evitar notificaciones repetidas para umbrales arbitrarios
    private static final Set<Integer> notifiedGeneric = ConcurrentHashMap.newKeySet();

    // Notifica automáticamente a 2 días (2880 min) y 1 hora (60 min)
    public static void verificarReservas(List<reserva> reservas, long umbralMinutos) {
        if (reservas == null || reservas.isEmpty()) {
            System.out.println("Notificacion: lista de reservas vacía o null");
            return;
        }

        LocalDateTime ahora = LocalDateTime.now();

        for (reserva r : reservas) {
            if (r == null || r.getHorario() == null || r.getHorario().getHoraStart() == null) continue;

            Date fechaInicio = r.getHorario().getHoraStart();
            LocalDateTime inicio = LocalDateTime.ofInstant(fechaInicio.toInstant(), ZoneId.systemDefault());

            long minutosRestantes = ChronoUnit.MINUTES.between(ahora, inicio);

            // log para depuración
            System.out.println("Notificacion: Reserva#" + r.getId() + " - minutos restantes: " + minutosRestantes);

            if (minutosRestantes < 0) continue; // ya pasó

            // 1 hora
            if (minutosRestantes <= 60 && !r.isNotificado1Hora()) {
                String titulo = "Reserva en 1 hora";
                String mensaje = String.format("Falta %d minutos para la reserva del salón %s", minutosRestantes,
                        r.getSalon() != null ? r.getSalon().getNombre() : "N/A");
                enviarNotificacion(titulo, mensaje);
                r.setNotificado1Hora(true);
                // después de setNotificado..., llamar:
                try { dao.actualizarFlags(r); } catch (Exception ex) { ex.printStackTrace(); }
                continue;
            }

            // 2 días
            if (minutosRestantes <= 2880 && !r.isNotificado2Dias()) {
                String titulo = "Reserva en 2 días";
                String mensaje = String.format("Faltan %d minutos para la reserva del salón %s", minutosRestantes,
                        r.getSalon() != null ? r.getSalon().getNombre() : "N/A");
                enviarNotificacion(titulo, mensaje);
                r.setNotificado2Dias(true);
                // después de setNotificado..., llamar:
                try { dao.actualizarFlags(r); } catch (Exception ex) { ex.printStackTrace(); }
                continue;
            }

            // Notificación genérica según umbral pasado (evitar duplicados con notifiedGeneric)
            if (umbralMinutos > 0 && minutosRestantes <= umbralMinutos) {
                if (!notifiedGeneric.contains(r.getId())) {
                    String titulo = "Recordatorio de reserva";
                    String mensaje = String.format("Faltan %d minutos para la reserva del salón %s", minutosRestantes,
                            r.getSalon() != null ? r.getSalon().getNombre() : "N/A");
                    enviarNotificacion(titulo, mensaje);
                    notifiedGeneric.add(r.getId());
                }
            }
        }
    }

    // Intenta usar la bandeja del sistema; si no está disponible hace println
    private static void enviarNotificacion(String titulo, String mensaje) {
        try {
            if (SystemTray.isSupported()) {
                EventQueue.invokeLater(() -> {
                    try {
                        SystemTray tray = SystemTray.getSystemTray();
                        Image image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
                        TrayIcon trayIcon = new TrayIcon(image, "Notificaciones");
                        trayIcon.setImageAutoSize(true);
                        tray.add(trayIcon);
                        trayIcon.displayMessage(titulo, mensaje, MessageType.INFO);
                        // remover el icono después de unos segundos
                        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                            try { tray.remove(trayIcon); } catch (Exception ignored) {}
                        }, 4, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        System.out.println(titulo + " - " + mensaje + " (fallback)");
                    }
                });
            } else {
                System.out.println(titulo + " - " + mensaje);
            }
        } catch (Exception e) {
            System.out.println(titulo + " - " + mensaje);
        }
    }

    // Inicia un scheduler que ejecuta verificarReservas cada checkIntervalSegundos segundos
    // firma compatible con tu Main actual
    public static ScheduledExecutorService startScheduler(List<reserva> reservas, long umbralMinutos, long checkIntervalSegundos) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> verificarReservas(reservas, umbralMinutos);
        scheduler.scheduleAtFixedRate(task, 0, checkIntervalSegundos, TimeUnit.SECONDS);
        return scheduler;
    }
}
