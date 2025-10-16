package modelos;

public class reserva {
    private int id;
    private Usuario usuario;
    private salon salon;
    private Horario horario;
    private boolean notificado2Dias = false;
    private boolean notificado1Hora = false;
    
    public reserva(Usuario usuario, salon salon) {
        this.usuario = usuario;
        this.salon = salon;
        this.id = (int) (Math.random() * 1000); // ID temporal
    }
    
    public void modificarRes() {
        // Lógica para modificar reserva
    }
    
    public void cancelarRes() {
        // Lógica para cancelar reserva
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public salon getSalon() { return salon; }
    public Horario getHorario() { return horario; }
    public void setHorario(Horario horario) { this.horario = horario; }
    public boolean isNotificado2Dias() { return notificado2Dias; }
    public void setNotificado2Dias(boolean notificado2Dias) { this.notificado2Dias = notificado2Dias; }
    public boolean isNotificado1Hora() { return notificado1Hora; }
    public void setNotificado1Hora(boolean notificado1Hora) { this.notificado1Hora = notificado1Hora; }
}
