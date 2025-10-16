package controller;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import modelos.Horario;
import modelos.Usuario;
import modelos.salon;
import modelos.reserva;
import java.util.Scanner;

public class Controlador {
    private List<Usuario> usuarios;
    private List<salon> salones;
    private List<reserva> reservas;
    private Usuario usuarioActual;
    private Scanner scanner;
    
    public Controlador() {
        this.usuarios = new ArrayList<>();
        this.salones = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        inicializarSalones();
    }
    
    private void inicializarSalones() {
        // Salones de biblioteca
        salon salon1 = new Salon("Biblioteca Central", "25");
        salon1.setId(1);
        salon1.setNombre("Sala de Estudio Grupal A");
        
        salon salon2 = new Salon("Biblioteca Central", "15");
        salon2.setId(2);
        salon2.setNombre("Sala de Estudio Individual B");
        
        salon salon3 = new Salon("Biblioteca Central", "30");
        salon3.setId(3);
        salon3.setNombre("Sala de Investigación C");
        
        // Otros salones
        salon salon4 = new Salon("Edificio A", "50");
        salon4.setId(4);
        salon4.setNombre("Salón 101");
        
        salones.add(salon1);
        salones.add(salon2);
        salones.add(salon3);
        salones.add(salon4);
    }
    
    public void iniciarSistema() {
        System.out.println("🎓 SISTEMA DE RESERVAS UVG 🎓");
        
        while (true) {
            if (usuarioActual == null) {
                mostrarMenuLogin();
            } else {
                mostrarMenuPrincipal();
            }
        }
    }
    
    private void mostrarMenuLogin() {
        System.out.println("\n=== INICIO DE SESIÓN ===");
        System.out.print("Correo UVG: ");
        String correo = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contraseña = scanner.nextLine();
        
        // Crear usuario temporal para login
        Usuario usuario = new Usuario(0, "Usuario UVG");
        if (usuario.login(correo, contraseña)) {
            usuarioActual = usuario;
            usuarios.add(usuario);
        }
    }
    
    private void mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("Usuario: " + usuarioActual.getCorreo());
        System.out.println("1. 📚 Ver salones de biblioteca disponibles");
        System.out.println("2. 🏢 Ver todos los salones disponibles");
        System.out.println("3. 📅 Hacer reserva");
        System.out.println("4. 📋 Ver mis reservas");
        System.out.println("5. 🚪 Cerrar sesión");
        System.out.print("Seleccione una opción: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        
        switch (opcion) {
            case 1:
                verSalonesBiblioteca();
                break;
            case 2:
                verTodosLosSalones();
                break;
            case 3:
                hacerReserva();
                break;
            case 4:
                verMisReservas();
                break;
            case 5:
                usuarioActual.logOut();
                usuarioActual = null;
                break;
            default:
                System.out.println("❌ Opción inválida");
        }
    }
    
    private void verSalonesBiblioteca() {
        System.out.println("\n=== SALONES DE BIBLIOTECA DISPONIBLES ===");
        
        // Crear un horario de ejemplo para hoy
        Horario horario = new Horario(new java.util.Date(), new java.util.Date());
        
        List<salon> salonesBiblioteca = getSalonesBibliotecaDisp(horario);
        
        if (salonesBiblioteca.isEmpty()) {
            System.out.println("No hay salones de biblioteca disponibles en este horario.");
        } else {
            for (salon salon : salonesBiblioteca) {
                System.out.println("🏛️ " + salon.getNombre());
                System.out.println("   📍 Ubicación: " + salon.getUbicacion());
                System.out.println("   👥 Capacidad: " + salon.getCapacidad() + " personas");
                System.out.println("   ✅ Disponible");
                System.out.println("---------------------------");
            }
        }
    }
    
    private void verTodosLosSalones() {
        System.out.println("\n=== TODOS LOS SALONES DISPONIBLES ===");
        
        Horario horario = new Horario(new java.util.Date(), new java.util.Date());
        List<salon> todosSalones = getSalonesDisp(horario);
        
        for (salon salon : todosSalones) {
            String icono = salon.getUbicacion().contains("Biblioteca") ? "🏛️" : "🏢";
            System.out.println(icono + " " + salon.getNombre());
            System.out.println("   📍 Ubicación: " + salon.getUbicacion());
            System.out.println("   👥 Capacidad: " + salon.getCapacidad() + " personas");
            System.out.println("---------------------------");
        }
    }
    
    private void hacerReserva() {
        System.out.println("\n=== NUEVA RESERVA ===");
        
        // Mostrar salones disponibles
        verTodosLosSalones();
        
        System.out.print("Ingrese el ID del salón: ");
        int idSalon = scanner.nextInt();
        scanner.nextLine();
        
        salon salonSeleccionado = null;
        for (salon salon : salones) {
            if (salon.getId() == idSalon) {
                salonSeleccionado = salon;
                break;
            }
        }
        
        if (salonSeleccionado == null) {
            System.out.println("❌ Salón no encontrado");
            return;
        }
        
        // Crear horario (en sistema real se pediría fecha y hora)
        Horario horario = new Horario(new java.util.Date(), new java.util.Date());
        
        reserva reserva = crearReserva(usuarioActual, salonSeleccionado, horario);
        if (reserva != null) {
            System.out.println("✅ Reserva creada exitosamente!");
            System.out.println("📅 ID de reserva: " + reserva.getId());
            System.out.println("🏛️ Salón: " + reserva.getSalon().getNombre());
        }
    }
    
    private void verMisReservas() {
        System.out.println("\n=== MIS RESERVAS ===");
        
        List<reserva> misReservas = new ArrayList<>();
        for (reserva reserva : reservas) {
            if (reserva.getUsuario().getCorreo().equals(usuarioActual.getCorreo())) {
                misReservas.add(reserva);
            }
        }
        
        if (misReservas.isEmpty()) {
            System.out.println("No tienes reservas activas.");
        } else {
            for (reserva reserva : misReservas) {
                System.out.println("📋 Reserva #" + reserva.getId());
                System.out.println("   🏛️ Salón: " + reserva.getSalon().getNombre());
                System.out.println("   📍 Ubicación: " + reserva.getSalon().getUbicacion());
                System.out.println("---------------------------");
            }
        }
    }
    
    public List<salon> getSalonesDisp(Horario horario) {
        List<salon> salonesDisponibles = new ArrayList<>();
        for (salon salon : salones) {
            if (salon.disponibilidad(horario)) {
                salonesDisponibles.add(salon);
            }
        }
        return salonesDisponibles;
    }
    
    public ListsSalon> getSalonesBibliotecaDisp(Horario horario) {
        List<salon> salonesBiblioteca = new ArrayList<>();
        for (salon salon : salones) {
            if (salon.getUbicacion().toLowerCase().contains("biblioteca") && 
                salon.disponibilidad(horario)) {
                salonesBiblioteca.add(salon);
            }
        }
        return salonesBiblioteca;
    }
    
    public reserva crearReserva(Usuario usuario, salon salon, Horario horario) {
        if (!usuario.isLoggedIn()) {
            System.out.println("❌ Debes iniciar sesión para hacer reservas");
            return null;
        }
        
        reserva reserva = new Reserva(usuario, salon);
        reserva.setHorario(horario);
        reservas.add(reserva);
        
        return reserva;
    }
    
    public List<reserva> getReservas() {
        return new ArrayList<>(reservas);
    }
}