import java.awt.*;
import javax.swing.*;

public class LoginGUI extends JFrame {

    private Controlador controller;
    private JTextField txtCorreo;
    private JPasswordField txtPass;

    public LoginGUI() { this(null); }

    public LoginGUI(Controlador controller) {
        this.controller = controller;
        setTitle("Inicio de sesión");
    
        pack();
        setMinimumSize(new java.awt.Dimension(420, 240));
        setResizable(false);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(2, 2, 6, 6));
        form.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        form.add(txtCorreo);
        form.add(new JLabel("Contraseña:"));
        txtPass = new JPasswordField();
        form.add(txtPass);


        JPanel buttons = new JPanel(new GridLayout(2, 2, 6, 6));
        JButton btnLogin = new JButton("Iniciar sesión");
        JButton btnCrear = new JButton("Crear cuenta");
        JButton btnRecuperar = new JButton("Recuperar contraseña");
        JButton btnSalir = new JButton("Salir");

        buttons.add(btnLogin);
        buttons.add(btnCrear);
        buttons.add(btnSalir);
        buttons.add(btnRecuperar);

        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(form, BorderLayout.CENTER);
        p.add(buttons, BorderLayout.SOUTH);
        add(p);

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
            int carnet = Integer.parseInt(carnetStr);
            usuario u = new usuario(carnet, nombre);
            u.setCorreo(correo);
            u.setContrasena(pass);

            boolean ok = AuthController.registrarCuenta(correo, pass);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Ya existe una cuenta con ese correo");
                return;
            }

            if (controller != null) {
                controller.registrarUsuario(u);
            }

            JOptionPane.showMessageDialog(this, "Cuenta creada exitosamente");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Carnet debe ser numérico");
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

        String token = AuthController.generarTokenReset(correo);
        if (token == null) {
            JOptionPane.showMessageDialog(this, "No se pudo generar token");
            return;
        }

        JOptionPane.showMessageDialog(this, "Token de reinicio (demo): " + token +
                "\nSe mostrará ahora un diálogo para cambiar contraseña.");

        JPasswordField pfToken = new JPasswordField();
        JPasswordField pfNew = new JPasswordField();
        Object[] msg = {
                "Token recibido:", pfToken,
                "Nueva contraseña:", pfNew
        };
        int opc = JOptionPane.showConfirmDialog(this, msg, "Restablecer contraseña", JOptionPane.OK_CANCEL_OPTION);
        if (opc != JOptionPane.OK_OPTION) return;

        String tokenIngresado = new String(pfToken.getPassword()).trim();
        String nueva = new String(pfNew.getPassword()).trim();
        if (tokenIngresado.isEmpty() || nueva.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Token y nueva contraseña son obligatorios");
            return;
        }

        boolean ok = AuthController.resetPasswordConToken(correo, tokenIngresado, nueva);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Contraseña actualizada");
        } else {
            JOptionPane.showMessageDialog(this, "Token inválido o expirado");
        }
    }

    // Para probar la ventana de login directamente
    public static void main(String[] args) {
        AuthController.registrarCuenta("demo@ejemplo.com", "1234");
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}