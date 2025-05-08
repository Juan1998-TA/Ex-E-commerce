package org.exc.sezionecommerciale;

import java.io.PrintWriter;
import java.io.IOException;

public class Utenti {
    public static void genera() {
        try (PrintWriter writer = new PrintWriter("utenti.txt")) {
            writer.println("ID;Nome;Cognome;Email;Indirizzo");
            writer.println("1;Giovanni;Rossi;giovanni@example.com;Via Roma 1");
            writer.println("2;Laura;Bianchi;laura.rossi@example.com;Via Lazio 3");
            writer.println("3;Luca;Verdi;luca.verdi@example.com;Via Pisa 109");
            System.out.println("Dati degli utenti salvati in utenti.txt.");
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file");
        }
    }
}
