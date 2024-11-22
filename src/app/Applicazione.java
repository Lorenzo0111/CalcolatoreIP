package app;

import app.finestre.MainScreen;
import app.finestre.OpzioneAScreen;
import app.finestre.OpzioneBScreen;

import javax.swing.*;

public class Applicazione {
    private static Applicazione instance;
    private final JFrame frame;

    public Applicazione() {
        instance = this;

        this.frame = new JFrame("Calcolatore IP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void mainScreen() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new MainScreen());
        frame.revalidate();
    }

    public void opzioneA() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new OpzioneAScreen());
        frame.revalidate();
    }

    public void opzioneB() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new OpzioneBScreen());
        frame.revalidate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Applicazione::create);
    }

    public static void create() {
        new Applicazione().mainScreen();
    }

    public static Applicazione getInstance() {
        return instance;
    }
}