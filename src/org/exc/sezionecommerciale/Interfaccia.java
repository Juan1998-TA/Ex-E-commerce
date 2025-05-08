package org.exc.sezionecommerciale;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Interfaccia extends JFrame {

    private JTextField emailField;
    private JLabel statoLabel;
    private JTextArea carrelloArea;
    private JButton logoutButton;

    private HashMap<String, String[][]> carrelli = new HashMap<>();
    private HashMap<String, String> utenti = new HashMap<>();

    public Interfaccia() {
        setTitle("Accesso Carrello Utente");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inizializza dati
        inizializzaDati();

        // Pannello superiore (email + pulsante accedi)
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Inserisci email:"));
        emailField = new JTextField(25);
        topPanel.add(emailField);
        JButton accediButton = new JButton("Accedi");
        topPanel.add(accediButton);
        add(topPanel, BorderLayout.NORTH);

        // Area centrale (carrello)
        carrelloArea = new JTextArea();
        carrelloArea.setEditable(false);
        add(new JScrollPane(carrelloArea), BorderLayout.CENTER);

        // Pannello inferiore (stato + logout)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        statoLabel = new JLabel("Stato: offline");
        bottomPanel.add(statoLabel, BorderLayout.WEST);
        logoutButton = new JButton("Logout");
        logoutButton.setEnabled(false);
        bottomPanel.add(logoutButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Azioni pulsanti
        accediButton.addActionListener(e -> accedi());
        logoutButton.addActionListener(e -> logout());

        setVisible(true);
    }

    private void accedi() {
        String email = emailField.getText().trim();

        if (!utenti.containsKey(email)) {
            JOptionPane.showMessageDialog(this, "Email non riconosciuta.");
            return;
        }

        String nomeCompleto = utenti.get(email);
        String[][] prodotti = carrelli.get(email);

        StringBuilder sb = new StringBuilder();
        sb.append("Carrello di ").append(nomeCompleto).append(":\n\n");

        double totale = 0;
        for (int i = 0; i < prodotti.length; i++) {
            String tipo = prodotti[i][0];
            String marca = prodotti[i][1];
            double prezzo = Double.parseDouble(prodotti[i][2]);
            totale += prezzo;

            sb.append("Prodotto ").append(i + 1).append(": ")
              .append(tipo).append(" (").append(marca)
              .append(") - Prezzo: ").append(String.format("%.2f", prezzo)).append("€\n");
        }

        sb.append("\nTotale: ").append(String.format("%.2f", totale)).append("€");

        carrelloArea.setText(sb.toString());
        statoLabel.setText("Stato: online (" + nomeCompleto + ")");
        logoutButton.setEnabled(true);
    }

    private void logout() {
        emailField.setText("");
        carrelloArea.setText("");
        statoLabel.setText("Stato: offline");
        logoutButton.setEnabled(false);
    }

    private void inizializzaDati() {
        // Mappa utenti: email -> nome completo
        utenti.put("giovanni@example.com", "Giovanni Rossi");
        utenti.put("laura.rossi@example.com", "Laura Bianchi");
        utenti.put("luca.verdi@example.com", "Luca Verdi");

        // Carrelli associati agli utenti
        carrelli.put("giovanni@example.com", new String[][]{
            {"Jeans", "Carrera", "29.99"},
            {"Pantaloncino", "Levis", "13.99"}
        });

        carrelli.put("laura.rossi@example.com", new String[][]{
            {"Jeans", "Levis", "39.99"},
            {"Pantaloncino", "Diesel", "9.99"}
        });

        carrelli.put("luca.verdi@example.com", new String[][]{
            {"Pantaloncino", "Diesel", "9.99"}
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Interfaccia::new);
    }
}
