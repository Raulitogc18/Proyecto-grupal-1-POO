package Vistas;
import Controllers.AuthController;
import Controllers.Controlador;
import Model.Conexion;
import Model.Notificacion;
import java.sql.Connection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        // Probar conexión con la base de datos
        try (Connection testConn = Conexion.conectar()) {
            if (testConn != null) {
                System.out.println("Conexión a BD exitosa");
            } else {
                System.err.println("No se pudo establecer conexión con la BD.");
            }
        } catch (Exception e) {
            System.err.println("Error conectando a BD: " + e.getMessage());
            e.printStackTrace();
        }

        setSystemLookAndFeel();

        try {
            Controlador controlador = new Controlador();

            // Crear cuenta demo solo si no existe
            if (!AuthController.existeCuenta("demo@ejemplo.com")) {
                System.out.println("Creando cuenta demo...");
                AuthController.registrarCuenta(
                    "Cuenta Demo",
                    "00000000",
                    "demo@ejemplo.com",
                    "1234"
                );
            } else {
                System.out.println("Cuenta demo ya existe, no se crea nuevamente.");
            }

            // Iniciar notificaciones (intervalos en minutos)
            Notificacion.startScheduler(controlador.getReservas(), 30, 60);

            // Iniciar la interfaz gráfica (solo Login al inicio)
            SwingUtilities.invokeLater(() -> {
                try {
                    new LoginGUI(controlador);
                } catch (Exception e) {
                    System.err.println("Error iniciando GUI: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            System.err.println("Error general iniciando app: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look & Feel del sistema: " + e.getMessage());
        }
    }
}
