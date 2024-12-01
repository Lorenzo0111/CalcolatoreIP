package app.finestre;

import app.Applicazione;
import app.errori.CalculatorException;
import app.ip.Sottorete;
import app.layout.Pannello;
import app.layout.Pulsante;
import app.layout.Testo;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class BaseCalculatorScreen extends Pannello {
    protected final JTextArea contenuto = new JTextArea();
    protected final Pulsante salva;
    protected Pulsante pulisci;
    protected Pulsante convertiBinario;
    protected List<Sottorete> risultato = null;

    public BaseCalculatorScreen(String title) {
        this.setBackground(Color.BLACK);

        JPanel titolo = new Pannello();
        titolo.add(new Pulsante("Indietro")
                .onClick(a -> Applicazione.getInstance().mainScreen()));
        titolo.add(new Testo(title, SwingConstants.CENTER)
                .setArial(Font.BOLD, 24));

        Pannello contenutoPagina = new Pannello();
        contenutoPagina.setLayout(new BoxLayout(contenutoPagina, BoxLayout.Y_AXIS));

        Pannello inserisciIp = inputPanel();

        Pannello azioni = new Pannello();
        azioni.layout(new GridLayout(1, 0, 4, 4));

        azioni.add(new Pulsante("Conferma")
                .onClick(a -> handleSubmit()), BorderLayout.PAGE_END);

        azioni.add(salva = new Pulsante("Salva")
                .setDisabled()
                .onClick(a -> {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Dove vuoi salvare il file?");

                    int userSelection = fileChooser.showSaveDialog(this);
                    if (userSelection != JFileChooser.APPROVE_OPTION) return;

                    File file = fileChooser.getSelectedFile();
                    if (!file.getName().toLowerCase().endsWith(".txt")) {
                        file = new File(file.getAbsolutePath() + ".txt");
                    }

                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(contenuto.getText());

                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().edit(file);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }), BorderLayout.PAGE_END);

        azioni.add(pulisci = new Pulsante("Pulisci")
                .setDisabled()
                .onClick(e -> {
                    contenuto.setText("");
                    salva.setDisabled();
                    convertiBinario.setDisabled();
                    pulisci.setDisabled();
                }), BorderLayout.PAGE_END);

        azioni.add(convertiBinario = new Pulsante("Binario")
                .setDisabled()
                .onClick(e -> {
                    mostraBinario();
                    convertiBinario.setDisabled();
                }), BorderLayout.PAGE_END);

        inserisciIp.add(azioni);

        contenuto.setMaximumSize(new Dimension(800, 40));
        contenuto.setEditable(false);
        contenuto.setBackground(Color.BLACK);
        contenuto.setForeground(Color.WHITE);
        contenuto.setFont(new Font("Arial", Font.PLAIN, 14));
        contenuto.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        contenutoPagina.add(inserisciIp);
        contenutoPagina.add(contenuto);

        JScrollPane scroll = new JScrollPane(contenuto);
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.DARK_GRAY;
                this.trackColor = Color.BLACK;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }

        });

        contenutoPagina.add(scroll);

        this.setLayout(new BorderLayout());

        this.add(titolo, BorderLayout.PAGE_START);
        this.add(contenutoPagina, BorderLayout.CENTER);
    }

    public abstract Pannello inputPanel();

    public abstract void handleSubmit();

    public void mostraBinario() {
        if (risultato == null) return;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < risultato.size(); i++) {
            Sottorete sottorete = risultato.get(i);
            builder.append("----------- Sottorete #").append(i + 1).append(" -----------\n")
                    .append(sottorete.toBinary())
                    .append("\n\n");
        }

        contenuto.setText(builder.toString());
        contenuto.setCaretPosition(0);
        salva.setEnabled();
        pulisci.setEnabled();
    }

}
