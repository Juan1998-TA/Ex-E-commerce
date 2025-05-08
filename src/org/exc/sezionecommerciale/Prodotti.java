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

            for (Prodotto prodotto : prodotti) {
                System.out.println(prodotto);
            }

        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
    }
}
