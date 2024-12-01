package app.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class Pulsante extends JButton {

    public Pulsante(String testo) {
        super(testo);

        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setOpaque(true);
        this.setForeground(Color.WHITE);
        this.setBackground(Color.DARK_GRAY);

        this.setSize(10, 20);
    }

    public Pulsante onClick(Consumer<ActionEvent> action) {
        this.addActionListener(action::accept);
        return this;
    }

    public Pulsante setDisabled() {
        this.setEnabled(false);
        this.setBackground(Color.GRAY);
        return this;
    }

    public Pulsante setEnabled() {
        this.setEnabled(true);
        this.setBackground(Color.DARK_GRAY);
        return this;
    }
}
