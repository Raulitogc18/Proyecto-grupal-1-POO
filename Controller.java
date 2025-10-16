import java.util.*;

public class Controller {
    // Listas estáticas para que todos los métodos estáticos puedan usarlas
    private static List<Usuario> usuarios = new ArrayList<>();
    private static List<Salon> salones = new ArrayList<>();
    private static List<Reserva> reservas = new ArrayList<>();

    // 🔹 Bloque estático para precargar algunos salones
    static {
        salones.add(new Salon("Edificio A - Sala 101", 30));
        salones.add(new Salon("Edificio B - Laboratorio 202", 25));
        salones.add(new Salon("Edificio C - Auditorio", 100));
    }

    // Registrar usuario
    public static void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    // Registrar salón (opcional, si quieres agregar más dinámicamente)
    public static void registrarSalon(Salon salon) {
        salones.add(salon);
    }

    // Crear reserva
    public static Reserva crearReserva(Usuario usuario, Salon salon, Horario horario) {
        Reserva r = new Reserva(usuario, salon, horario);
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
    public static List<Salon> getSalones() {
        return salones;
    }

    // Obtener salones disponibles según un horario
    public static List<Salon> getSalonesDisp(Horario horario) {
        List<Salon> disponibles = new ArrayList<>();
        for (Salon s : salones) {
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

    public static List<Reserva> getReservas() {
        return reservas;
    }
}
