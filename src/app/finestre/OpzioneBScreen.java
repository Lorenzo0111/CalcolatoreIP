package app.finestre;

import app.Applicazione;
import app.layout.Pannello;
import app.layout.Pulsante;
import app.layout.Testo;

import javax.swing.*;
import java.awt.*;

public class OpzioneBScreen extends Pannello {
    private final Pannello contenuto = new Pannello();

    public OpzioneBScreen() {
        this.setBackground(Color.BLACK);

        JPanel titolo = new Pannello();
        titolo.add(new Pulsante("Indietro")
                .onClick(a -> Applicazione.getInstance().mainScreen()));
        titolo.add(new Testo("Controlla stessa S.M.", SwingConstants.CENTER)
                .setArial(Font.BOLD, 24));

        Pannello contenutoPagina = new Pannello();
        contenutoPagina.layout(new BoxLayout(contenutoPagina, BoxLayout.Y_AXIS));

        Pannello inserisciIp = new Pannello();
        inserisciIp.layout(new GridLayout(4, 0));

        inserisciIp.add(new Testo("Inserisci l'indirizzo IP del server:"));

        JTextField ip1 = new JTextField();
        ip1.setSize(new Dimension(200, 30));
        inserisciIp.add(ip1);

        JTextField ip2 = new JTextField();
        ip2.setSize(new Dimension(200, 30));
        inserisciIp.add(ip2);

        JTextField sm = new JTextField();
        sm.setSize(new Dimension(200, 30));
        inserisciIp.add(sm);

        inserisciIp.add(new Pulsante("Conferma")
                .onClick(a -> esegui(ip1.getText(), ip2.getText(), sm.getText())), BorderLayout.PAGE_END);

        contenutoPagina.add(inserisciIp);
        contenutoPagina.add(contenuto);

        this.setLayout(new BorderLayout());

        this.add(titolo, BorderLayout.PAGE_START);
        this.add(contenutoPagina, BorderLayout.CENTER);
    }

    public void esegui(String ip1, String ip2, String sm) {

    }

}
