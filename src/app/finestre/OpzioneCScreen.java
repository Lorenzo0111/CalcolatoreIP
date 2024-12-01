package app.finestre;

import app.Applicazione;
import app.errori.CalculatorException;
import app.ip.Classe;
import app.ip.Sottorete;
import app.layout.Pannello;
import app.layout.PlaceholderInput;
import app.layout.Pulsante;
import app.layout.Testo;
import app.utils.IPUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpzioneCScreen extends Pannello {
    private final JTextArea contenuto = new JTextArea();
    private final Pulsante salva;
    private final Pulsante pulisci;

    public OpzioneCScreen() {
        this.setBackground(Color.BLACK);

        JPanel titolo = new Pannello();
        titolo.add(new Pulsante("Indietro")
                .onClick(a -> Applicazione.getInstance().mainScreen()));
        titolo.add(new Testo("Calcola sottreti", SwingConstants.CENTER)
                .setArial(Font.BOLD, 24));

        Pannello contenutoPagina = new Pannello();
        contenutoPagina.setLayout(new BoxLayout(contenutoPagina, BoxLayout.Y_AXIS));

        Pannello inserisciIp = new Pannello();
        inserisciIp.setMaximumSize(new Dimension(400, 100));
        inserisciIp.layout(new GridLayout(3, 0, 4, 4));

        PlaceholderInput ip = new PlaceholderInput();
        ip.setPlaceholder("Indirizzo IP");
        ip.setSize(new Dimension(200, 30));
        inserisciIp.add(ip);

        PlaceholderInput reti = new PlaceholderInput();

        reti.setPlaceholder("Numero reti");
        reti.setSize(new Dimension(200, 30));
        inserisciIp.add(reti);

        Pannello azioni = new Pannello();
        azioni.layout(new GridLayout(1, 0, 4, 4));

        azioni.add(new Pulsante("Conferma")
                .onClick(a -> {
                    int numReti;

                    try {
                        numReti = Integer.parseInt(reti.getText());
                    } catch (NumberFormatException e) {
                        throw new CalculatorException("Il numero di reti inserite non è valido");
                    }

                    esegui(ip.getText(), numReti);
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

                    if (e.getSource() instanceof Pulsante pulsante)
                        pulsante.setDisabled();
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

    public void esegui(String ip, int numSottoreti) {
        int[] ottetti = IPUtils.calcolaOttetti(ip);

        if (ottetti == null)
            throw new CalculatorException("L'indirizzo ip inserito non è valido");

        Classe classe = IPUtils.calcolaClasse(ottetti);
        if (classe == null) throw new CalculatorException("La classe non è valida");

        int bitSottoreti = (int) Math.ceil(IPUtils.log2(numSottoreti));
        int bitHostSottoreti = classe.getBitHost() - bitSottoreti;

        if (bitHostSottoreti < 2)
            throw new CalculatorException("Il numero di reti inserite non è valido");

        List<Sottorete> sottoreti = new ArrayList<>();

        for (int i = 0; i < numSottoreti; i++) {
            String netId = Integer.toBinaryString(i);
            netId = IPUtils.padStart(netId, '0', bitSottoreti);

            Sottorete sottorete = Sottorete.create(ottetti, classe, netId);
            sottoreti.add(sottorete);
        }

        StringBuilder builder = new StringBuilder();
        String subnetMask = IPUtils.calculateSubnetMask(classe, bitSottoreti);
        int[] subnetMaskOttetti = Arrays.stream(IPUtils.dividiOttetti(subnetMask))
                .mapToInt(s -> Integer.parseInt(s, 2))
                .toArray();
        int cidr = classe.getBitRete() + bitSottoreti;

        builder.append("Indirizzo IP: ").append(ip).append("\n")
                .append("Classe: ").append(classe).append("\n")
                .append("Maschera di sottorete: ").append(IPUtils.toString(subnetMaskOttetti))
                .append(" (/").append(cidr).append(")\n")
                .append("\n\n");

        for (int i = 0; i < sottoreti.size(); i++) {
            Sottorete sottorete = sottoreti.get(i);
            builder.append("----------- Sottorete #").append(i + 1).append(" -----------\n")
                    .append(sottorete)
                    .append("\n\n");
        }

        contenuto.setText(builder.toString());
        contenuto.setCaretPosition(0);
        salva.setEnabled();
        pulisci.setEnabled();
    }

}
