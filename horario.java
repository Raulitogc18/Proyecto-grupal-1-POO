import java.util.Date;

public class horario {
    private Date horaStart;
    private Date horaEnd;

    public horario() {}

    public horario(Date start, Date end) {
        this.horaStart = start;
        this.horaEnd = end;
    }

    public Date getHoraStart() { return horaStart; }
    public void setHoraStart(Date horaStart) { this.horaStart = horaStart; }

    public Date getHoraEnd() { return horaEnd; }
    public void setHoraEnd(Date horaEnd) { this.horaEnd = horaEnd; }
}