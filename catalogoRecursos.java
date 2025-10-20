import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class catalogoRecursos extends JDialog {

    private final DefaultListModel<recurso> model = new DefaultListModel<>();
    private JList<recurso> lista;
    private JTextArea detalle;

    public catalogoRecursos(Frame owner) {
        super(owner, "Catálogo de recursos", true);
        setSize(520, 360);
        setLocationRelativeTo(owner);
        initModel();
        initComponents();
    }

    private void initModel() {
        // Ejemplos: libros, marcadores, pizarrones, cables HDMI...
        List<recurso> items = new ArrayList<>();
        items.add(new recurso("Libro: Algoritmos", "Libro", "Introducción a algoritmos y estructuras de datos", 3));
        items.add(new recurso("Marcador verde", "Marcador", "Marcador de tinta permanente color verde", 10));
        items.add(new recurso("Pizarrón portátil", "Pizarrón", "Pizarrón blanco con soporte", 2));
        items.add(new recurso("Cable HDMI 2m", "Cable", "HDMI alta velocidad 2 metros", 5));
        items.add(new recurso("Proyector portátil", "Equipo", "Mini proyector para presentaciones", 1));

        for (recurso r : items) model.addElement(r);
    }

    private void initComponents() {
        setLayout(new BorderLayout(8, 8));
        lista = new JList<>(model);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(lista), BorderLayout.CENTER);

        detalle = new JTextArea();
        detalle.setEditable(false);
        detalle.setLineWrap(true);
        detalle.setWrapStyleWord(true);
        detalle.setBorder(BorderFactory.createTitledBorder("Detalle"));
        detalle.setPreferredSize(new Dimension(200, 100));
        add(detalle, BorderLayout.EAST);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnCerrar = new JButton("Cerrar");
        JButton btnReservar = new JButton("Solicitar recurso (demo)");

        btnCerrar.addActionListener(e -> dispose());
        btnReservar.addActionListener(e -> accionSolicitar());

        south.add(btnReservar);
        south.add(btnCerrar);
        add(south, BorderLayout.SOUTH);

        lista.addListSelectionListener(e -> {
            recurso sel = lista.getSelectedValue();
            if (sel != null) {
                detalle.setText(sel.getNombre() + "\n\nTipo: " + sel.getTipo() + "\n\n" + sel.getDescripcion()
                        + "\n\nDisponibles: " + sel.getCantidad());
            } else {
                detalle.setText("");
            }
        });
    }

    private void accionSolicitar() {
        recurso sel = lista.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso del catálogo.");
            return;
        }
        if (sel.getCantidad() <= 0) {
            JOptionPane.showMessageDialog(this, "No hay unidades disponibles de: " + sel.getNombre());
            return;
        }
        // Demo: reducir cantidad localmente
        sel.setCantidad(sel.getCantidad() - 1);
        lista.repaint();
        detalle.setText(sel.getNombre() + "\n\nTipo: " + sel.getTipo() + "\n\n" + sel.getDescripcion()
                + "\n\nDisponibles: " + sel.getCantidad());
        JOptionPane.showMessageDialog(this, "Solicitud enviada (demo) para: " + sel.getNombre());
    }
}
