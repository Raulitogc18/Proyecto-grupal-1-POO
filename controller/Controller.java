package controller;
import java.util.*;

import modelos.Horario;
import modelos.Usuario;
import modelos.reserva;
import modelos.salon;

public class Controller {
    // Listas estáticas para que todos los métodos estáticos puedan usarlas
    private static List<Usuario> usuarios = new ArrayList<>();
    private static List<salon> salones = new ArrayList<>();
    private static List<reserva> reservas = new ArrayList<>();

    // 🔹 Bloque estático para precargar algunos salones
    static {
        salones.add(new salon("Biblioteca - Sala 101", 30));
        salones.add(new salon("Biblioteca - Laboratorio 202", 25));
        salones.add(new salon("Biblioteca - Auditorio", 100));
    }

    // Registrar usuario
    public static void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    // Registrar salón (opcional, si quieres agregar más dinámicamente)
    public static void registrarSalon(salon salon) {
        salones.add(salon);
    }

    // Crear reserva
    public static reserva crearReserva(Usuario usuario, salon salon, Horario horario) {
        reserva r = new reserva(usuario, salon, horario);
        reservas.add(r);
        return r;
    }

    // Buscar usuario por correo
    public static Usuario buscarUsuario(String correo) {
        for (Usuario u : usuarios) {
            if (u.getCorreo() != null && u.getCorreo().equalsIgnoreCase(correo)) {
                return u;
            }
        }
        return null;
    }

    // Obtener lista de todos los salones
    public static List<salon> getSalones() {
        return salones;
    }

    // Obtener salones disponibles según un horario
    public static List<salon> getSalonesDisp(Horario horario) {
        List<salon> disponibles = new ArrayList<>();
        for (salon s : salones) {
            if (s.disponibilidad(horario)) {
                disponibles.add(s);
            }
        }
        return disponibles;
    }

    // Métodos opcionales para debug
    public static List<Usuario> getUsuarios() {
        return usuarios;
    }

    public static List<reserva> getReservas() {
        return reservas;
    }
}
