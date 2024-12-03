package app.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PlaceholderInput extends JTextField {
    private String placeholder = null;

    public PlaceholderInput() {
        this.prepareInput();
    }

    public void prepareInput() {
        this.setBorder(null);
        this.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setForeground(Color.GRAY);
                    setText(placeholder);
                }
            }
        });
    }

    public void setPlaceholder(String placeholder) {
        boolean wasPlaceholder = getText().isEmpty() || getText().equals(this.placeholder);

        this.placeholder = placeholder;

        // Se il campo è vuoto o contiene il placeholder, allora aggiorna
        if (wasPlaceholder) {
            setText(placeholder);
            setForeground(Color.GRAY);
        }
    }

}
