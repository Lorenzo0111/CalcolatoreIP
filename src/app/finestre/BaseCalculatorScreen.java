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

/**
 * Classe base per l'astrazione delle schermate del calcolatore (opzione C e D)
 */
public abstract class BaseCalculatorScreen extends Pannello {
    /**
     * Pannello del contenuto della pagina
     */
    private JPanel contenutoPagina;
    /**
     * Pulsanti per salvare, pulire e convertire in binario
     */
    protected Pulsante salva;
    protected Pulsante pulisci;
    protected Pulsante convertiBinario;
    /**
     * Tabella per visualizzare i risultati
     */
    protected Tabella table;
    /**
     * Risultato del calcolo o null se non è stato ancora calcolato
     */
    private List<Sottorete> risultato = null;

    /**
     * Costruttore della classe
     *
     * @param title Titolo della schermata
     */
    public BaseCalculatorScreen(String title) {
        this.redraw(title);
    }

    /**
     * Ridisegna la schermata
     *
     * @param title Titolo della schermata
     */
    public void redraw(String title) {
        // Pulisce la schermata
        this.setBackground(Color.BLACK);
        this.removeAll();

        // Titolo e pulsante per tornare indietro
        JPanel titolo = new Pannello();
        titolo.add(new Pulsante("Indietro")
                .onClick(a -> Applicazione.getInstance().mainScreen()));
        titolo.add(new Testo(title, SwingConstants.CENTER)
                .setArial(Font.BOLD, 24));

        // Contenuto della pagina
        contenutoPagina = new Pannello();
        contenutoPagina.setLayout(new BoxLayout(contenutoPagina, BoxLayout.Y_AXIS));

        // Pannello per inserire l'indirizzo IP e il numero di reti
        Pannello inserisciIp = new Pannello();
        inserisciIp.setMaximumSize(new Dimension(400, 100));
        inserisciIp.layout(new GridLayout(3, 0, 4, 4));

        // Input per l'indirizzo IP
        PlaceholderInput ipInput = new PlaceholderInput();
        ipInput.setPlaceholder("Indirizzo IP");
        ipInput.setSize(new Dimension(200, 30));
        inserisciIp.add(ipInput);

        // Input per il numero di reti
        PlaceholderInput retiInput = new PlaceholderInput();

        retiInput.setPlaceholder("Numero reti");
        retiInput.setSize(new Dimension(200, 30));
        inserisciIp.add(retiInput);

        // Pulsanti per confermare, salvare, pulire e convertire in binario
        Pannello azioni = new Pannello();
        azioni.layout(new GridLayout(1, 0, 4, 4));

        azioni.add(new Pulsante("Conferma")
                .onClick(a -> {
                    String ip = ipInput.getText();
                    int numSottoreti;

                    // Validazione numero di reti
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
                    // Apre una finestra di dialogo per salvare il file
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
                            // Scrive i risultati nel file a seconda dello stato di binario
                            if (convertiBinario.getText()
                                    .equalsIgnoreCase("Decimale"))
                                writer.write(sottorete.toBinary());
                            else
                                writer.write(sottorete.toString());
                            writer.write("\n");
                        }

                        // Apre il file con l'applicazione predefinita per i file .txt
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().edit(file);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }), BorderLayout.PAGE_END);

        // Pulsante per pulire i risultati
        azioni.add(pulisci = new Pulsante("Pulisci")
                .setDisabled()
                .onClick(e -> this.redraw(title)), BorderLayout.PAGE_END);

        // Pulsante per convertire i risultati in binario
        azioni.add(convertiBinario = new Pulsante("Binario")
                .setDisabled()
                .onClick(e -> {
                    // Se il testo del pulsante è "Binario" allora mostra i risultati in binario
                    if (convertiBinario.getText().equalsIgnoreCase("Binario")) {
                        mostraBinario();
                        convertiBinario.setText("Decimale");
                        return;
                    }

                    // Altrimenti mostra i risultati in decimale
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

    /**
     * Metodo chiamato quando l'utente conferma l'inserimento
     *
     * @param ip           Indirizzo IP
     * @param numSottoreti Numero di sottoreti
     */
    public abstract void handleSubmit(String ip, int numSottoreti);

    public void mostraBinario() {
        // Se non ci sono risultati non fare nulla
        if (risultato == null) return;

        // Rimuove la tabella attuale
        JScrollPane prev = table.get();
        if (prev != null)
            contenutoPagina.remove(prev);

        // Converte i risultati in binario
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

    /**
     * Imposta il risultato del calcolo
     *
     * @param risultato Risultato del calcolo
     */
    protected void impostaRisultato(List<Sottorete> risultato) {
        this.risultato = risultato;

        // Rimuove la tabella attuale
        JScrollPane prev = table.get();
        if (prev != null)
            contenutoPagina.remove(prev);

        // Converte i risultati in decimale
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
