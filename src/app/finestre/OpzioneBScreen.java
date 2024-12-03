package app.finestre;

import app.Applicazione;
import app.errori.CalculatorException;
import app.layout.Pannello;
import app.layout.PlaceholderInput;
import app.layout.Pulsante;
import app.layout.Testo;
import app.utils.IPUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Schermata per l'opzione B
 */
public class OpzioneBScreen extends Pannello {
    private final Pannello contenuto = new Pannello();

    public OpzioneBScreen() {
        this.setBackground(Color.BLACK);

        // Titolo e pulsante per tornare indietro
        JPanel titolo = new Pannello();
        titolo.add(new Pulsante("Indietro")
                .onClick(a -> Applicazione.getInstance().mainScreen()));
        titolo.add(new Testo("Controlla stessa S.M.", SwingConstants.CENTER)
                .setArial(Font.BOLD, 24));

        // Contenuto
        Pannello contenutoPagina = new Pannello();
        contenutoPagina.layout(new BoxLayout(contenutoPagina, BoxLayout.Y_AXIS));

        Pannello inserisciIp = new Pannello();
        inserisciIp.setMaximumSize(new Dimension(400, 500));
        inserisciIp.layout(new GridLayout(5, 0, 4, 4));

        PlaceholderInput ip1 = new PlaceholderInput();
        ip1.setPlaceholder("Primo IP");
        ip1.setSize(new Dimension(200, 30));
        inserisciIp.add(ip1);

        PlaceholderInput ip2 = new PlaceholderInput();
        ip2.setPlaceholder("Secondo IP");
        ip2.setSize(new Dimension(200, 30));
        inserisciIp.add(ip2);

        PlaceholderInput sm1 = new PlaceholderInput();
        sm1.setPlaceholder("SM Primo IP");
        sm1.setSize(new Dimension(200, 30));
        inserisciIp.add(sm1);

        PlaceholderInput sm2 = new PlaceholderInput();
        sm2.setPlaceholder("SM Secondo IP");
        sm2.setSize(new Dimension(200, 30));
        inserisciIp.add(sm2);

        inserisciIp.add(new Pulsante("Conferma")
                .onClick(a -> onClick(ip1.getText(), ip2.getText(), sm1.getText(), sm2.getText())), BorderLayout.PAGE_END);

        contenutoPagina.add(inserisciIp);
        contenutoPagina.add(contenuto);

        this.setLayout(new BorderLayout());

        this.add(titolo, BorderLayout.PAGE_START);
        this.add(contenutoPagina, BorderLayout.CENTER);
    }

    private void onClick(String ip1, String ip2, String sm1, String sm2) {
        boolean stessoNetId = esegui(ip1, ip2, sm1, sm2);

        contenuto.removeAll();

        if (stessoNetId) contenuto.add(new Testo("I due indirizzi appartengono alla stessa sottorete."));
        else contenuto.add(new Testo("I due indirizzi non appartengono alla stessa sottorete."));

        contenuto.revalidate();
    }

    public static boolean esegui(String ip1, String ip2, String sm1, String sm2) {
        // Controllo se gli indirizzi IP e le subnet mask sono validi
        int[] ottetti1 = IPUtils.calcolaOttetti(ip1);
        int[] ottetti2 = IPUtils.calcolaOttetti(ip2);
        int[] ottettiSM1 = IPUtils.calcolaOttetti(sm1);
        int[] ottettiSM2 = IPUtils.calcolaOttetti(sm2);

        int[] netId1 = new int[4];
        int[] netId2 = new int[4];

        if (ottetti1 == null)
            throw new CalculatorException("Il primo indirizzo non è valido");

        if (ottetti2 == null)
            throw new CalculatorException("Il secondo indirizzo non è valido");

        if (ottettiSM1 == null)
            throw new CalculatorException("La prima subnet mask non è valida");

        if (ottettiSM2 == null)
            throw new CalculatorException("La seconda subnet mask non è valida");

        // Controllo se le subnet mask sono valide
        for (int i = 0; i < 4; i++) {
            netId1[i] = ottetti1[i] & ottettiSM1[i];
            netId2[i] = ottetti2[i] & ottettiSM2[i];
        }

        return netId1[0] == netId2[0] &&
                netId1[1] == netId2[1] &&
                netId1[2] == netId2[2] &&
                netId1[3] == netId2[3];
    }

}
