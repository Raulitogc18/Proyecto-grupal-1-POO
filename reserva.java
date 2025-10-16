import java.time.LocalDateTime;

public class reserva {
    private salon salon;
    private LocalDateTime fechaHora;

    public reserva(salon salon, LocalDateTime fechaHora) {
        this.salon = salon;
        this.fechaHora = fechaHora;
    }

    public salon getSalon() { return salon; }
    public LocalDateTime getFechaHora() { return fechaHora; }
}
    

