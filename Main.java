public class Main {
    public static void main(String[] args) {
        // Solo lanza la interfaz gráfica
        javax.swing.SwingUtilities.invokeLater(() -> {
            new VistaGUI();
        });
    }
}
