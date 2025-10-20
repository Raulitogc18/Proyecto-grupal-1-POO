import java.awt.*;
import java.util.ArrayList;
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

        JPanel pSalones = new JPanel(new BorderLayout(6, 6));
        pSalones.setBorder(BorderFactory.createTitledBorder("Salones disponibles"));
        listaSalones = new JList<>(salonesModel);
        listaSalones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pSalones.add(new JScrollPane(listaSalones), BorderLayout.CENTER);

        JButton btnRefrescarSalones = new JButton("Refrescar salones");
        btnRefrescarSalones.addActionListener(e -> refreshSalones());
        pSalones.add(btnRefrescarSalones, BorderLayout.SOUTH);

        JPanel pReservas = new JPanel(new BorderLayout(6, 6));
        pReservas.setBorder(BorderFactory.createTitledBorder("Reservas"));
        listaReservas = new JList<>(reservasModel);
        pReservas.add(new JScrollPane(listaReservas), BorderLayout.CENTER);

        JButton btnRefrescarReservas = new JButton("Refrescar reservas");
        btnRefrescarReservas.addActionListener(e -> refreshReservas());
        pReservas.add(btnRefrescarReservas, BorderLayout.SOUTH);

        JPanel pForm = new JPanel(new GridLayout(2, 1, 6, 6));
        pForm.setBorder(BorderFactory.createTitledBorder("Crear reserva (demo)"));

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
                String s = String.format("reserva #%d - Usuario: %s - Salón: %s",
                        r.getId(),
                        r.getUsuario() != null ? r.getUsuario().getCorreo() : "N/A",
                        r.getSalon() != null ? r.getSalon().getNombre() : "N/A");
                reservasModel.addElement(s);
            }
        } catch (Exception ex) {
            reservasModel.addElement("(error al cargar reservas: " + ex.getMessage() + ")");
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

        try {
            usuario u = new usuario(carnet, nombre);
            u.setCorreo(correo);

            horario horario = new horario(new Date(), new Date());

            reserva res = controller.crearreserva(u, seleccionado, horario);
            if (res != null) {
                JOptionPane.showMessageDialog(this, "Reserva creada ✅ ID: " + res.getId());
                refreshReservas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear la reserva.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear reserva: " + ex.getMessage());
        }
    }

    // Para probar la ventana de login directamente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Controlador ctrl = new Controlador();
            new ProgramaGUI(ctrl);
        });
    }
}