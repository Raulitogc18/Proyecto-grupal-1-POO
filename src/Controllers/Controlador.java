package Controllers;

import Model.Horario;
import Model.Reserva;
import Model.ReservaDAO;
import Model.Salon;
import Model.Usuario;
import java.util.*;

public class Controlador {
    private final List<Usuario> usuarios;
    private final List<Salon> salones;
    private final List<Reserva> reservas;
    private Usuario usuarioActual;
    private final ReservaDAO reservaDAO;

    public Controlador() {
        this.usuarios = Collections.synchronizedList(new ArrayList<>());
        this.salones = new ArrayList<>();
        this.reservas = Collections.synchronizedList(new ArrayList<>());
        this.reservaDAO = new ReservaDAO();

        inicializarSalones();
        cargarReservasDesdeBD(); // ⚙️ carga segura desde BD
    }

    private void cargarReservasDesdeBD() {
        try {
            List<Reserva> desdeBD = reservaDAO.listarTodas();
            reservas.clear();

            if (desdeBD != null) {
                for (Reserva r : desdeBD) {
                    if (r != null && r.getUsuario() != null && r.getSalon() != null) {
                        reservas.add(r);
                        registrarUsuario(r.getUsuario()); // asegura que el usuario quede en memoria
                    }
                }
            }

            System.out.println("Reservas cargadas: " + reservas.size());
        } catch (Exception e) {
            System.err.println("⚠️ Error cargando reservas desde BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void inicializarSalones() {
        Salon s1 = new Salon("Biblioteca Central", "25");
        s1.setId(1);
        s1.setNombre("Sala de Estudio Grupal A");

        Salon s2 = new Salon("Biblioteca Central", "15");
        s2.setId(2);
        s2.setNombre("Sala de Estudio Individual B");

        Salon s3 = new Salon("Biblioteca Central", "30");
        s3.setId(3);
        s3.setNombre("Sala de Investigación C");

        Salon s4 = new Salon("Edificio A", "50");
        s4.setId(4);
        s4.setNombre("Salón 101");

        salones.addAll(Arrays.asList(s1, s2, s3, s4));
    }

    public List<Salon> getSalonesDisp(Horario horario) {
        List<Salon> disponibles = new ArrayList<>();
        for (Salon s : salones) {
            if (s.disponibilidad(horario)) {
                disponibles.add(s);
            }
        }
        return disponibles;
    }

    public List<Salon> getSalonesBibliotecaDisp(Horario horario) {
        List<Salon> salonesBiblioteca = new ArrayList<>();
        for (Salon s : salones) {
            if (s.getUbicacion() != null
                    && s.getUbicacion().toLowerCase().contains("biblioteca")
                    && s.disponibilidad(horario)) {
                salonesBiblioteca.add(s);
            }
        }
        return salonesBiblioteca;
    }

    public Reserva crearReserva(Usuario usuario, Salon salon, Horario horario) {
        if (usuario == null || salon == null) return null;

        registrarUsuario(usuario);

        Integer idEnBD = AuthController.obtenerIdPorCorreo(usuario.getCorreo());
        if (idEnBD == null) {
            try {
                AuthController.registrarCuenta(
                    usuario.getNombre() != null ? usuario.getNombre() : "Usuario",
                    String.valueOf(usuario.getId()),
                    usuario.getCorreo(),
                    ""
                );
            } catch (Exception ignore) {}
            idEnBD = AuthController.obtenerIdPorCorreo(usuario.getCorreo());
        }
        if (idEnBD != null) usuario.setId(idEnBD);

        Reserva r = new Reserva(usuario, salon, horario);
        try {
            if (reservaDAO.insertar(r)) {
                reservas.add(r);
                return r;
            }
        } catch (Exception e) {
            System.err.println("Error insertando reserva: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean eliminarReserva(Reserva r) {
        if (r == null) return false;
        return reservas.remove(r);
    }

    public boolean eliminarReservaById(int id) {
        return reservas.removeIf(res -> res.getId() == id);
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void registrarUsuario(Usuario u) {
        if (u == null) return;
        boolean existe = usuarios.stream()
                .anyMatch(x -> x.getCorreo() != null && x.getCorreo().equalsIgnoreCase(u.getCorreo()));
        if (!existe) usuarios.add(u);
    }

    public void setUsuarioActual(Usuario u) {
        if (u == null) return;
        this.usuarioActual = u;
        registrarUsuario(u);
    }

    public boolean autenticarYSetUsuario(String correo, String password) {
        if (!AuthController.autenticar(correo, password)) return false;
        Usuario u = usuarios.stream()
                .filter(x -> correo.equalsIgnoreCase(x.getCorreo()))
                .findFirst()
                .orElseGet(() -> {
                    Usuario nuevo = new Usuario(0, correo);
                    nuevo.setCorreo(correo);
                    nuevo.setContrasena(password);
                    registrarUsuario(nuevo);
                    return nuevo;
                });
        u.login(correo, password);
        setUsuarioActual(u);
        return true;
    }
}
