package org.exc.sezionecommerciale;

import java.time.LocalDate;

public class Utente {
    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String ruolo;
    private LocalDate dataNascita;

    public Utente(int id, String nome, String cognome, String email, String ruolo, LocalDate dataNascita) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.ruolo = ruolo;
        this.dataNascita = dataNascita;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getEmail() { return email; }
    public String getRuolo() { return ruolo; }
    public LocalDate getDataNascita() { return dataNascita; }
}