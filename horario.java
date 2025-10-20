import java.util.Date;

public class horario {
    private Date fecha;
    private Date horaStart;
    private Date horaEnd;

    // Constructor: fecha + hora de inicio (horaEnd = horaStart + 2 horas por defecto)
    public horario(Date fecha, Date horaStart) {
        this.fecha = (fecha == null) ? null : new Date(fecha.getTime());
        this.horaStart = (horaStart == null) ? null : new Date(horaStart.getTime());
        if (this.horaStart != null) {
            this.horaEnd = new Date(this.horaStart.getTime() + 2 * 60 * 60 * 1000);
        } else {
            this.horaEnd = null;
        }
    }

    // Constructor: inicio y fin (fecha se toma de horaStart si no se provee)
    public horario(Date horaStart, Date horaEnd, boolean fechaFromStart) {
        this.fecha = (horaStart == null) ? null : new Date(horaStart.getTime());
        this.horaStart = (horaStart == null) ? null : new Date(horaStart.getTime());
        if (horaEnd != null) {
            this.horaEnd = new Date(horaEnd.getTime());
        } else if (this.horaStart != null) {
            this.horaEnd = new Date(this.horaStart.getTime() + 2 * 60 * 60 * 1000);
        } else {
            this.horaEnd = null;
        }
    }

    // Constructor completo: fecha, inicio y fin
    public horario(Date fecha, Date horaStart, Date horaEnd) {
        this.fecha = (fecha == null) ? null : new Date(fecha.getTime());
        this.horaStart = (horaStart == null) ? null : new Date(horaStart.getTime());
        if (horaEnd != null) {
            this.horaEnd = new Date(horaEnd.getTime());
        } else if (this.horaStart != null) {
            this.horaEnd = new Date(this.horaStart.getTime() + 2 * 60 * 60 * 1000);
        } else {
            this.horaEnd = null;
        }
    }

    public boolean seValida() {
        return fecha != null && horaStart != null && horaEnd != null && horaStart.before(horaEnd);
    }

    // Getters y Setters con copias defensivas
    public Date getFecha() { return (fecha == null) ? null : new Date(fecha.getTime()); }
    public Date getHoraStart() { return (horaStart == null) ? null : new Date(horaStart.getTime()); }
    public Date getHoraEnd() { return (horaEnd == null) ? null : new Date(horaEnd.getTime()); }

    public void setFecha(Date fecha) { this.fecha = (fecha == null) ? null : new Date(fecha.getTime()); }
    public void setHoraStart(Date horaStart) {
        this.horaStart = (horaStart == null) ? null : new Date(horaStart.getTime());
    }
    public void setHoraEnd(Date horaEnd) {
        this.horaEnd = (horaEnd == null && this.horaStart != null)
                ? new Date(this.horaStart.getTime() + 2 * 60 * 60 * 1000)
                : (horaEnd == null ? null : new Date(horaEnd.getTime()));
    }

    @Override
    public String toString() {
        return "Horario{fecha=" + fecha + ", inicio=" + horaStart + ", fin=" + horaEnd + "}";
    }
}