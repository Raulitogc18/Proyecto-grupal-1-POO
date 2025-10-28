package Model;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservaDAO {

    public boolean insertar(reserva r) throws SQLException {
        String sql = "INSERT INTO reservas (usuario_id, salon_id, hora_start, hora_end, notificado_2dias, notificado_1hora) VALUES (?,?,?,?,?,?)";
        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getUsuario().getId());
            ps.setInt(2, r.getSalon().getId());
            ps.setTimestamp(3, new Timestamp(r.getHorario().getHoraStart().getTime()));
            if (r.getHorario().getHoraEnd() != null)
                ps.setTimestamp(4, new Timestamp(r.getHorario().getHoraEnd().getTime()));
            else
                ps.setNull(4, Types.TIMESTAMP);

            ps.setInt(5, r.isNotificado2Dias() ? 1 : 0);
            ps.setInt(6, r.isNotificado1Hora() ? 1 : 0);

            int filas = ps.executeUpdate();
            if (filas == 0) return false;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) r.setId(rs.getInt(1));
            }
            return true;
        }
    }

    public List<reserva> listarTodas() throws SQLException {
        List<reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas";
        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_reserva");
                int usuarioId = rs.getInt("usuario_id");
                int salonId = rs.getInt("salon_id");
                Timestamp tsStart = rs.getTimestamp("hora_start");
                Timestamp tsEnd = rs.getTimestamp("hora_end");
                boolean n2 = rs.getInt("notificado_2dias") == 1;
                boolean n1 = rs.getInt("notificado_1hora") == 1;

                usuario u = new usuario(); u.setId(usuarioId);
                salon s = new salon(); s.setId(salonId);
                horario h = new horario();
                if (tsStart != null) h.setHoraStart(new Date(tsStart.getTime()));
                if (tsEnd != null) h.setHoraEnd(new Date(tsEnd.getTime()));

                reserva r = new reserva(u, s, h);
                r.setId(id);
                r.setNotificado2Dias(n2);
                r.setNotificado1Hora(n1);

                lista.add(r);
            }
        }
        return lista;
    }

    public boolean actualizarFlags(reserva r) throws SQLException {
        String sql = "UPDATE reservas SET notificado_2dias = ?, notificado_1hora = ? WHERE id_reserva = ?";
        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.isNotificado2Dias() ? 1 : 0);
            ps.setInt(2, r.isNotificado1Hora() ? 1 : 0);
            ps.setInt(3, r.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idReserva) throws SQLException {
        String sql = "DELETE FROM reservas WHERE id_reserva = ?";
        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            return ps.executeUpdate() > 0;
        }
    }
}