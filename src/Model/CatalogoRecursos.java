package Model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

public class CatalogoRecursos extends JDialog {
    private final DefaultListModel<Recurso> model = new DefaultListModel<>();
    private JList<Recurso> lista;
    private JTextArea detalle;

    public CatalogoRecursos(Frame owner) {
        super(owner, "Catálogo de recursos", true);
        setSize(520, 360);
        setLocationRelativeTo(owner);
        initModel();
        initComponents();
    }

    private void initModel() {
        List<Recurso> items = new ArrayList<>();
        items.add(new Recurso("Libro: Algoritmos", "Libro", "Introducción a algoritmos y estructuras de datos", 3));
        items.add(new Recurso("Marcador verde", "Marcador", "Marcador de tinta permanente color verde", 10));
        items.add(new Recurso("Pizarrón portátil", "Pizarrón", "Pizarrón blanco con soporte", 2));
        items.add(new Recurso("Cable HDMI 2m", "Cable", "HDMI alta velocidad 2 metros", 5));
        items.add(new Recurso("Proyector portátil", "Equipo", "Mini proyector para presentaciones", 1));

        for (Recurso r : items) {
            model.addElement(r);
        }
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
        JButton btnReservar = new JButton("Solicitar recurso");

        btnCerrar.addActionListener(e -> dispose());
        btnReservar.addActionListener(e -> accionSolicitar());

        south.add(btnReservar);
        south.add(btnCerrar);
        add(south, BorderLayout.SOUTH);

        lista.addListSelectionListener(e -> {
            Recurso sel = lista.getSelectedValue();
            if (sel != null) {
                detalle.setText(sel.getNombre() + "\n\nTipo: " + sel.getTipo() + "\n\n" + sel.getDescripcion()
                        + "\n\nDisponibles: " + sel.getCantidad());
            } else {
                detalle.setText("");
            }
        });
    }

    private void accionSolicitar() {
        Recurso sel = lista.getSelectedValue();
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
        JOptionPane.showMessageDialog(this, "Solicitud enviada para: " + sel.getNombre());
    }
}
