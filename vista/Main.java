package vista;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import controller.BiblioController;
import modelos.Usuario;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BiblioController controlador = new BiblioController();
        controlador.iniciarNotificaciones();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Usuario usuarioActivo = null;

        while (true) {
            System.out.println("\n===== 🎓 Sistema de Reservas UVG =====");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Ver salones disponibles");
            System.out.println("3. Hacer reserva");
            System.out.println("4. Cancelar reserva");
            System.out.println("5. Salir");
            System.out.print("Opción: ");
            int opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> {
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Carnet: ");
                    String carnet = sc.nextLine();
                    System.out.print("Correo institucional: ");
                    String correo = sc.nextLine();
                    Usuario usuario = new Usuario(nombre, carnet, correo);
                    if (controlador.autenticarUsuario(usuario)) {
                        usuarioActivo = usuario;
                    }
                }
                case 2 -> controlador.mostrarSalones();
                case 3 -> {
                    if (usuarioActivo == null) {
                        System.out.println("⚠️ Debes iniciar sesión primero.");
                        break;
                    }
                    controlador.mostrarSalones();
                    System.out.print("Selecciona salón (número): ");
                    int salon = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Confirma tu carnet: ");
                    String carnetConf = sc.nextLine();
                    System.out.print("Fecha y hora (dd/MM/yyyy HH:mm): ");
                    String fechaTexto = sc.nextLine();
                    try {
                        LocalDateTime fecha = LocalDateTime.parse(fechaTexto, formato);
                        controlador.reservarSalon(usuarioActivo, salon, fecha, carnetConf);
                    } catch (Exception e) {
                        System.out.println("❌ Fecha inválida.");
                    }
                }
                case 4 -> {
                    if (usuarioActivo == null) {
                        System.out.println("⚠️ Debes iniciar sesión primero.");
                        break;
                    }
                    System.out.print("Nombre del salón a cancelar: ");
                    String salon = sc.nextLine();
                    controlador.cancelarReserva(usuarioActivo, salon);
                }
                case 5 -> {
                    System.out.println("👋 Cerrando sistema...");
                    sc.close();
                    return;
                }
                default -> System.out.println("❌ Opción no válida.");
            }
        }
    }
}
