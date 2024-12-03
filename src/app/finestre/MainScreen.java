package app.finestre;

import app.Applicazione;
import app.layout.Pannello;
import app.layout.Pulsante;
import app.layout.Testo;

import javax.swing.*;
import java.awt.*;

/**
 * Schermata principale del programma
 */
public class MainScreen extends Pannello {

    public MainScreen() {
        int gap = 5;
        this.setLayout(new BorderLayout(gap, gap));
        this.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));

        this.add(new Testo("Seleziona un'opzione", SwingConstants.CENTER)
                .setArial(Font.BOLD, 24), BorderLayout.PAGE_START);

        JPanel buttons = new Pannello();
        buttons.add(new Pulsante("Trova classe e visibilità")
                .onClick(a -> Applicazione.getInstance().opzioneA()));
        buttons.add(new Pulsante("Controlla stessa S.M.")
                .onClick(a -> Applicazione.getInstance().opzioneB()));
        buttons.add(new Pulsante("Calcola sottoreti (S.M fissa)")
                .onClick(a -> Applicazione.getInstance().opzioneC()));
        buttons.add(new Pulsante("Calcola sottoreti (S.M variabile)")
                .onClick(a -> Applicazione.getInstance().opzioneD()));

        this.add(buttons, BorderLayout.CENTER);
    }

}
