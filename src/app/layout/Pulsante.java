package app.layout;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class Pulsante extends JButton {

    public Pulsante(String testo) {
        super(testo);

        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setOpaque(true);
        this.setForeground(Color.WHITE);
        this.setBackground(Color.CYAN);

        this.setSize(10, 20);
    }

    public Pulsante onClick(Consumer<Void> action) {
        this.addActionListener(e -> action.accept(null));
        return this;
    }
}
