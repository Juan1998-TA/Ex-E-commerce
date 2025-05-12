package org.exc.sezionecommerciale;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Prodotti {

    static class Prodotto {
        String nome;
        String tipo;
        double prezzo;
        String marca;

        public Prodotto(String nome, String tipo, double prezzo, String marca) {
            this.nome = nome;
            this.tipo = tipo;
            this.prezzo = prezzo;
            this.marca = marca;
        }

        @Override
        public String toString() {
            return nome + " (" + marca + ") - " + tipo + " - " + prezzo + "€";
        }
    }

    public static void visualizza() {
        List<Prodotto> prodotti = caricaProdotti();

        for (Prodotto prodotto : prodotti) {
            System.out.println(prodotto);
        }
    }
    
    public static List<Prodotto> caricaProdotti() {
        List<Prodotto> prodotti = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("prodotti.txt"))) {
            reader.readLine(); // intestazione

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 4) {
                    String nome = data[0];
                    String tipo = data[1];
                    double prezzo = Double.parseDouble(data[2]);
                    String marca = data[3];
                    prodotti.add(new Prodotto(nome, tipo, prezzo, marca));
                }
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file prodotti: " + e.getMessage());
            // Se c'è un errore, aggiungiamo alcuni prodotti di esempio
            prodotti.add(new Prodotto("Jeans", "Abbigliamento", 29.99, "Carrera"));
            prodotti.add(new Prodotto("Pantaloncino", "Abbigliamento", 13.99, "Levis"));
            prodotti.add(new Prodotto("Jeans", "Abbigliamento", 39.99, "Levis"));
            prodotti.add(new Prodotto("Pantaloncino", "Abbigliamento", 9.99, "Diesel"));
            prodotti.add(new Prodotto("T-Shirt", "Abbigliamento", 15.99, "Fruit"));
            prodotti.add(new Prodotto("Polo", "Abbigliamento", 25.99, "Ralph Lauren"));
        }

        return prodotti;
    }
    
    public static void generaEsempio() {
        try (PrintWriter writer = new PrintWriter("prodotti.txt")) {
            writer.println("Nome;Tipo;Prezzo;Marca");
            writer.println("Jeans;Abbigliamento;29.99;Carrera");
            writer.println("Pantaloncino;Abbigliamento;13.99;Levis");
            writer.println("Jeans;Abbigliamento;39.99;Levis");
            writer.println("Pantaloncino;Abbigliamento;9.99;Diesel");
            writer.println("T-Shirt;Abbigliamento;15.99;Fruit");
            writer.println("Polo;Abbigliamento;25.99;Ralph Lauren");
            writer.println("Giacca;Abbigliamento;49.99;North Face");
            writer.println("Scarpe;Calzature;59.99;Nike");
            writer.println("Sandali;Calzature;19.99;Birkenstock");
            writer.println("Cappello;Accessori;12.99;Adidas");
            System.out.println("Prodotti di esempio salvati in prodotti.txt.");
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file prodotti");
        }
    }
}