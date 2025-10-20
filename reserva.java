public class reserva {
    private static int contador = 1;
    private int id;
    private usuario usuario;
    private salon salon;
    private horario horario;
    private boolean notificado2Dias = false;
    private boolean notificado1Hora = false;

    public reserva(usuario usuario, salon salon) {
        this(usuario, salon, null);
    }

    public reserva(usuario usuario, salon salon, horario horario) {
        this.id = contador++;
        this.usuario = usuario;
        this.salon = salon;
        this.horario = horario;
    }

    public void modificarReserva(horario nuevoHorario) {
        this.horario = nuevoHorario;
    }

    public void cancelarReserva() {
        this.horario = null;
    }

    // Getters y setters
    public int getId() { return id; }
    public usuario getUsuario() { return usuario; }
    public salon getSalon() { return salon; }
    public horario getHorario() { return horario; }
    public void setHorario(horario horario) { this.horario = horario; }

    public boolean isNotificado2Dias() { return notificado2Dias; }
    public void setNotificado2Dias(boolean notificado2Dias) { this.notificado2Dias = notificado2Dias; }
    public boolean isNotificado1Hora() { return notificado1Hora; }
    public void setNotificado1Hora(boolean notificado1Hora) { this.notificado1Hora = notificado1Hora; }

    @Override
    public String toString() {
        String usuarioStr = (usuario != null) ? usuario.getNombre() : "N/A";
        String salonStr = (salon != null) ? salon.getNombre() : "N/A";
        String horarioStr = (horario != null) ? horario.toString() : "Sin horario";
        return "Reserva#" + id + " - Usuario: " + usuarioStr + " - Salón: " + salonStr + " - " + horarioStr;
    }
}
