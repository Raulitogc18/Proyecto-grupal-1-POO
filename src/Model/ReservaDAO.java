package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    // Ahora incluimos las columnas de notificación para evitar el error de "no default value"
    private static final String INSERT_SQL =
        "INSERT INTO reservas (usuario_id, salon_id, hora_start, hora_end, notificado_2dias, notificado_1hora) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String LIST_SQL =
        "SELECT id_reserva, usuario_id, salon_id, hora_start, hora_end FROM reservas";

    public boolean insertar(Reserva r) {
        if (r == null) return false;
        try (Connection conn = Conexion.conectar()) {
            if (conn == null) return false;

            Integer usuarioId = (r.getUsuario() != null) ? r.getUsuario().getId() : null;
            Integer salonId = (r.getSalon() != null) ? r.getSalon().getId() : null;

            Timestamp tsInicio = null;
            Timestamp tsFin = null;
            if (r.getHorario() != null) {
                if (r.getHorario().getHoraStart() != null)
                    tsInicio = new Timestamp(r.getHorario().getHoraStart().getTime());
                if (r.getHorario().getHoraEnd() != null)
                    tsFin = new Timestamp(r.getHorario().getHoraEnd().getTime());
            }

            try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                if (usuarioId != null) ps.setInt(1, usuarioId); else ps.setNull(1, Types.INTEGER);
                if (salonId != null) ps.setInt(2, salonId); else ps.setNull(2, Types.INTEGER);
                if (tsInicio != null) ps.setTimestamp(3, tsInicio); else ps.setNull(3, Types.TIMESTAMP);
                if (tsFin != null) ps.setTimestamp(4, tsFin); else ps.setNull(4, Types.TIMESTAMP);

                // valores por defecto para columnas de notificación (0 = no notificado)
                ps.setInt(5, 0); // notificado_2dias
                ps.setInt(6, 0); // notificado_1hora

                int filas = ps.executeUpdate();
                if (filas > 0) {
                    try (ResultSet gk = ps.getGeneratedKeys()) {
                        if (gk != null && gk.next()) {
                            r.setId(gk.getInt(1)); // asigna id_reserva generado
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("ReservaDAO.insertar: error accediendo a BD: " + e.getMessage());
        }
        return false;
    }

    public List<Reserva> listarTodas() {
        List<Reserva> lista = new ArrayList<>();
        try (Connection conn = Conexion.conectar()) {
            if (conn == null) return lista;
            try (PreparedStatement ps = conn.prepareStatement(LIST_SQL);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reserva r = new Reserva();
                    r.setId(rs.getInt("id_reserva"));

                    int uid = rs.getInt("usuario_id");
                    if (!rs.wasNull()) {
                        Usuario u = new Usuario();
                        u.setId(uid);
                        r.setUsuario(u);
                    }

                    int sid = rs.getInt("salon_id");
                    if (!rs.wasNull()) {
                        Salon s = new Salon();
                        s.setId(sid);
                        r.setSalon(s);
                    }

                    Timestamp inicio = rs.getTimestamp("hora_start");
                    Timestamp fin = rs.getTimestamp("hora_end");
                    if (inicio != null) {
                        Horario h = new Horario(new java.util.Date(inicio.getTime()),
                                fin != null ? new java.util.Date(fin.getTime()) : null);
                        r.setHorario(h);
                    }

                    lista.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("ReservaDAO: error accediendo a BD: " + e.getMessage());
        }
        return lista;
    }

    public boolean marcarNotificado(int idReserva, boolean set2dias, boolean set1hora) {
        String sql = "UPDATE reservas SET ";
        List<String> parts = new ArrayList<>();
        if (set2dias) parts.add("notificado_2dias = 1");
        if (set1hora) parts.add("notificado_1hora = 1");
        if (parts.isEmpty()) return false;
        sql += String.join(", ", parts) + " WHERE id_reserva = ?";
        try (Connection conn = Conexion.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("ReservaDAO.marcarNotificado: " + e.getMessage());
            return false;
        }
    }
}