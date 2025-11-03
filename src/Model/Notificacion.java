package Model;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

public class Notificacion {

    private static final ReservaDAO dao = new ReservaDAO();
    private static final Set<Integer> notifiedGeneric = ConcurrentHashMap.newKeySet();
    private static ScheduledExecutorService scheduler;
    private static TrayIcon trayIcon;

    public static ScheduledExecutorService startScheduler(List<Reserva> reservas, int umbralMinutos, int checkIntervalSegundos) {
        stopScheduler();
        final List<Reserva> lista = (reservas == null) ? Collections.synchronizedList(new ArrayList<>()) : reservas;

        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "Notificacion-Scheduler");
            t.setDaemon(true);
            return t;
        });

        System.out.println("Notificacion: scheduler iniciado. umbralMinutos=" + umbralMinutos + " checkIntervalSeg=" + checkIntervalSegundos + " reservas=" + lista.size());

        Runnable task = () -> verificarReservas(lista, umbralMinutos);
        scheduler.scheduleAtFixedRate(task, 0, Math.max(1, checkIntervalSegundos), TimeUnit.SECONDS);
        return scheduler;
    }

    public static void stopScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            scheduler = null;
        }
        removeTrayIcon();
    }

    private static void verificarReservas(List<Reserva> reservas, int umbralMinutos) {
        if (reservas == null || reservas.isEmpty()) return;

        LocalDateTime ahora = LocalDateTime.now();
        synchronized (reservas) {
            for (Reserva r : reservas) {
                try {
                    if (r == null || r.getHorario() == null || r.getHorario().getHoraStart() == null) continue;

                    LocalDateTime inicio = LocalDateTime.ofInstant(r.getHorario().getHoraStart().toInstant(), ZoneId.systemDefault());
                    long minutosRestantes = ChronoUnit.MINUTES.between(ahora, inicio);

                    System.out.println("Notificacion: Reserva#" + r.getId() + " - minutos restantes: " + minutosRestantes);

                    if (minutosRestantes < 0) continue;

                    boolean not2 = safeIsNotificado2Dias(r);
                    boolean not1 = safeIsNotificado1Hora(r);

                    if (!not2 && minutosRestantes <= 2880 && minutosRestantes > 60) {
                        String msg = String.format("Faltan %d minutos para la reserva del salón %s", minutosRestantes,
                                r.getSalon() != null ? r.getSalon().getNombre() : "N/A");
                        doNotify("Reserva en 2 días", msg);
                        safeSetNotificado2Dias(r, true);
                        safeMarcarNotificadoEnBD(r.getId(), true, false);
                    }

                    if (!not1 && minutosRestantes <= 60 && minutosRestantes >= 0) {
                        String msg = String.format("Faltan %d minutos para la reserva del salón %s", minutosRestantes,
                                r.getSalon() != null ? r.getSalon().getNombre() : "N/A");
                        doNotify("Reserva en 1 hora", msg);
                        safeSetNotificado1Hora(r, true);
                        safeMarcarNotificadoEnBD(r.getId(), false, true);
                    }

                    if (umbralMinutos > 0 && minutosRestantes <= umbralMinutos) {
                        if (!notifiedGeneric.contains(r.getId())) {
                            String msg = String.format("Faltan %d minutos para la reserva del salón %s", minutosRestantes,
                                    r.getSalon() != null ? r.getSalon().getNombre() : "N/A");
                            doNotify("Recordatorio de reserva", msg);
                            notifiedGeneric.add(r.getId());
                        }
                    }

                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    private static void ensureTrayIcon() {
        if (!SystemTray.isSupported()) return;
        if (trayIcon != null) return;

        try {
            EventQueue.invokeAndWait(() -> {
                try {
                    SystemTray tray = SystemTray.getSystemTray();
                    Image image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                    TrayIcon ti = new TrayIcon(image, "Notificaciones");
                    ti.setImageAutoSize(true);
                    tray.add(ti);
                    trayIcon = ti;
                    System.out.println("TrayIcon creado exitosamente.");
                } catch (Throwable e) {
                    System.err.println("No se pudo crear TrayIcon: " + e.getMessage());
                    trayIcon = null;
                }
            });
        } catch (Throwable t) {
            System.err.println("invokeAndWait falló: " + t.getMessage());
            trayIcon = null;
        }
    }

    private static void doNotify(String titulo, String mensaje) {
        ensureTrayIcon();
        System.out.println("Notificacion: " + titulo + " - " + mensaje);

        Runnable showMsg = () -> {
            try {
                if (trayIcon != null) {
                    trayIcon.displayMessage(titulo, mensaje, MessageType.INFO);
                } else {
                    JOptionPane.showMessageDialog(null, titulo + "\n" + mensaje);
                }
            } catch (Throwable t) {
                System.err.println("doNotify fallo: " + t.getMessage());
                JOptionPane.showMessageDialog(null, titulo + "\n" + mensaje);
            }
        };

        EventQueue.invokeLater(showMsg);
    }

    private static boolean safeIsNotificado2Dias(Reserva r) {
        try { return r.isNotificado2Dias(); } catch (Throwable ignore) {}
        try { return r.isNotificado2dias(); } catch (Throwable ignore) {}
        return false;
    }

    private static boolean safeIsNotificado1Hora(Reserva r) {
        try { return r.isNotificado1Hora(); } catch (Throwable ignore) {}
        try { return r.isNotificado1hora(); } catch (Throwable ignore) {}
        return false;
    }

    private static void safeSetNotificado2Dias(Reserva r, boolean v) {
        try { r.setNotificado2Dias(v); return; } catch (Throwable ignore) {}
        try { r.setNotificado2dias(v); return; } catch (Throwable ignore) {}
    }

    private static void safeSetNotificado1Hora(Reserva r, boolean v) {
        try { r.setNotificado1Hora(v); return; } catch (Throwable ignore) {}
        try { r.setNotificado1hora(v); return; } catch (Throwable ignore) {}
    }

    private static void safeMarcarNotificadoEnBD(int reservaId, boolean dosDias, boolean unaHora) {
        try {
            dao.marcarNotificado(reservaId, dosDias, unaHora);
        } catch (Throwable t) {
            System.err.println("Error marcando notificado en BD: " + t.getMessage());
        }
    }

    public static void removeTrayIcon() {
        try {
            if (trayIcon != null && SystemTray.isSupported()) {
                SystemTray.getSystemTray().remove(trayIcon);
                trayIcon = null;
            }
        } catch (Throwable ignored) {}
    }
}
