package org.exc.sezionecommerciale;

import java.io.PrintWriter;
import java.io.IOException;

public class Carrello {
    public static void genera() {
        try (PrintWriter writer = new PrintWriter("carrelli.txt")) {
            writer.println("Carrelli utenti:\n");

            writer.println("Utente: Giovanni Rossi");
            writer.println("Prodotto 1: Jeans (Carrera) - Prezzo: 29.99");
            writer.println("Prodotto 2: Pantaloncino (Levis) - Prezzo: 13.99\n");

            writer.println("Utente: Laura Bianchi");
            writer.println("Prodotto 1: Jeans (Levis) - Prezzo: 39.99");
            writer.println("Prodotto 2: Pantaloncino (Diesel) - Prezzo: 9.99\n");

            writer.println("Utente: Luca Verdi");
            writer.println("Prodotto 1: Pantaloncino (Diesel) - Prezzo: 9.99\n");

            System.out.println("Carrelli di esempio salvati in carrelli.txt.");
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file");
        }
    }
}package org.exc.sezionecommerciale;

import java.io.PrintWriter;
import java.io.IOException;

public class Carrello {
    public static void genera() {
        try (PrintWriter writer = new PrintWriter("carrelli.txt")) {
            writer.println("Carrelli utenti:\n");

            writer.println("Utente: Giovanni Rossi");
            writer.println("Prodotto 1: Jeans (Carrera) - Prezzo: 29.99");
            writer.println("Prodotto 2: Pantaloncino (Levis) - Prezzo: 13.99\n");

            writer.println("Utente: Laura Bianchi");
            writer.println("Prodotto 1: Jeans (Levis) - Prezzo: 39.99");
            writer.println("Prodotto 2: Pantaloncino (Diesel) - Prezzo: 9.99\n");

            writer.println("Utente: Luca Verdi");
            writer.println("Prodotto 1: Pantaloncino (Diesel) - Prezzo: 9.99\n");

            System.out.println("Carrelli salvati in carrelli.txt.");
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file");
        }
    }
}
