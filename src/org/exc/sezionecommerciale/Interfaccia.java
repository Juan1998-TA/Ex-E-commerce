package org.exc.sezionecommerciale;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class Interfaccia extends JFrame {
    private static final long serialVersionUID = 1L;
    private Utente utente;
    private ProdottoDAO prodottiDAO;
    private CarrelloDAO carrelloDAO;
    private List<Prodotto> catalogo;
    private List<CarrelloItem> carrello;
    private DefaultTableModel catalogModel;
    private DefaultTableModel cartModel;
    private JTable catalogTable;
    private JTable cartTable;

    public Interfaccia(Utente u) {
        this.utente = u;
        prodottiDAO = new ProdottoDAO();
        carrelloDAO = new CarrelloDAO();
        loadData();
        initUI();
    }

    private void loadData() {
        try {
            catalogo = prodottiDAO.findAll();
            carrello = carrelloDAO.findByUser(utente.getId());
        } catch (SQLException e) {
            catalogo = List.of();
            carrello = List.of();
            JOptionPane.showMessageDialog(this, "Errore DB: " + e.getMessage());
        }
    }

    private void initUI() {
        setTitle("E-commerce: " + utente.getNome());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabPane = new JTabbedPane();
        tabPane.add("Catalogo", createCatalogPanel());
        tabPane.add("Carrello", createCartPanel());
        add(tabPane);
    }

    private JPanel createCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        catalogModel = new DefaultTableModel(new Object[]{"ID","Prodotto","Prezzo (€)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        catalogTable = new JTable(catalogModel);
        refreshCatalog();
        panel.add(new JScrollPane(catalogTable), BorderLayout.CENTER);

        JButton addButton = new JButton("Aggiungi al carrello");
        addButton.addActionListener(e -> {
            int sel = catalogTable.getSelectedRow();
            if (sel >= 0) {
                Prodotto p = catalogo.get(sel);
                try {
                    carrelloDAO.addItem(utente.getId(), p.getId());
                    refreshCart();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Errore DB: " + ex.getMessage());
                }
            }
        });
        panel.add(addButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        cartModel = new DefaultTableModel(new Object[]{"CartID","Nome","Qt","Subtotale (€)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        cartTable = new JTable(cartModel);
        refreshCart();
        panel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton removeButton = new JButton("Rimuovi");
        removeButton.addActionListener(e -> {
            int sel = cartTable.getSelectedRow();
            if (sel >= 0) {
                int cartId = (int) cartModel.getValueAt(sel, 0);
                try {
                    carrelloDAO.removeItem(cartId);
                    refreshCart();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Errore DB: " + ex.getMessage());
                }
            }
        });
        JButton totalButton = new JButton("Totale");
        totalButton.addActionListener(e -> {
            double total = carrello.stream().mapToDouble(item -> item.getProdotto().getPrezzo() * item.getQuantita()).sum();
            JOptionPane.showMessageDialog(this, String.format("Spesa totale: %.2f €", total));
        });
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            Interfaccia.main(new String[0]);
        });
        buttons.add(removeButton);
        buttons.add(totalButton);
        buttons.add(logoutButton);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshCatalog() {
        catalogModel.setRowCount(0);
        catalogo.forEach(p -> catalogModel.addRow(new Object[]{p.getId(), p.getNome(), p.getPrezzo()}));
    }

    private void refreshCart() {
        try { carrello = carrelloDAO.findByUser(utente.getId()); } catch (SQLException e) {}
        cartModel.setRowCount(0);
        cartello: for (CarrelloItem ci : carrello) {
            cartModel.addRow(new Object[]{
                ci.getIdCarrello(), ci.getProdotto().getNome(),
                ci.getQuantita(), ci.getProdotto().getPrezzo() * ci.getQuantita()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String email = JOptionPane.showInputDialog(
                null, "Email:", "Login", JOptionPane.QUESTION_MESSAGE);
            if (email == null) return;
            try {
                Utente u = new UtenteDAO().findByEmail(email.trim());
                if (u == null) {
                    JOptionPane.showMessageDialog(null, "Email non valida");
                } else {
                    new Interfaccia(u).setVisible(true);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Errore DB: " + ex.getMessage());
            }
        });
    }
}