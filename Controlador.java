import java.util.*;

public class Controlador {
    private List<usuario> usuarios;
    private List<salon> salones;
    private List<reserva> reservas;
    private usuario usuarioActual;

    public Controlador() {
        this.usuarios = new ArrayList<>();
        this.salones = new ArrayList<>();
        this.reservas = new ArrayList<>();
        inicializarSalones();
    }

    private void inicializarSalones() {
        salon s1 = new salon("Biblioteca Central", "25");
        s1.setId(1);
        s1.setNombre("Sala de Estudio Grupal A");

        salon s2 = new salon("Biblioteca Central", "15");
        s2.setId(2);
        s2.setNombre("Sala de Estudio Individual B");

        salon s3 = new salon("Biblioteca Central", "30");
        s3.setId(3);
        s3.setNombre("Sala de Investigación C");

        salon s4 = new salon("Edificio A", "50");
        s4.setId(4);
        s4.setNombre("Salón 101");

        salones.add(s1);
        salones.add(s2);
        salones.add(s3);
        salones.add(s4);
    }

    // Devuelve salones disponibles según la lógica del modelo (actualmente todos)
    public List<salon> getSalonesDisp(horario horario) {
        List<salon> salonesDisponibles = new ArrayList<>();
        for (salon s : salones) {
            if (s.disponibilidad(horario)) {
                salonesDisponibles.add(s);
            }
        }
        return salonesDisponibles;
    }

    public List<salon> getSalonesBibliotecaDisp(horario horario) {
        List<salon> salonesBiblioteca = new ArrayList<>();
        for (salon s : salones) {
            if (s.getUbicacion() != null
                    && s.getUbicacion().toLowerCase().contains("biblioteca")
                    && s.disponibilidad(horario)) {
                salonesBiblioteca.add(s);
            }
        }
        return salonesBiblioteca;
    }

    // Crear reserva (ya no requiere sesión)
    public reserva crearreserva(usuario usuario, salon salon, horario horario) {
        if (usuario == null) {
            usuario = this.usuarioActual;
        }
        if (usuario == null) {
            usuario = new usuario(0, "Invitado");
        }
        registrarUsuario(usuario);
        reserva r = new reserva(usuario, salon, horario);
        reservas.add(r);
        return r;
    }

    // Eliminar reserva por objeto
    public boolean eliminarReserva(reserva r) {
        if (r == null) return false;
        return reservas.remove(r);
    }

    // Eliminar reserva por id
    public boolean eliminarReservaById(int id) {
        for (int i = 0; i < reservas.size(); i++) {
            if (reservas.get(i).getId() == id) {
                reservas.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<reserva> getReservas() {
        return new ArrayList<>(reservas);
    }

    // Permite registrar un usuario en la lista (si no existe)
    public void registrarUsuario(usuario u) {
        if (u == null) return;
        boolean existe = usuarios.stream()
                .anyMatch(x -> x.getCorreo() != null && x.getCorreo().equalsIgnoreCase(u.getCorreo()));
        if (!existe) usuarios.add(u);
    }

    public void setUsuarioActual(usuario u) {
        if (u == null) return;
        this.usuarioActual = u;
        registrarUsuario(u);
    }

    // Intenta autenticar usando AuthController y, si OK, establece usuarioActual
    public boolean autenticarYSetUsuario(String correo, String password) {
        if (!AuthController.autenticar(correo, password)) return false;
        usuario u = null;
        for (usuario x : usuarios) {
            if (correo.equalsIgnoreCase(x.getCorreo())) {
                u = x;
                break;
            }
        }
        if (u == null) {
            u = new usuario(0, correo);
            u.setCorreo(correo);
            u.setContrasena(password);
            registrarUsuario(u);
        }
        u.login(correo, password);
        setUsuarioActual(u);
        return true;
    }
}