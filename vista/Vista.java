import java.util.Date;
import java.util.List;

import modelos.salon;

import java.util.Calendar;

public class Vista {
    public static void main(String[] args) {
        // Crear controlador
        Controlador controlador = new Controlador();
        

        
        // Registrar usuarios (solo los UVG se registrarán)
        controlador.registrarUsuario(estudiante1);
        controlador.registrarUsuario(estudiante2); // Este no se registrará
        controlador.registrarUsuario(admin1);
        
        // Crear salones (incluyendo de biblioteca)
        salon salonBiblioteca1 = new salon("Biblioteca Central", "25");
        salonBiblioteca1.setNombre("Sala de Estudio A");
        salonBiblioteca1.setId(1);
        
        Salon salonBiblioteca2 = new Salon("Biblioteca Central", "15");
        salonBiblioteca2.setNombre("Sala de Estudio B");
        salonBiblioteca2.setId(2);
        
        Salon salonNormal = new Salon("Edificio A", "50");
        salonNormal.setNombre("Salón 101");
        salonNormal.setId(3);
        
        controlador.getSalones().add(salonBiblioteca1);
        controlador.getSalones().add(salonBiblioteca2);
        controlador.getSalones().add(salonNormal);
        
        // Crear horario para mañana
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_YEAR, 1); // Mañana
        Date fechaManana = calendario.getTime();
        
        calendario.set(Calendar.HOUR_OF_DAY, 10); // 10:00 AM
        Date horaStart = calendario.getTime();
        
        Horario horario1 = new Horario(fechaManana, horaStart);
        
        System.out.println("=== SISTEMA DE RESERVAS UVG ===");
        
        // Buscar usuario
        Usuario usuarioEncontrado = controlador.buscarUsuario("jperez@uvg.edu.gt");
        if (usuarioEncontrado != null) {
            System.out.println("Usuario UVG encontrado: " + usuarioEncontrado.getNombre());
        }
        
        // Obtener TODOS los salones disponibles
        List<Salon> salonesDisponibles = controlador.getSalonesDisp(horario1);
        System.out.println("\nTodos los salones disponibles: " + salonesDisponibles.size());
        
        // Obtener solo salones de la BIBLIOTECA disponibles
        List<Salon> salonesBiblioteca = controlador.getSalonesBibliotecaDisp(horario1);
        System.out.println("Salones de BIBLIOTECA disponibles: " + salonesBiblioteca.size());
        
        for (Salon salon : salonesBiblioteca) {
            System.out.println(" - " + salon.getNombre() + " (Capacidad: " + salon.getCapacidad() + ")");
        }
        
        // Crear reserva con usuario UVG
        Reserva reserva = controlador.crearReserva(estudiante1, salonBiblioteca1, horario1);
        if (reserva != null) {
            System.out.println("\n✅ Reserva creada exitosamente para: " + reserva.getUsuario().getNombre());
        }
        
        // Intentar crear reserva con usuario NO UVG
        reserva reservaInvalida = controlador.crearReserva(estudiante2, salonBiblioteca2, horario1);
        if (reservaInvalida == null) {
            System.out.println("❌ No se pudo crear reserva - Usuario no UVG");
        }
        
        // Mostrar reservas
        List<reserva> reservas = controlador.getReservas();
        System.out.println("\nTotal de reservas activas: " + reservas.size());
        
        // Mantener el programa ejecutándose para probar notificaciones
        try {
            System.out.println("\n⏰ Sistema de notificaciones activo...");
            Thread.sleep(120000); // Ejecutar por 2 minutos para pruebas
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        controlador.detenerTimer();
        System.out.println("Sistema cerrado.");
    }
}