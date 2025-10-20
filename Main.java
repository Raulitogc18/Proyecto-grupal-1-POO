import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Intentar aplicar Look & Feel del sistema (no crítico si falla)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Crear controlador principal
        Controlador controlador = new Controlador();

        // Precargar cuenta demo en el AuthController (métodos estáticos)
        AuthController.registrarCuenta("demo@ejemplo.com", "1234");

        // Iniciar las vistas en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            new ProgramaGUI(controlador); // Vista ligada al controlador principal
            new LoginGUI(controlador);   // Vista de autenticación ligada al mismo controlador
        });
    }
}