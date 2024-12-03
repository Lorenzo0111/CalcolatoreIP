package app.finestre;

import app.Applicazione;
import app.errori.CalculatorException;
import app.ip.Classe;
import app.layout.Pannello;
import app.layout.PlaceholderInput;
import app.layout.Pulsante;
import app.layout.Testo;
import app.utils.IPUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Schermata per l'opzione A
 */
public class OpzioneAScreen extends Pannello {
    private final Pannello contenuto = new Pannello();

    public OpzioneAScreen() {
        this.setBackground(Color.BLACK);

        // Titolo e pulsante per tornare indietro
        JPanel titolo = new Pannello();
        titolo.add(new Pulsante("Indietro")
                .onClick(a -> Applicazione.getInstance().mainScreen()));
        titolo.add(new Testo("Trova classe e visibilità", SwingConstants.CENTER)
                .setArial(Font.BOLD, 24));

        // Contenuto
        Pannello contenutoPagina = new Pannello();
        contenutoPagina.layout(new BoxLayout(contenutoPagina, BoxLayout.Y_AXIS));

        Pannello inserisciIp = new Pannello();
        inserisciIp.setMaximumSize(new Dimension(400, 500));
        inserisciIp.layout(new GridLayout(5, 0, 4, 4));
        inserisciIp.add(new Testo("Inserisci l'indirizzo IP del server:"));

        JTextField ip = new PlaceholderInput();
        ip.setPreferredSize(new Dimension(200, 30));
        inserisciIp.add(ip);

        inserisciIp.add(new Pulsante("Conferma")
                .onClick(a -> esegui(ip.getText())), BorderLayout.PAGE_END);

        contenutoPagina.add(inserisciIp);
        contenutoPagina.add(contenuto);

        this.setLayout(new BorderLayout());

        this.add(titolo, BorderLayout.PAGE_START);
        this.add(contenutoPagina, BorderLayout.CENTER);
    }

    public void esegui(String ip) {
        // Controllo se l'indirizzo IP è valido
        int[] ottetti = IPUtils.calcolaOttetti(ip);
        if (ottetti == null)
            throw new CalculatorException("Inserisci un indirizzo IP valido");

        // Calcolo classe e visibilità
        Classe classe = IPUtils.calcolaClasse(ottetti);
        boolean privateIp = IPUtils.privato(ottetti);

        if (classe == null)
            throw new CalculatorException("Classe non valida");

        // Aggiorno il contenuto
        String visibilita = privateIp ? "privato" : "pubblico";

        contenuto.removeAll();
        contenuto.add(new Testo("Classe: " + classe.name()));
        contenuto.add(new Testo("Visibilità: " + visibilita));
        contenuto.revalidate();
    }

}
