package app.finestre;

import app.errori.CalculatorException;
import app.ip.Classe;
import app.ip.Sottorete;
import app.utils.IPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpzioneCScreen extends BaseCalculatorScreen {

    public OpzioneCScreen() {
        super("Calcola sottoreti");
    }

    @Override
    public void handleSubmit(String ip, int numSottoreti) {
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

        impostaRisultato(sottoreti);

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
        convertiBinario.setEnabled();
    }

}
