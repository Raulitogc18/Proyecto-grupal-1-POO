import java.util.*;

public class Controlador {
    private List<usuario> usuarios;
    private List<salon> salones;
    private List<reserva> reservas;
    private usuario usuarioActual;
    private Scanner scanner;

    public Controlador() {
        this.usuarios = new ArrayList<>();
        this.salones = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.scanner = new Scanner(System.in);
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
        String contrasena = scanner.nextLine();

        usuario utemp = new usuario(0, "Usuario UVG");
        if (utemp.login(correo, contrasena)) {
            usuarioActual = utemp;
            usuarios.add(utemp);
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("Usuario: " + (usuarioActual != null ? usuarioActual.getCorreo() : "N/A"));
        System.out.println("1. 📚 Ver salones de biblioteca disponibles");
        System.out.println("2. 🏢 Ver todos los salones disponibles");
        System.out.println("3. 📅 Hacer reserva");
        System.out.println("4. 📋 Ver mis reservas");
        System.out.println("5. 🚪 Cerrar sesión");
        System.out.print("Seleccione una opción: ");

        int opcion = -1;
        try {
            opcion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return;
        }

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
                if (usuarioActual != null) usuarioActual.logOut();
                usuarioActual = null;
                break;
            default:
                System.out.println("❌ Opción inválida");
        }
    }

    private void verSalonesBiblioteca() {
        System.out.println("\n=== SALONES DE BIBLIOTECA DISPONIBLES ===");
        horario horarioAhora = new horario(new java.util.Date(), new java.util.Date());
        List<salon> salonesBiblioteca = getSalonesBibliotecaDisp(horarioAhora);

        if (salonesBiblioteca.isEmpty()) {
            System.out.println("No hay salones de biblioteca disponibles en este horario.");
        } else {
            for (salon s : salonesBiblioteca) {
                System.out.println("🏛️ " + s.getNombre());
                System.out.println("   📍 Ubicación: " + s.getUbicacion());
                System.out.println("   👥 Capacidad: " + s.getCapacidad() + " personas");
                System.out.println("   ✅ Disponible");
                System.out.println("---------------------------");
            }
        }
    }

    private void verTodosLosSalones() {
        System.out.println("\n=== TODOS LOS SALONES DISPONIBLES ===");
        horario horarioAhora = new horario(new java.util.Date(), new java.util.Date());
        List<salon> todosSalones = getSalonesDisp(horarioAhora);

        for (salon s : todosSalones) {
            String icono = s.getUbicacion() != null && s.getUbicacion().toLowerCase().contains("biblioteca") ? "🏛️" : "🏢";
            System.out.println(icono + " " + s.getNombre());
            System.out.println("   📍 Ubicación: " + s.getUbicacion());
            System.out.println("   👥 Capacidad: " + s.getCapacidad() + " personas");
            System.out.println("---------------------------");
        }
    }

    private void hacerReserva() {
        System.out.println("\n=== NUEVA RESERVA ===");
        verTodosLosSalones();

        System.out.print("Ingrese el ID del salón: ");
        int idSalon = -1;
        try {
            idSalon = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        salon salonSeleccionado = null;
        for (salon s : salones) {
            if (s.getId() == idSalon) {
                salonSeleccionado = s;
                break;
            }
        }

        if (salonSeleccionado == null) {
            System.out.println("❌ Salón no encontrado");
            return;
        }

        horario horarioReserva = new horario(new java.util.Date(), new java.util.Date());
        reserva r = crearreserva(usuarioActual, salonSeleccionado, horarioReserva);
        if (r != null) {
            System.out.println("✅ Reserva creada exitosamente!");
            System.out.println("📅 ID de reserva: " + r.getId());
            System.out.println("🏛️ Salón: " + r.getSalon().getNombre());
        }
    }

    private void verMisReservas() {
        System.out.println("\n=== MIS RESERVAS ===");
        List<reserva> misReservas = new ArrayList<>();
        for (reserva r : reservas) {
            if (r.getUsuario() != null && r.getUsuario().getCorreo() != null
                    && usuarioActual != null
                    && r.getUsuario().getCorreo().equals(usuarioActual.getCorreo())) {
                misReservas.add(r);
            }
        }

        if (misReservas.isEmpty()) {
            System.out.println("No tienes reservas activas.");
        } else {
            for (reserva r : misReservas) {
                System.out.println("📋 Reserva #" + r.getId());
                System.out.println("   🏛️ Salón: " + r.getSalon().getNombre());
                System.out.println("   📍 Ubicación: " + r.getSalon().getUbicacion());
                System.out.println("---------------------------");
            }
        }
    }

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

    // ahora método con clases en minúscula
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