package Vistas;
import java.awt.*;
import javax.swing.*;

import Controllers.AuthController;
import Controllers.Controlador;

public class LoginGUI extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtPass;
    private Controlador controller;

    public LoginGUI() {
        this(null);
    }

    public LoginGUI(Controlador controller) {
        super("Inicio de sesión");
        this.controller = controller;

        setMinimumSize(new Dimension(420, 240));
        setResizable(false);
        setLocationRelativeTo(null);

        initComponents();

        pack();
        setVisible(true);
    }

    private void initComponents() {
        JPanel p = new JPanel(new BorderLayout(8, 8));

        // Formulario
        JPanel form = new JPanel(new GridLayout(2, 2, 6, 6));
        form.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        form.add(txtCorreo);

        form.add(new JLabel("Contraseña:"));
        txtPass = new JPasswordField();
        form.add(txtPass);

        // Botones
        JPanel buttons = new JPanel(new GridLayout(2, 2, 6, 6));
        JButton btnLogin = new JButton("Iniciar sesión");
        JButton btnCrear = new JButton("Crear cuenta");
        JButton btnRecuperar = new JButton("Recuperar contraseña");
        JButton btnSalir = new JButton("Salir");

        buttons.add(btnLogin);
        buttons.add(btnCrear);
        buttons.add(btnRecuperar);
        buttons.add(btnSalir);

        // Panel principal
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(form, BorderLayout.CENTER);
        p.add(buttons, BorderLayout.SOUTH);
        add(p);

        // Acciones
        btnLogin.addActionListener(e -> accionLogin());
        btnCrear.addActionListener(e -> accionCrearCuenta());
        btnRecuperar.addActionListener(e -> accionOlvideContrasena());
        btnSalir.addActionListener(e -> dispose());
    }

    private void accionLogin() {
        String correo = txtCorreo.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (correo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Correo y contraseña obligatorios");
            return;
        }

        if (AuthController.autenticar(correo, pass)) {
            if (controller != null) {
                controller.autenticarYSetUsuario(correo, pass);
            }
            JOptionPane.showMessageDialog(this, "Bienvenido " + correo);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas");
        }
    }

    private void accionCrearCuenta() {
        JTextField tfNombre = new JTextField();
        JTextField tfCarnet = new JTextField();
        JTextField tfCorreo = new JTextField();
        JPasswordField pfPass = new JPasswordField();

        Object[] msg = {
            "Nombre:", tfNombre,
            "Carnet (número):", tfCarnet,
            "Correo:", tfCorreo,
            "Contraseña:", pfPass
        };

        int opc = JOptionPane.showConfirmDialog(this, msg, "Crear cuenta", JOptionPane.OK_CANCEL_OPTION);
        if (opc != JOptionPane.OK_OPTION) return;

        String nombre = tfNombre.getText().trim();
        String carnetStr = tfCarnet.getText().trim();
        String correo = tfCorreo.getText().trim();
        String pass = new String(pfPass.getPassword());

        if (nombre.isEmpty() || carnetStr.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        try {
            // No se pide salón al crear cuenta; enviamos cadena vacía o ajustar según AuthController
            boolean ok = AuthController.registrarCuenta(nombre, carnetStr, correo, pass);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Ya existe una cuenta con ese correo o error al guardar");
                return;
            }
            JOptionPane.showMessageDialog(this, "Cuenta creada exitosamente");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear cuenta: " + ex.getMessage());
        }
    }

    private void accionOlvideContrasena() {
        String correo = JOptionPane.showInputDialog(this, "Ingrese su correo:");
        if (correo == null || correo.trim().isEmpty()) return;
        correo = correo.trim();

        if (!AuthController.existeCuenta(correo)) {
            JOptionPane.showMessageDialog(this, "No existe cuenta con ese correo");
            return;
        }

        JPasswordField pfNew = new JPasswordField();
        Object[] msg = {"Nueva contraseña:", pfNew};
        int opc = JOptionPane.showConfirmDialog(this, msg, "Restablecer contraseña", JOptionPane.OK_CANCEL_OPTION);
        if (opc != JOptionPane.OK_OPTION) return;

        String nueva = new String(pfNew.getPassword()).trim();
        if (nueva.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La nueva contraseña es obligatoria");
            return;
        }

        boolean ok = AuthController.resetPassword(correo, nueva);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Contraseña actualizada");
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar la contraseña");
        }
    }

    // Para probar la ventana de login directamente
    public static void main(String[] args) {
        AuthController.registrarCuenta("Cuenta Demo", "0000", "demo@ejemplo.com", "1234", "");
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}
