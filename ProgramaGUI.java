import java.awt.*;
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
    private final List<salon> salonesCache = new ArrayList<>();

    private JList<String> listaSalones;
    private JList<String> listaReservas;
    private JTextField txtUsuarioCorreo;
    private JTextField txtUsuarioNombre;
    private JTextField txtUsuarioCarnet;

    private final SimpleDateFormat sdfFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public ProgramaGUI(Controlador controller) {
        this.controller = controller;
        setTitle("Interfaz - Controlador");
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

        // Panel izquierdo: Salones
        JPanel pSalones = new JPanel(new BorderLayout(6, 6));
        pSalones.setBorder(BorderFactory.createTitledBorder("Salones disponibles"));
        listaSalones = new JList<>(salonesModel);
        listaSalones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pSalones.add(new JScrollPane(listaSalones), BorderLayout.CENTER);

        JButton btnRefrescarSalones = new JButton("Refrescar salones");
        btnRefrescarSalones.addActionListener(e -> refreshSalones());
        pSalones.add(btnRefrescarSalones, BorderLayout.SOUTH);

        // Panel derecho: Reservas
        JPanel pReservas = new JPanel(new BorderLayout(6, 6));
        pReservas.setBorder(BorderFactory.createTitledBorder("Reservas"));
        listaReservas = new JList<>(reservasModel);
        pReservas.add(new JScrollPane(listaReservas), BorderLayout.CENTER);

        // Panel de botones bajo Reservas: refrescar + catálogo
        JPanel pReservasSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        JButton btnRefrescarReservas = new JButton("Refrescar reservas");
        btnRefrescarReservas.addActionListener(e -> refreshReservas());
        pReservasSouth.add(btnRefrescarReservas);

        // Botón Catálogo de recursos
        JButton btnCatalogo = new JButton("Catálogo de recursos");
        btnCatalogo.addActionListener(e -> {
            // abrir diálogo del catálogo (modal)
            catalogoRecursos dialog = new catalogoRecursos(this);
            dialog.setVisible(true);
        });
        pReservasSouth.add(btnCatalogo);

        // Botón Eliminar reserva seleccionada
        JButton btnEliminarReserva = new JButton("Eliminar reserva");
        btnEliminarReserva.addActionListener(e -> accionEliminarReserva());
        pReservasSouth.add(btnEliminarReserva);

        // Botón Finalizar y salir
        JButton btnFinalizarSalir = new JButton("Finalizar y salir");
        btnFinalizarSalir.addActionListener(e -> {
            int opt = JOptionPane.showConfirmDialog(this,
                    "¿Desea finalizar y salir de la aplicación?",
                    "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opt == JOptionPane.YES_OPTION) {
                // cerrar aplicación
                System.exit(0);
            }
        });
        pReservasSouth.add(btnFinalizarSalir);

        pReservas.add(pReservasSouth, BorderLayout.SOUTH);

        // Panel inferior: Formulario de reserva simple
        JPanel pForm = new JPanel(new GridLayout(2, 1, 6, 6));
        pForm.setBorder(BorderFactory.createTitledBorder("Crear reserva"));

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
        JButton btnReservar = new JButton("Crear reserva (selección)");
        btnReservar.addActionListener(e -> accionCrearReserva());
        pActions.add(btnReservar);

        pForm.add(pInputs);
        pForm.add(pActions);

        // Layout principal
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
            horario nowHorario = new horario(new Date(), new Date());
            List<salon> salones = controller.getSalonesDisp(nowHorario);
            if (salones == null) salones = new ArrayList<>();
            for (salon s : salones) {
                salonesCache.add(s);
                String entry = String.format("ID:%d  %s  (%s)  Cap:%s",
                        s.getId(), s.getNombre(), s.getUbicacion(), s.getCapacidad());
                salonesModel.addElement(entry);
            }
            if (salonesModel.isEmpty()) salonesModel.addElement("(no hay salones disponibles)");
        } catch (Exception ex) {
            salonesModel.addElement("(error al cargar salones: " + ex.getMessage() + ")");
        }
    }

    private void refreshReservas() {
        reservasModel.clear();
        try {
            List<reserva> reservas = controller.getReservas();
            if (reservas == null || reservas.isEmpty()) {
                reservasModel.addElement("(no hay reservas)");
                return;
            }
            for (reserva r : reservas) {
                String horarioStr = "";
                if (r.getHorario() != null) {
                    horarioStr = " | " + formatHorario(r.getHorario());
                }
                String s = String.format("reserva #%d - Usuario: %s - Salón: %s%s",
                        r.getId(),
                        r.getUsuario() != null ? r.getUsuario().getCorreo() : "N/A",
                        r.getSalon() != null ? r.getSalon().getNombre() : "N/A",
                        horarioStr);
                reservasModel.addElement(s);
            }
        } catch (Exception ex) {
            reservasModel.addElement("(error al cargar reservas: " + ex.getMessage() + ")");
        }
    }

    private String formatHorario(horario h) {
        if (h == null) return "";
        Date inicio = h.getHoraStart();
        Date fin = h.getHoraEnd();
        if (inicio == null) return "";
        if (fin == null) {
            return sdfFechaHora.format(inicio);
        } else {
            return sdfFechaHora.format(inicio) + " - " + sdfFechaHora.format(fin);
        }
    }

    private void accionCrearReserva() {
        int idx = listaSalones.getSelectedIndex();
        if (idx < 0 || salonesCache.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un salón de la lista a la izquierda.");
            return;
        }
        salon seleccionado = salonesCache.get(Math.min(idx, salonesCache.size() - 1));

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
            JOptionPane.showMessageDialog(this, "Carnet debe ser numérico.");
            return;
        }

        // pedir fecha y hora al usuario (inicio y fin)
        horario horarioSeleccionado = pedirHorario();
        if (horarioSeleccionado == null) {
            // usuario canceló o error en selección
            return;
        }

        try {
            usuario u = new usuario(carnet, nombre);
            u.setCorreo(correo);

            reserva res = controller.crearreserva(u, seleccionado, horarioSeleccionado);
            if (res != null) {
                JOptionPane.showMessageDialog(this, "Reserva creada, ID: " + res.getId() + "\n" + formatHorario(horarioSeleccionado));
                refreshReservas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear la reserva.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear reserva: " + ex.getMessage());
        }
    }

    /**
     * Muestra un diálogo con dos JSpinner (inicio y fin) para que el usuario seleccione fecha+hora.
     * Valida que fin sea posterior a inicio. Devuelve un objeto horario con fecha, horaStart y horaEnd.
     * Devuelve null si el usuario cancela o la validación falla.
     */
    private horario pedirHorario() {
        Date ahora = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(ahora);

        // inicio por defecto = ahora + 30 min redondeado a 15 min
        cal.add(Calendar.MINUTE, 30);
        int min = cal.get(Calendar.MINUTE);
        int rem = min % 15;
        if (rem != 0) cal.add(Calendar.MINUTE, 15 - rem);
        Date defaultInicio = cal.getTime();

        // fin por defecto = inicio + 2 horas
        Calendar calFin = Calendar.getInstance();
        calFin.setTime(defaultInicio);
        calFin.add(Calendar.HOUR_OF_DAY, 2);
        Date defaultFin = calFin.getTime();

        SpinnerDateModel modelInicio = new SpinnerDateModel(defaultInicio, null, null, Calendar.MINUTE);
        JSpinner spinnerInicio = new JSpinner(modelInicio);
        spinnerInicio.setEditor(new JSpinner.DateEditor(spinnerInicio, "dd/MM/yyyy HH:mm"));

        SpinnerDateModel modelFin = new SpinnerDateModel(defaultFin, null, null, Calendar.MINUTE);
        JSpinner spinnerFin = new JSpinner(modelFin);
        spinnerFin.setEditor(new JSpinner.DateEditor(spinnerFin, "dd/MM/yyyy HH:mm"));

        JPanel panel = new JPanel(new GridLayout(4, 1, 6, 6));
        panel.add(new JLabel("Seleccione fecha y hora de inicio:"));
        panel.add(spinnerInicio);
        panel.add(new JLabel("Seleccione fecha y hora de fin:"));
        panel.add(spinnerFin);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Fecha y hora de reserva",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return null;
        }

        Date inicio = (Date) spinnerInicio.getValue();
        Date fin = (Date) spinnerFin.getValue();

        if (!fin.after(inicio)) {
            JOptionPane.showMessageDialog(this, "La hora de fin debe ser posterior a la hora de inicio.");
            return null;
        }

        // construir horario con fecha e inicio/fin
        return new horario(inicio, inicio, fin);
    }

    private void accionEliminarReserva() {
        int idx = listaReservas.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva para eliminar.");
            return;
        }

        List<reserva> reservas = controller.getReservas();
        if (reservas == null || reservas.isEmpty() || idx >= reservas.size()) {
            JOptionPane.showMessageDialog(this, "Reserva inválida.");
            refreshReservas();
            return;
        }

        reserva sel = reservas.get(idx);
        int opt = JOptionPane.showConfirmDialog(this,
                "¿Eliminar reserva #" + sel.getId() + "?\nSalón: " + (sel.getSalon() != null ? sel.getSalon().getNombre() : "N/A"),
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (opt != JOptionPane.YES_OPTION) return;

        boolean ok = controller.eliminarReserva(sel);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Reserva eliminada.");
            refreshReservas();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar la reserva.");
        }
    }

    // Para probar la ventana de forma independiente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Controlador ctrl = new Controlador();
            new ProgramaGUI(ctrl);
        });
    }
}