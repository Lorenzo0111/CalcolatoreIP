package app.finestre;

import app.Applicazione;
import app.errori.CalculatorException;
import app.ip.Sottorete;
import app.layout.Pannello;
import app.layout.PlaceholderInput;
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
    protected final Pulsante salva;
    protected Pulsante pulisci;
    protected Pulsante convertiBinario;
    protected JTable table;
    private List<Sottorete> risultato = null;

    public BaseCalculatorScreen(String title) {
        this.setBackground(Color.BLACK);

        JPanel titolo = new Pannello();
        titolo.add(new Pulsante("Indietro")
                .onClick(a -> Applicazione.getInstance().mainScreen()));
        titolo.add(new Testo(title, SwingConstants.CENTER)
                .setArial(Font.BOLD, 24));

        Pannello contenutoPagina = new Pannello();
        contenutoPagina.setLayout(new BoxLayout(contenutoPagina, BoxLayout.Y_AXIS));

        Pannello inserisciIp = new Pannello();
        inserisciIp.setMaximumSize(new Dimension(400, 100));
        inserisciIp.layout(new GridLayout(3, 0, 4, 4));

        PlaceholderInput ipInput = new PlaceholderInput();
        ipInput.setPlaceholder("Indirizzo IP");
        ipInput.setSize(new Dimension(200, 30));
        inserisciIp.add(ipInput);

        PlaceholderInput retiInput = new PlaceholderInput();

        retiInput.setPlaceholder("Numero reti");
        retiInput.setSize(new Dimension(200, 30));
        inserisciIp.add(retiInput);

        Pannello azioni = new Pannello();
        azioni.layout(new GridLayout(1, 0, 4, 4));

        azioni.add(new Pulsante("Conferma")
                .onClick(a -> {
                    String ip = ipInput.getText();
                    int numSottoreti;

                    try {
                        numSottoreti = Integer.parseInt(retiInput.getText());
                    } catch (NumberFormatException e) {
                        throw new CalculatorException("Il numero di reti inserite non è valido");
                    }

                    handleSubmit(ip, numSottoreti);
                }), BorderLayout.PAGE_END);

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
                        for (Sottorete sottorete : risultato) {
                            writer.write(sottorete.toString());
                            writer.write("\n");
                        }

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
                    risultato = null;
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
        contenutoPagina.add(inserisciIp);

        table = new JTable(new String[][]{}, new String[]{ "N. Hosts", "Net ID", "Primo Host", "Ultimo Host", "Gateway", "Broadcast", "SM" });

        JScrollPane scroll = new JScrollPane(table);
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

    public abstract void handleSubmit(String ip, int numSottoreti);

    public void mostraBinario() {
        if (risultato == null) return;

        // TODO: AGGIORNA TABLE
        salva.setEnabled();
        pulisci.setEnabled();
    }

    protected void impostaRisultato(List<Sottorete> risultato) {
        this.risultato = risultato;
        // TODO: AGGIORNA TABLE
    }

}
