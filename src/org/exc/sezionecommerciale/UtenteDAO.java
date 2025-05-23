package org.exc.sezionecommerciale;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UtenteDAO {
    public Utente findByEmail(String email) throws SQLException {
        String sql = "SELECT id, nome, cognome, email, ruolo, data_nascita FROM utenti WHERE email = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate nascita = rs.getDate("data_nascita").toLocalDate();
                    return new Utente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("ruolo"),
                        nascita
                    );
                }
            }
        }
        return null;
    }
}