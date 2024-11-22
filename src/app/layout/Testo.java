package app.layout;

import javax.swing.*;
import java.awt.*;

public class Testo extends JLabel {

    public Testo(String testo) {
        super(testo);
        this.setForeground(Color.WHITE);
        this.setArial(Font.PLAIN, 16);
    }

    public Testo(String testo, int position) {
        super(testo, position);
        this.setForeground(Color.WHITE);
        this.setArial(Font.PLAIN, 16);
    }

    public Testo setArial(int weight, int size) {
        this.setFont(new Font("Arial", weight, size));
        return this;
    }
}
