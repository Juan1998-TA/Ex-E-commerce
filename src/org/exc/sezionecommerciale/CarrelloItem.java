package org.exc.sezionecommerciale;

public class CarrelloItem {
    private int idCarrello;
    private Prodotto prodotto;
    private int quantita;

    public CarrelloItem(int idCarrello, Prodotto prodotto, int quantita) {
        this.idCarrello = idCarrello;
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    public int getIdCarrello() { return idCarrello; }
    public Prodotto getProdotto() { return prodotto; }
    public int getQuantita() { return quantita; }
}