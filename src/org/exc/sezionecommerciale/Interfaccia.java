package org.exc.sezionecommerciale;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interfaccia extends JFrame {

    private JTextField emailField;
    private JLabel statoLabel;
    private JTextArea carrelloArea;
    private JButton logoutButton;
    private JButton aggiungiButton;
    private JButton rimuoviButton;
    private JComboBox<String> prodottiComboBox;
    
    private String emailCorrente = null;
    private List<Prodotti.Prodotto> prodottiDisponibili;
    private Map<String, List<Prodotti.Prodotto>> carrelli = new HashMap<>();
    private Map<String, String> utenti = new HashMap<>();

    public Interfaccia() {
        setTitle("Gestione Carrello Utente");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Carica prodotti e utenti
        prodottiDisponibili = Prodotti.caricaProdotti();
        caricaUtenti();
        caricaCarrelli();

        // Pannello di login (in alto)
        JPanel loginPanel = new JPanel();
        loginPanel.add(new JLabel("Inserisci email:"));
        emailField = new JTextField(25);
        loginPanel.add(emailField);
        JButton accediButton = new JButton("Accedi");
        loginPanel.add(accediButton);
        add(loginPanel, BorderLayout.NORTH);

        // Area centrale (carrello)
        carrelloArea = new JTextArea();
        carrelloArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(carrelloArea);
        add(scrollPane, BorderLayout.CENTER);

        // Pannello per la gestione del carrello (sotto il carrello)
        JPanel gestionePanel = new JPanel();
        gestionePanel.setBorder(BorderFactory.createTitledBorder("Gestione Carrello"));
        
        // Cambiamo il layout da FlowLayout a GridBagLayout per un controllo migliore
        gestionePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Etichetta "Prodotti:"
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gestionePanel.add(new JLabel("Prodotti:"), gbc);
        
        // ComboBox prodotti
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        prodottiComboBox = new JComboBox<>();
        aggiornaComboBoxProdotti();
        prodottiComboBox.setPreferredSize(new Dimension(250, 25));
        gestionePanel.add(prodottiComboBox, gbc);
        
        // Pulsante "Aggiungi al carrello"
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        aggiungiButton = new JButton("Aggiungi al carrello");
        aggiungiButton.setEnabled(false);
        gestionePanel.add(aggiungiButton, gbc);
        
        // Pulsante "Rimuovi prodotto"
        gbc.gridx = 3;
        gbc.weightx = 0.2;
        rimuoviButton = new JButton("Rimuovi prodotto");
        rimuoviButton.setEnabled(false);
        gestionePanel.add(rimuoviButton, gbc);
        
        // Aggiungiamo il pannello di gestione alla parte sud, sopra la barra di stato
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(gestionePanel, BorderLayout.CENTER);

        // Pannello inferiore (stato + logout)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        statoLabel = new JLabel("Stato: offline");
        bottomPanel.add(statoLabel, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel();
        logoutButton = new JButton("Logout");
        logoutButton.setEnabled(false);
        buttonPanel.add(logoutButton);
        
        JButton salvaButton = new JButton("Salva Carrelli");
        buttonPanel.add(salvaButton);
        
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        southPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(southPanel, BorderLayout.SOUTH);

        // Azioni pulsanti
        accediButton.addActionListener(e -> accedi());
        logoutButton.addActionListener(e -> logout());
        aggiungiButton.addActionListener(e -> aggiungiAlCarrello());
        rimuoviButton.addActionListener(e -> rimuoviDalCarrello());
        salvaButton.addActionListener(e -> salvaCarrelli());

        setVisible(true);
    }

    private void aggiornaComboBoxProdotti() {
        prodottiComboBox.removeAllItems();
        for (Prodotti.Prodotto prodotto : prodottiDisponibili) {
            prodottiComboBox.addItem(prodotto.toString());
        }
    }

    private void caricaUtenti() {
        try (BufferedReader reader = new BufferedReader(new FileReader("utenti.txt"))) {
            reader.readLine(); // intestazione
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 4) { // almeno ID, Nome, Cognome, Email
                    String email = data[3];
                    String nomeCompleto = data[1] + " " + data[2];
                    utenti.put(email, nomeCompleto);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file utenti: " + e.getMessage());
            // Carica dati di esempio se il file non esiste
            utenti.put("giovanni@example.com", "Giovanni Rossi");
            utenti.put("laura.rossi@example.com", "Laura Bianchi");
            utenti.put("luca.verdi@example.com", "Luca Verdi");
        }
    }

    private void caricaCarrelli() {
        try (BufferedReader reader = new BufferedReader(new FileReader("carrelli.txt"))) {
            String line;
            String emailUtente = null;
            List<Prodotti.Prodotto> carrelloCorrente = null;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                if (line.startsWith("Utente:")) {
                    // Salva il carrello precedente se presente
                    if (emailUtente != null && carrelloCorrente != null) {
                        carrelli.put(emailUtente, carrelloCorrente);
                    }
                    
                    // Estrai il nome utente e cerca l'email corrispondente
                    String nomeCompleto = line.substring("Utente:".length()).trim();
                    emailUtente = trovaEmailDaNome(nomeCompleto);
                    
                    if (emailUtente != null) {
                        carrelloCorrente = new ArrayList<>();
                    } else {
                        System.out.println("Email non trovata per l'utente: " + nomeCompleto);
                        carrelloCorrente = null;
                    }
                } else if (line.startsWith("Prodotto") && emailUtente != null && carrelloCorrente != null) {
                    try {
                        // Estrai informazioni prodotto
                        int inizio = line.indexOf(":") + 1;
                        int fine = line.indexOf(" - Prezzo:");
                        if (inizio > 0 && fine > inizio) {
                            String prodottoInfo = line.substring(inizio, fine).trim();
                            String tipo = prodottoInfo.substring(0, prodottoInfo.indexOf(" ("));
                            String marca = prodottoInfo.substring(prodottoInfo.indexOf("(") + 1, prodottoInfo.indexOf(")"));
                            
                            String prezzoStr = line.substring(line.lastIndexOf(":") + 1).trim();
                            prezzoStr = prezzoStr.replace("€", "").trim(); // Rimuove il simbolo dell'euro se presente
                            double prezzo = Double.parseDouble(prezzoStr);
                            
                            carrelloCorrente.add(new Prodotti.Prodotto(tipo, tipo, prezzo, marca));
                        }
                    } catch (Exception e) {
                        System.out.println("Errore nel parsing della linea del carrello: " + line);
                        System.out.println(e.getMessage());
                    }
                }
            }
            
            // Salva l'ultimo carrello
            if (emailUtente != null && carrelloCorrente != null) {
                carrelli.put(emailUtente, carrelloCorrente);
            }
            
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file carrelli: " + e.getMessage());
            // I carrelli saranno inizializzati vuoti
            
            // Informa l'utente che si stanno usando carrelli vuoti
            System.out.println("Inizializzazione di carrelli vuoti per tutti gli utenti.");
            for (String email : utenti.keySet()) {
                carrelli.put(email, new ArrayList<>());
            }
        }
    }
    
    private String trovaEmailDaNome(String nomeCompleto) {
        for (Map.Entry<String, String> entry : utenti.entrySet()) {
            if (entry.getValue().equals(nomeCompleto)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void accedi() {
        String email = emailField.getText().trim();

        if (!utenti.containsKey(email)) {
            JOptionPane.showMessageDialog(this, "Email non riconosciuta.");
            return;
        }

        emailCorrente = email;
        String nomeCompleto = utenti.get(email);
        
        // Assicurati che ci sia un carrello per questo utente
        if (!carrelli.containsKey(email)) {
            carrelli.put(email, new ArrayList<>());
        }
        
        aggiornaVistaCarrello();
        
        statoLabel.setText("Stato: online (" + nomeCompleto + ")");
        logoutButton.setEnabled(true);
        aggiungiButton.setEnabled(true);
        rimuoviButton.setEnabled(true);
        
        JOptionPane.showMessageDialog(this, "Accesso effettuato come " + nomeCompleto);
    }
    
    private void aggiornaVistaCarrello() {
        if (emailCorrente == null) return;
        
        List<Prodotti.Prodotto> prodotti = carrelli.get(emailCorrente);
        String nomeCompleto = utenti.get(emailCorrente);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Carrello di ").append(nomeCompleto).append(":\n\n");

        double totale = 0;
        for (int i = 0; i < prodotti.size(); i++) {
            Prodotti.Prodotto p = prodotti.get(i);
            totale += p.prezzo;

            sb.append("Prodotto ").append(i + 1).append(": ")
              .append(p.nome).append(" (").append(p.marca)
              .append(") - Prezzo: ").append(String.format("%.2f", p.prezzo)).append("€\n");
        }

        sb.append("\nTotale: ").append(String.format("%.2f", totale)).append("€");

        carrelloArea.setText(sb.toString());
    }

    private void aggiungiAlCarrello() {
        if (emailCorrente == null) return;
        
        int indexProdotto = prodottiComboBox.getSelectedIndex();
        if (indexProdotto >= 0 && indexProdotto < prodottiDisponibili.size()) {
            Prodotti.Prodotto prodottoSelezionato = prodottiDisponibili.get(indexProdotto);
            carrelli.get(emailCorrente).add(prodottoSelezionato);
            aggiornaVistaCarrello();
        }
    }
    
    private void rimuoviDalCarrello() {
        if (emailCorrente == null) return;
        
        String input = JOptionPane.showInputDialog(this, 
                "Inserisci il numero del prodotto da rimuovere:", 
                "Rimozione Prodotto", 
                JOptionPane.QUESTION_MESSAGE);
        
        try {
            int numeroProdotto = Integer.parseInt(input);
            List<Prodotti.Prodotto> carrelloUtente = carrelli.get(emailCorrente);
            
            if (numeroProdotto > 0 && numeroProdotto <= carrelloUtente.size()) {
                carrelloUtente.remove(numeroProdotto - 1);
                aggiornaVistaCarrello();
            } else {
                JOptionPane.showMessageDialog(this, "Numero prodotto non valido.");
            }
        } catch (NumberFormatException | NullPointerException e) {
            // L'utente ha annullato o inserito un valore non valido
        }
    }

    private void logout() {
        emailCorrente = null;
        emailField.setText("");
        carrelloArea.setText("");
        statoLabel.setText("Stato: offline");
        logoutButton.setEnabled(false);
        aggiungiButton.setEnabled(false);
        rimuoviButton.setEnabled(false);
    }
    
    private void salvaCarrelli() {
        try (PrintWriter writer = new PrintWriter("carrelli.txt")) {
            writer.println("Carrelli utenti:\n");

            for (Map.Entry<String, List<Prodotti.Prodotto>> entry : carrelli.entrySet()) {
                String email = entry.getKey();
                List<Prodotti.Prodotto> prodotti = entry.getValue();
                
                if (utenti.containsKey(email)) {
                    writer.println("Utente: " + utenti.get(email));
                    
                    for (int i = 0; i < prodotti.size(); i++) {
                        Prodotti.Prodotto p = prodotti.get(i);
                        writer.println("Prodotto " + (i+1) + ": " + p.nome + " (" + p.marca + ") - Prezzo: " + p.prezzo);
                    }
                    
                    writer.println();
                }
            }

            JOptionPane.showMessageDialog(this, "Carrelli salvati con successo in carrelli.txt.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Errore nel salvataggio dei carrelli: " + e.getMessage(), 
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Creare file di esempio se non esistono
        Utenti.genera();
        Carrello.genera();
        Prodotti.generaEsempio();
        
        SwingUtilities.invokeLater(Interfaccia::new);
    }
}