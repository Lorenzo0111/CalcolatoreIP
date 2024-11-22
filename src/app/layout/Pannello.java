package app.layout;

import javax.swing.*;
import java.awt.*;

public class Pannello extends JPanel {

    public Pannello() {
        this.setBackground(Color.BLACK);
    }

    public Pannello layout(LayoutManager layout) {
        super.setLayout(layout);
        return this;
    }
}
