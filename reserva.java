
public class reserva {
    private int id;
    private usuario usuario;
    private salon salon;
    private horario horario;
    private boolean notificado2Dias;
    private boolean notificado1Hora;

    public reserva() {}

    public reserva(usuario usuario, salon salon, horario horario) {
        this.usuario = usuario;
        this.salon = salon;
        this.horario = horario;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public usuario getUsuario() { return usuario; }
    public void setUsuario(usuario usuario) { this.usuario = usuario; }

    public salon getSalon() { return salon; }
    public void setSalon(salon salon) { this.salon = salon; }

    public horario getHorario() { return horario; }
    public void setHorario(horario horario) { this.horario = horario; }

    public boolean isNotificado2Dias() { return notificado2Dias; }
    public void setNotificado2Dias(boolean notificado2Dias) { this.notificado2Dias = notificado2Dias; }

    public boolean isNotificado1Hora() { return notificado1Hora; }
    public void setNotificado1Hora(boolean notificado1Hora) { this.notificado1Hora = notificado1Hora; }
}
