package org.exc.sezionecommerciale;

public class Prodotto {
    private int id;
    private String nome;
    private double prezzo;

    public Prodotto(int id, String nome, double prezzo) {
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPrezzo() { return prezzo; }
}