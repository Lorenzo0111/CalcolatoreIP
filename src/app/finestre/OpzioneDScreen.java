package app.finestre;

import app.errori.CalculatorException;
import app.ip.Classe;
import app.ip.Sottorete;
import app.utils.IPUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpzioneDScreen extends BaseCalculatorScreen {

    public OpzioneDScreen() {
        super("Calcola sottoreti (variabile)");
    }

    @Override
    public void handleSubmit(String ip, int numSottoreti) {
        int[] ottetti = IPUtils.calcolaOttetti(ip);

        if (ottetti == null)
            throw new CalculatorException("L'indirizzo ip inserito non è valido");

        Classe classe = IPUtils.calcolaClasse(ottetti);
        if (classe == null) throw new CalculatorException("La classe non è valida");

        if (numSottoreti < 1)
            throw new CalculatorException("Il numero di sottoreti deve essere maggiore di 0");

        int[] hostPerSottorete = new int[numSottoreti];

        for (int i = 0; i < numSottoreti; i++) {
            String response = JOptionPane.showInputDialog("Inserisci il numero di host per la sottorete " + (i + 1));
            if (response == null) return;

            try {
                hostPerSottorete[i] = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Inserisci un numero valido");
                i--;
            }
        }

        IPUtils.descendingSort(hostPerSottorete);

        int[] bitsSottoreti = new int[numSottoreti];

        for (int i = 0; i < numSottoreti; i++) {
            bitsSottoreti[i] = classe.getBitHost() - (int) Math.ceil(IPUtils.log2(hostPerSottorete[i] + 3));

            if (bitsSottoreti[i] <= 0)
                throw new CalculatorException("I bit per la sottorete #" + (i+1) + " non sono sufficienti");
        }

        List<Sottorete> sottoreti = new ArrayList<>();
        String lastNetId = null;

        for (int i = 0; i < numSottoreti; i++) {
            if (lastNetId == null)
                lastNetId = IPUtils.padEnd("0", '0', bitsSottoreti[i]);
            else
                lastNetId = IPUtils.padEnd(IPUtils.incrementBinary(lastNetId), '0', bitsSottoreti[i]);

            Sottorete sottorete = Sottorete.create(ottetti, classe, lastNetId);
            sottoreti.add(sottorete);
        }

        impostaRisultato(sottoreti);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sottoreti.size(); i++) {
            Sottorete sottorete = sottoreti.get(i);
            String subnetMask = IPUtils.calculateSubnetMask(classe, bitsSottoreti[i]);
            int[] subnetMaskOttetti = Arrays.stream(IPUtils.dividiOttetti(subnetMask))
                    .mapToInt(s -> Integer.parseInt(s, 2))
                    .toArray();
            int cidr = classe.getBitRete() + bitsSottoreti[i];

            builder.append("----------- Sottorete #").append(i + 1).append(" -----------\n")
                    .append("Host per sottorete: ").append(hostPerSottorete[i]).append("\n")
                    .append(sottorete)
                    .append("Maschera di sottorete: ").append(IPUtils.toString(subnetMaskOttetti))
                    .append(" (/").append(cidr).append(")\n")
                    .append("\n\n");
        }

        contenuto.setText(builder.toString());
        contenuto.setCaretPosition(0);
        salva.setEnabled();
        pulisci.setEnabled();
        convertiBinario.setEnabled();
    }

}
