package Vistas;

import Controllers.Controlador;
import Model.CatalogoRecursos;
import Model.Horario;
import Model.Reserva;
import Model.Salon;
import Model.Usuario;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;

public class ProgramaGUI extends JFrame {

    private final Controlador controller;
    private final DefaultListModel<String> salonesModel = new DefaultListModel<>();
    private final DefaultListModel<String> reservasModel = new DefaultListModel<>();
    private final List<Salon> salonesCache = new ArrayList<>();

    private JList<String> listaSalones;
    private JList<String> listaReservas;
    private JTextField txtUsuarioCorreo;
    private JTextField txtUsuarioNombre;
    private JTextField txtUsuarioCarnet;

    private final SimpleDateFormat sdfFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public ProgramaGUI(Controlador controller) {
        this.controller = controller;
        setTitle("Sistema de Reservas - Programa");
        setSize(800, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        refreshSalones();
        refreshReservas();
        setVisible(true);
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(8, 8));

        // --- Panel Izquierdo: Salones ---
        JPanel pSalones = new JPanel(new BorderLayout(6, 6));
        pSalones.setBorder(BorderFactory.createTitledBorder("Salones disponibles"));
        listaSalones = new JList<>(salonesModel);
        listaSalones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pSalones.add(new JScrollPane(listaSalones), BorderLayout.CENTER);

        JButton btnRefrescarSalones = new JButton("Refrescar salones");
        btnRefrescarSalones.addActionListener(e -> refreshSalones());
        pSalones.add(btnRefrescarSalones, BorderLayout.SOUTH);

        // --- Panel Derecho: Reservas ---
        JPanel pReservas = new JPanel(new BorderLayout(6, 6));
        pReservas.setBorder(BorderFactory.createTitledBorder("Reservas actuales"));
        listaReservas = new JList<>(reservasModel);
        listaReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pReservas.add(new JScrollPane(listaReservas), BorderLayout.CENTER);

        JPanel pReservasSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        JButton btnRefrescarReservas = new JButton("Refrescar reservas");
        btnRefrescarReservas.addActionListener(e -> refreshReservas());
        pReservasSouth.add(btnRefrescarReservas);

        JButton btnCatalogo = new JButton("Catálogo de recursos");
        btnCatalogo.addActionListener(e -> {
            CatalogoRecursos dialog = new CatalogoRecursos(this);
            dialog.setVisible(true);
        });
        pReservasSouth.add(btnCatalogo);

        JButton btnEliminarReserva = new JButton("Eliminar reserva");
        btnEliminarReserva.addActionListener(e -> accionEliminarReserva());
        pReservasSouth.add(btnEliminarReserva);

        JButton btnFinalizarSalir = new JButton("Finalizar y salir");
        btnFinalizarSalir.addActionListener(e -> {
            int opt = JOptionPane.showConfirmDialog(this,
                    "¿Desea finalizar y salir de la aplicación?",
                    "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
        });
        pReservasSouth.add(btnFinalizarSalir);

        pReservas.add(pReservasSouth, BorderLayout.SOUTH);

        // --- Panel Inferior: Formulario para crear reserva ---
        JPanel pForm = new JPanel(new GridLayout(2, 1, 6, 6));
        pForm.setBorder(BorderFactory.createTitledBorder("Crear nueva reserva"));

        JPanel pInputs = new JPanel(new GridLayout(3, 2, 6, 6));
        pInputs.add(new JLabel("Correo usuario:"));
        txtUsuarioCorreo = new JTextField();
        pInputs.add(txtUsuarioCorreo);

        pInputs.add(new JLabel("Nombre usuario:"));
        txtUsuarioNombre = new JTextField();
        pInputs.add(txtUsuarioNombre);

        pInputs.add(new JLabel("Carnet (número):"));
        txtUsuarioCarnet = new JTextField();
        pInputs.add(txtUsuarioCarnet);

        JPanel pActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnReservar = new JButton("Crear reserva");
        btnReservar.addActionListener(e -> accionCrearReserva());
        pActions.add(btnReservar);

        pForm.add(pInputs);
        pForm.add(pActions);

        // --- Distribución principal ---
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pSalones, pReservas);
        split.setResizeWeight(0.6);

        main.add(split, BorderLayout.CENTER);
        main.add(pForm, BorderLayout.SOUTH);

        add(main);
    }

    private void refreshSalones() {
        salonesModel.clear();
        salonesCache.clear();
        try {
            Horario nowHorario = new Horario(new Date(), new Date());
            List<Salon> salones = controller.getSalonesDisp(nowHorario);
            if (salones == null || salones.isEmpty()) {
                salonesModel.addElement("(no hay salones disponibles)");
                return;
            }
            for (Salon s : salones) {
                salonesCache.add(s);
                salonesModel.addElement(String.format(
                        "ID:%d  %s  (%s)  Capacidad: %s",
                        s.getId(), s.getNombre(), s.getUbicacion(), s.getCapacidad()
                ));
            }
        } catch (Exception ex) {
            salonesModel.addElement("(error al cargar salones: " + ex.getMessage() + ")");
        }
    }

    private void refreshReservas() {
        reservasModel.clear();
        try {
            List<Reserva> reservas = controller.getReservas();
            if (reservas == null || reservas.isEmpty()) {
                reservasModel.addElement("(no hay reservas registradas)");
                return;
            }
            for (Reserva r : reservas) {
                String horarioStr = (r.getHorario() != null) ? " | " + formatHorario(r.getHorario()) : "";
                reservasModel.addElement(String.format(
                        "Reserva #%d - Usuario: %s - Salón: %s%s",
                        r.getId(),
                        (r.getUsuario() != null ? r.getUsuario().getCorreo() : "N/A"),
                        (r.getSalon() != null ? r.getSalon().getNombre() : "N/A"),
                        horarioStr
                ));
            }
        } catch (Exception ex) {
            reservasModel.addElement("(error al cargar reservas: " + ex.getMessage() + ")");
        }
    }

    private String formatHorario(Horario h) {
        if (h == null) return "";
        Date inicio = h.getHoraStart();
        Date fin = h.getHoraEnd();
        return (inicio == null) ? "" :
                (fin == null ? sdfFechaHora.format(inicio)
                        : sdfFechaHora.format(inicio) + " - " + sdfFechaHora.format(fin));
    }

    private void accionCrearReserva() {
        int idx = listaSalones.getSelectedIndex();
        if (idx < 0 || salonesCache.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un salón de la lista.");
            return;
        }

        Salon seleccionado = salonesCache.get(Math.min(idx, salonesCache.size() - 1));
        String correo = txtUsuarioCorreo.getText().trim();
        String nombre = txtUsuarioNombre.getText().trim();
        String carnetStr = txtUsuarioCarnet.getText().trim();

        if (correo.isEmpty() || nombre.isEmpty() || carnetStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese correo, nombre y carnet del usuario.");
            return;
        }

        int carnet;
        try {
            carnet = Integer.parseInt(carnetStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El carnet debe ser numérico.");
            return;
        }

        Horario horarioSeleccionado = pedirHorario();
        if (horarioSeleccionado == null) return;

        try {
            Usuario u = new Usuario(carnet, nombre);
            u.setCorreo(correo);
            Reserva res = controller.crearReserva(u, seleccionado, horarioSeleccionado);
            if (res != null) {
                JOptionPane.showMessageDialog(this,
                        "Reserva creada correctamente (ID: " + res.getId() + ").");
                refreshReservas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear la reserva.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear la reserva: " + ex.getMessage());
        }
    }

    private Horario pedirHorario() {
        Date ahora = new Date();
        Calendar calInicio = Calendar.getInstance();
        calInicio.setTime(ahora);
        calInicio.add(Calendar.MINUTE, 30);

        Calendar calFin = Calendar.getInstance();
        calFin.setTime(calInicio.getTime());
        calFin.add(Calendar.HOUR_OF_DAY, 2);

        SpinnerDateModel modelInicio = new SpinnerDateModel(calInicio.getTime(), null, null, Calendar.MINUTE);
        JSpinner spinnerInicio = new JSpinner(modelInicio);
        spinnerInicio.setEditor(new JSpinner.DateEditor(spinnerInicio, "dd/MM/yyyy HH:mm"));

        SpinnerDateModel modelFin = new SpinnerDateModel(calFin.getTime(), null, null, Calendar.MINUTE);
        JSpinner spinnerFin = new JSpinner(modelFin);
        spinnerFin.setEditor(new JSpinner.DateEditor(spinnerFin, "dd/MM/yyyy HH:mm"));

        JPanel panel = new JPanel(new GridLayout(4, 1, 6, 6));
        panel.add(new JLabel("Seleccione fecha y hora de inicio:"));
        panel.add(spinnerInicio);
        panel.add(new JLabel("Seleccione fecha y hora de fin:"));
        panel.add(spinnerFin);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Configurar horario de reserva",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return null;

        Date inicio = (Date) spinnerInicio.getValue();
        Date fin = (Date) spinnerFin.getValue();

        if (!fin.after(inicio)) {
            JOptionPane.showMessageDialog(this, "La hora de fin debe ser posterior a la hora de inicio.");
            return null;
        }

        return new Horario(inicio, fin);
    }

    private void accionEliminarReserva() {
        int idx = listaReservas.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva para eliminar.");
            return;
        }

        List<Reserva> reservas = controller.getReservas();
        if (reservas == null || reservas.isEmpty() || idx >= reservas.size()) {
            JOptionPane.showMessageDialog(this, "La reserva seleccionada no es válida.");
            refreshReservas();
            return;
        }

        Reserva sel = reservas.get(idx);
        int opt = JOptionPane.showConfirmDialog(this,
                "¿Eliminar reserva #" + sel.getId() + "?\nSalón: " +
                        (sel.getSalon() != null ? sel.getSalon().getNombre() : "N/A"),
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (opt != JOptionPane.YES_OPTION) return;

        boolean ok = controller.eliminarReserva(sel);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Reserva eliminada correctamente.");
            refreshReservas();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar la reserva.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Controlador ctrl = new Controlador();
            new ProgramaGUI(ctrl);
        });
    }

    public List<Salon> getSalonesCache() {
        return salonesCache;
    }
}
