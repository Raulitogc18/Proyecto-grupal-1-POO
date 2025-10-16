import java.util.Date;

public class Notificacion {
    private String mensaje;
    private Usuario destino;
    private Date fechaEnvio;

    public Notificacion(String mensaje, Usuario destino) {
        this.mensaje = mensaje;
        this.destino = destino;
        this.fechaEnvio = new Date();
    }

    public void enviar() {
    }

}