import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;

public class VistaGUI extends JFrame {
    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public VistaGUI() {
        setTitle("Sistema de Reservas de Salones");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));

        JButton btnEstudiante = new JButton("Registrar Estudiante");
        JButton btnAdmin = new JButton("Registrar Administrador");
        JButton btnReserva = new JButton("Crear Reserva");
        JButton btnBuscar = new JButton("Buscar Usuario por Correo");
        JButton btnResumen = new JButton("Mostrar Reservas");
        JButton btnSalir = new JButton("Salir");

        panel.add(btnEstudiante);
        panel.add(btnAdmin);
        panel.add(btnReserva);
        panel.add(btnBuscar);
        panel.add(btnResumen);
        panel.add(btnSalir);

        add(panel);

        btnEstudiante.addActionListener(e -> registrarEstudiante());
        btnAdmin.addActionListener(e -> registrarAdministrador());
        btnReserva.addActionListener(e -> crearReserva());
        btnBuscar.addActionListener(e -> buscarUsuario());
        btnResumen.addActionListener(e -> mostrarReservas());
        btnSalir.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void registrarEstudiante() {
        String carnetStr = JOptionPane.showInputDialog(this, "Carnet:");
        String nombre = JOptionPane.showInputDialog(this, "Nombre:");
        String correo = JOptionPane.showInputDialog(this, "Correo:");

        if (carnetStr == null || nombre == null || correo == null ||
                carnetStr.trim().isEmpty() || nombre.trim().isEmpty() || correo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios!!!");
            return;
        }

        try {
            int carnet = Integer.parseInt(carnetStr.trim());
            Estudiante est = new Estudiante(carnet, nombre.trim());
            est.setCorreo(correo.trim());
            Controller.registrarUsuario(est);
            JOptionPane.showMessageDialog(this, "Estudiante registrado!!!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Carnet debe ser numérico");
        }
    }

    private void registrarAdministrador() {
        String carnetStr = JOptionPane.showInputDialog(this, "Carnet:");
        String nombre = JOptionPane.showInputDialog(this, "Nombre:");
        String correo = JOptionPane.showInputDialog(this, "Correo:");

        if (carnetStr == null || nombre == null || correo == null ||
                carnetStr.trim().isEmpty() || nombre.trim().isEmpty() || correo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios!!!");
            return;
        }

        try {
            int carnet = Integer.parseInt(carnetStr.trim());
            Administrador admin = new Administrador(carnet, nombre.trim());
            admin.setCorreo(correo.trim());
            Controller.registrarUsuario(admin);
            JOptionPane.showMessageDialog(this, "Administrador registrado!!!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Carnet debe ser numérico");
        }
    }

    private void crearReserva() {
        String correo = JOptionPane.showInputDialog(this, "Correo del usuario:");
        if (correo == null || correo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Correo es obligatorio!!!");
            return;
        }

        Usuario u = Controller.buscarUsuario(correo.trim());
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado");
            return;
        }

        List<Salon> salones = Controller.getSalones();
        if (salones.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay salones disponibles");
            return;
        }

        String[] nombresSalones = salones.stream().map(Salon::getNombre).toArray(String[]::new);
        String nombreSeleccionado = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona un salón:",
                "Salones disponibles",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombresSalones,
                nombresSalones[0]
        );

        if (nombreSeleccionado == null) return;

        Salon seleccionado = salones.stream()
                .filter(s -> s.getNombre().equals(nombreSeleccionado))
                .findFirst()
                .orElse(null);

        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Salón no encontrado");
            return;
        }

        try {
            String inicioStr = JOptionPane.showInputDialog(this, "Fecha y hora inicio (dd/MM/yyyy HH:mm):");
            String finStr = JOptionPane.showInputDialog(this, "Fecha y hora fin (dd/MM/yyyy HH:mm):");

            Date inicio = formato.parse(inicioStr.trim());
            Date fin = formato.parse(finStr.trim());

            SimpleDateFormat soloFecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat soloHora = new SimpleDateFormat("HH:mm");

            String fechaInicio = soloFecha.format(inicio);
            String horaInicio = soloHora.format(inicio);
            String fechaFin = soloFecha.format(fin);
            String horaFin = soloHora.format(fin);

            Horario horario = new Horario(inicio, fin);

            Controller.crearReserva(u, seleccionado, horario);
            JOptionPane.showMessageDialog(this, "Reserva creada para " + u.getNombre() +
                    "\nFecha inicio: " + fechaInicio + " Hora: " + horaInicio +
                    "\nFecha fin: " + fechaFin + " Hora: " + horaFin);

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto \nEjemplo: 07/09/2025 14:30");
        }
    }

    private void buscarUsuario() {
        String correo = JOptionPane.showInputDialog(this, "Correo:");
        if (correo == null || correo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Correo obligatorio!!!");
            return;
        }

        Usuario u = Controller.buscarUsuario(correo.trim());

        if (u != null) {
            JOptionPane.showMessageDialog(this, "Usuario encontrado: " + u.getNombre());
        } else {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado");
        }
    }

    private void mostrarReservas() {
        List<Reserva> reservas = Controller.getReservas();
        if (reservas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay reservas registradas");
            return;
        }

        StringBuilder sb = new StringBuilder("Reservas registradas:\n\n");
        SimpleDateFormat soloFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat soloHora = new SimpleDateFormat("HH:mm");

        for (Reserva r : reservas) {
            String fechaInicio = soloFecha.format(r.getHorario().getHoraStart());
            String horaInicio = soloHora.format(r.getHorario().getHoraStart());
            String fechaFin = soloFecha.format(r.getHorario().getHoraEnd());
            String horaFin = soloHora.format(r.getHorario().getHoraEnd());

            sb.append("Usuario: ").append(r.getUsuario().getNombre())
              .append(" | Correo: ").append(r.getUsuario().getCorreo())
              .append("\nSalón: ").append(r.getSalon().getNombre())
              .append(" (Ubicación: ").append(r.getSalon().getUbicacion()).append(")")
              .append("\nHorario: ").append(fechaInicio).append(" ").append(horaInicio)
              .append(" - ").append(fechaFin).append(" ").append(horaFin)
              .append("\n\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(this, scrollPane, "Reservas", JOptionPane.INFORMATION_MESSAGE);
    }
}
