package modelos;

public class reserva {
    private static int contador = 1;
    private int id;
    private Usuario usuario;
    private salon salon;
    private Horario horario;

    public reserva(Usuario usuario, salon salon, Horario horario) {
        this.id = contador++;
        this.usuario = usuario;
        this.salon = salon;
        this.horario = horario;
    }

    public void modificarReserva() {
    }

    public void cancelarReserva() {
    }

    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public salon getSalon() { return salon; }
    public Horario getHorario() { return horario; }

    @Override
    public String toString() {
        return "Reserva #" + id + " - Usuario: " + usuario.getNombre() +
            ", Salón: " + salon.getUbicacion();
    }
}
