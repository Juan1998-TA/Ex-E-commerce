package org.exc.sezionecommerciale;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarrelloDAO {
    public List<CarrelloItem> findByUser(int idUtente) throws SQLException {
        List<CarrelloItem> items = new ArrayList<>();
        String sql = "SELECT c.id_carrello, p.id_prodotto, p.nome_prodotto, p.prezzo, c.quantita " +
                     "FROM carrello c JOIN prodotti p ON c.id_prodotto = p.id_prodotto " +
                     "WHERE c.id_utente = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new CarrelloItem(
                        rs.getInt("id_carrello"),
                        new Prodotto(
                            rs.getInt("id_prodotto"),
                            rs.getString("nome_prodotto"),
                            rs.getDouble("prezzo")
                        ),
                        rs.getInt("quantita")
                    ));
                }
            }
        }
        return items;
    }

    
    public void addItem(int idUtente, int idProdotto) throws SQLException {
        String sel = "SELECT id_carrello, quantita FROM carrello WHERE id_utente=? AND id_prodotto=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement psSel = conn.prepareStatement(sel)) {
            psSel.setInt(1, idUtente);
            psSel.setInt(2, idProdotto);
            try (ResultSet rs = psSel.executeQuery()) {
                if (rs.next()) {
                    int idCart = rs.getInt("id_carrello");
                    int old = rs.getInt("quantita");
                    String upd = "UPDATE carrello SET quantita=? WHERE id_carrello=?";
                    try (PreparedStatement psUpd = conn.prepareStatement(upd)) {
                        psUpd.setInt(1, old + 1);
                        psUpd.setInt(2, idCart);
                        psUpd.executeUpdate();
                    }
                    return;
                }
            }
            String ins = "INSERT INTO carrello(id_utente,id_prodotto,quantita) VALUES(?,?,1)";
            try (PreparedStatement psIns = conn.prepareStatement(ins)) {
                psIns.setInt(1, idUtente);
                psIns.setInt(2, idProdotto);
                psIns.executeUpdate();
            }
        }
    }

    public void removeItem(int idCarrello) throws SQLException {
        String del = "DELETE FROM carrello WHERE id_carrello=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(del)) {
            ps.setInt(1, idCarrello);
            ps.executeUpdate();
        }
    }
}