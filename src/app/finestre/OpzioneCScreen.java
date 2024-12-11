package app.finestre;

import app.errori.CalculatorException;
import app.ip.Classe;
import app.ip.Sottorete;
import app.utils.IPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Schermata per l'opzione C
 */
public class OpzioneCScreen extends BaseCalculatorScreen {

    public OpzioneCScreen() {
        super("Calcola sottoreti");
    }

    @Override
    public void handleSubmit(String ip, int numSottoreti) {
        List<Sottorete> sottoreti = esegui(ip, numSottoreti);

        impostaRisultato(sottoreti);

        salva.setEnabled();
        pulisci.setEnabled();
        convertiBinario.setEnabled();
    }

    public static List<Sottorete> esegui(String ip, int numSottoreti) {
        // Calcola gli ottetti dell'indirizzo ip
        int[] ottetti = IPUtils.calcolaOttetti(ip);

        if (ottetti == null)
            throw new CalculatorException("L'indirizzo ip inserito non è valido");

        // Calcola la classe dell'indirizzo ip
        Classe classe = IPUtils.calcolaClasse(ottetti);
        if (classe == null) throw new CalculatorException("La classe non è valida");

        // Calcola il numero di bit necessari per rappresentare il numero di sottoreti
        int bitSottoreti = (int) Math.ceil(IPUtils.log2(numSottoreti));
        int bitHostSottoreti = classe.getBitHost() - bitSottoreti;

        if (bitHostSottoreti < 2)
            throw new CalculatorException("Il numero di reti inserite non è valido. Il massimo è " + (int) Math.pow(2, classe.getBitHost() - 2));

        // Calcola le sottoreti
        List<Sottorete> sottoreti = new ArrayList<>();

        for (int i = 0; i < numSottoreti; i++) {
            // Calcola l'id della sottorete
            String netId = Integer.toBinaryString(i);
            netId = IPUtils.padStart(netId, '0', bitSottoreti);

            Sottorete sottorete = Sottorete.create(ottetti, classe, netId, bitSottoreti, -1);
            sottoreti.add(sottorete);
        }

        return sottoreti;
    }

}
