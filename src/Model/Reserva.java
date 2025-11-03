package Model;

import java.io.Serializable;
import java.util.Objects;

public class Reserva implements Serializable {
    private int id;
    private Usuario usuario;
    private Salon salon;
    private Horario horario;

    // Flags de notificación (corresponden a notificado_2dias / notificado_1hora)
    private boolean notificado2dias = false;
    private boolean notificado1hora = false;

    public Reserva() {}

    public Reserva(Usuario usuario, Salon salon, Horario horario) {
        this.usuario = usuario;
        this.salon = salon;
        this.horario = horario;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Salon getSalon() { return salon; }
    public void setSalon(Salon salon) { this.salon = salon; }

    public Horario getHorario() { return horario; }
    public void setHorario(Horario horario) { this.horario = horario; }

    // Métodos con distintas variantes de nombre para compatibilidad con Notificacion
    // 2 días
    public boolean isNotificado2dias() { return notificado2dias; }
    public void setNotificado2dias(boolean v) { this.notificado2dias = v; }

    public boolean isNotificado2Dias() { return notificado2dias; }
    public void setNotificado2Dias(boolean v) { this.notificado2dias = v; }

    // 1 hora
    public boolean isNotificado1hora() { return notificado1hora; }
    public void setNotificado1hora(boolean v) { this.notificado1hora = v; }

    public boolean isNotificado1Hora() { return notificado1hora; }
    public void setNotificado1Hora(boolean v) { this.notificado1hora = v; }

    @Override
    public String toString() {
        return "Reserva{id=" + id
                + ", usuario=" + (usuario != null ? usuario.getCorreo() : "N/A")
                + ", salon=" + (salon != null ? salon.getNombre() : "N/A")
                + ", inicio=" + (horario != null ? horario.getHoraStart() : "N/A")
                + ", n2d=" + notificado2dias + ", n1h=" + notificado1hora + "}";
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva)) return false;
        Reserva r = (Reserva) o;
        return id == r.id;
    }
}
