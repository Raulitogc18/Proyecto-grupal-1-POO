package modelos;
import java.util.Date;

public class Horario {
    private Date fecha;
    private Date horaStart;
    private Date horaEnd;
    
    public Horario(Date fecha, Date horaStart) {
        this.fecha = fecha;
        this.horaStart = horaStart;
        // Establecer horaEnd automáticamente (2 horas después por defecto)
        this.horaEnd = new Date(horaStart.getTime() + (2 * 60 * 60 * 1000));
    }
    
    public boolean seValida() {
        return fecha != null && horaStart != null && horaEnd != null &&
               horaStart.before(horaEnd);
    }
    
    // Getters y Setters
    public Date getFecha() { return fecha; }
    public Date getHoraStart() { return horaStart; }
    public Date getHoraEnd() { return horaEnd; }
    public void setHoraEnd(Date horaEnd) { this.horaEnd = horaEnd; }
}