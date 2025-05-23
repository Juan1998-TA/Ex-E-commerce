package org.exc.sezionecommerciale;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdottoDAO {
    public List<Prodotto> findAll() throws SQLException {
        List<Prodotto> prodotti = new ArrayList<>();
        String sql = "SELECT id_prodotto, nome_prodotto, prezzo FROM prodotti";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                prodotti.add(new Prodotto(
                    rs.getInt("id_prodotto"),
                    rs.getString("nome_prodotto"),
                    rs.getDouble("prezzo")
                ));
            }
        }
        return prodotti;
    }
}