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

public class OpzioneCScreen extends Pannello {
    private final Pannello contenuto = new Pannello();

    public OpzioneCScreen() {
        this.setBackground(Color.BLACK);

        JPanel titolo = new Pannello();
        titolo.add(new Pulsante("Indietro")
                .onClick(a -> Applicazione.getInstance().mainScreen()));
        titolo.add(new Testo("Calcola sottreti", SwingConstants.CENTER)
                .setArial(Font.BOLD, 24));

        Pannello contenutoPagina = new Pannello();
        contenutoPagina.layout(new BoxLayout(contenutoPagina, BoxLayout.Y_AXIS));

        Pannello inserisciIp = new Pannello();
        inserisciIp.setMaximumSize(new Dimension(400, 500));
        inserisciIp.layout(new GridLayout(3, 0, 4, 4));

        PlaceholderInput ip = new PlaceholderInput();
        ip.setPlaceholder("Indirizzo IP");
        ip.setSize(new Dimension(200, 30));
        inserisciIp.add(ip);

        PlaceholderInput reti = new PlaceholderInput();

        reti.setPlaceholder("Numero reti");
        reti.setSize(new Dimension(200, 30));
        inserisciIp.add(reti);

        inserisciIp.add(new Pulsante("Conferma")
                .onClick(a -> {
                    int numReti;

                    try {
                        numReti = Integer.parseInt(reti.getText());
                    } catch (NumberFormatException e) {
                        throw new CalculatorException("Il numero di reti inserite non è valido");
                    }

                    esegui(ip.getText(), numReti);
                }), BorderLayout.PAGE_END);

        contenutoPagina.add(inserisciIp);
        contenutoPagina.add(contenuto);

        this.setLayout(new BorderLayout());

        this.add(titolo, BorderLayout.PAGE_START);
        this.add(contenutoPagina, BorderLayout.CENTER);
    }

    public void esegui(String ip, int numSottoreti) {
        int[] ottetti = IPUtils.calcolaOttetti(ip);

        if (ottetti == null)
            throw new CalculatorException("L'indirizzo ip inserito non è valido");

        Classe classe = IPUtils.calcolaClasse(ottetti);
        if (classe == null) throw new CalculatorException("La classe non è valida");

        int bitSottoreti = (int) Math.ceil(IPUtils.log2(numSottoreti));
        int bitHostSottrete = classe.getBitHost() - bitSottoreti;


    }

}
