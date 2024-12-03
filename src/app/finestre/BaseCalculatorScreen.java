package app.finestre;

import app.Applicazione;
import app.errori.CalculatorException;
import app.ip.Sottorete;
import app.layout.*;
import app.utils.IPUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class BaseCalculatorScreen extends Pannello {
    private JPanel contenutoPagina;
    protected Pulsante salva;
    protected Pulsante pulisci;
    protected Pulsante convertiBinario;
    protected Tabella table;
    private List<Sottorete> risultato = null;

    public BaseCalculatorScreen(String title) {
        this.redraw(title);
    }

    public void redraw(String title) {
        this.setBackground(Color.BLACK);
        this.removeAll();

        JPanel titolo = new Pannello();
        titolo.add(new Pulsante("Indietro")
                .onClick(a -> Applicazione.getInstance().mainScreen()));
        titolo.add(new Testo(title, SwingConstants.CENTER)
                .setArial(Font.BOLD, 24));

        contenutoPagina = new Pannello();
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
                .onClick(e -> this.redraw(title)), BorderLayout.PAGE_END);

        azioni.add(convertiBinario = new Pulsante("Binario")
                .setDisabled()
                .onClick(e -> {
                    if (convertiBinario.getText().equalsIgnoreCase("Binario")) {
                        mostraBinario();
                        convertiBinario.setText("Decimale");
                        return;
                    }

                    convertiBinario.setText("Binario");
                    impostaRisultato(risultato);
                }), BorderLayout.PAGE_END);

        inserisciIp.add(azioni);
        contenutoPagina.add(inserisciIp);

        table = new Tabella(new String[]{"N. Hosts", "Net ID", "Primo Host", "Ultimo Host", "Gateway", "Broadcast", "SM"});

        this.setLayout(new BorderLayout());

        this.add(titolo, BorderLayout.PAGE_START);
        this.add(contenutoPagina, BorderLayout.CENTER);

        this.revalidate();
    }

    public abstract void handleSubmit(String ip, int numSottoreti);

    public void mostraBinario() {
        if (risultato == null) return;

        JScrollPane prev = table.get();
        if (prev != null)
            contenutoPagina.remove(prev);

        String[][] dati = new String[risultato.size()][7];
        for (int i = 0; i < risultato.size(); i++) {
            Sottorete sottorete = risultato.get(i);
            dati[i] = new String[]{
                    String.valueOf(sottorete.hosts()),
                    IPUtils.toBinary(sottorete.netId()),
                    IPUtils.toBinary(sottorete.firstHost()),
                    IPUtils.toBinary(sottorete.lastHost()),
                    IPUtils.toBinary(sottorete.gateway()),
                    IPUtils.toBinary(sottorete.broadcast()),
                    IPUtils.toBinary(sottorete.subnetMask()) + " /" + IPUtils.getCidr(sottorete.subnetMask())
            };
        }

        table.setData(dati);
        contenutoPagina.add(table.get());
        contenutoPagina.revalidate();

        salva.setEnabled();
        pulisci.setEnabled();
    }

    protected void impostaRisultato(List<Sottorete> risultato) {
        this.risultato = risultato;

        JScrollPane prev = table.get();
        if (prev != null)
            contenutoPagina.remove(prev);

        String[][] dati = new String[risultato.size()][7];
        for (int i = 0; i < risultato.size(); i++) {
            Sottorete sottorete = risultato.get(i);
            dati[i] = new String[]{
                    String.valueOf(sottorete.hosts()),
                    IPUtils.toString(sottorete.netId()),
                    IPUtils.toString(sottorete.firstHost()),
                    IPUtils.toString(sottorete.lastHost()),
                    IPUtils.toString(sottorete.gateway()),
                    IPUtils.toString(sottorete.broadcast()),
                    IPUtils.toString(sottorete.subnetMask()) + " /" + IPUtils.getCidr(sottorete.subnetMask())
            };
        }

        table.setData(dati);
        contenutoPagina.add(table.get());
        contenutoPagina.revalidate();
    }

}
