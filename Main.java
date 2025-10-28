import java.sql.Connection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import Controllers.AuthController;
import Controllers.Controlador;
import Model.Notificacion;
import Model.conexion;
import Vistas.LoginGUI;
import Vistas.ProgramaGUI;

public class Main {

    public static void main(String[] args) {
        // Probar conexión BD primero
        try {
            Connection testConn = conexion.conectar();
            System.out.println("Conexión a BD exitosa");
            testConn.close();
        } catch (Exception e) {
            System.err.println("Error conectando a BD: " + e.getMessage());
            e.printStackTrace();
            // No salir, intentar continuar con la app
        }

        setSystemLookAndFeel();

        try {
            Controlador controlador = new Controlador();
            precargarCuentaDemo();

            // Iniciar notificaciones
            Notificacion.startScheduler(controlador.getReservas(), 30, 60);

            SwingUtilities.invokeLater(() -> {
                try {
                    new ProgramaGUI(controlador);
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

    private static void precargarCuentaDemo() {
        AuthController.registrarCuenta(
            "Cuenta Demo",      // Nombre
            "00000000",         // Carnet
            "demo@ejemplo.com", // Correo
            "1234"              // Contraseña
        );
    }
}
