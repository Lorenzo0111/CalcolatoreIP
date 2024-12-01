package app;

import app.errori.CalculatorException;
import app.finestre.*;

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

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            if (!(e instanceof CalculatorException))
                e.printStackTrace();

            JOptionPane.showMessageDialog(null, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        });
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

    public void opzioneC() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new OpzioneCScreen());
        frame.revalidate();
    }

    public void opzioneD() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new OpzioneDScreen());
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