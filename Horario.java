import java.util.Date;

public class Horario {
    private Date horaStart;
    private Date horaEnd;

    public Horario(Date horaStart, Date horaEnd) {
        this.horaStart = horaStart;
        this.horaEnd = horaEnd;
    }

    public Date getHoraStart() {
        return horaStart;
    }

    public Date getHoraEnd() {
        return horaEnd;
    }
}
