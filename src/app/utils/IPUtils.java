package app.utils;

import app.errori.CalculatorException;
import app.ip.Classe;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Funzioni di utilità per gli indirizzi IP
 */
public final class IPUtils {

    /**
     * Dividi l'indirizzo IP in ottetti
     * @param ip Indirizzo IP
     * @return Ottetti dell'indirizzo IP
     */
    public static int[] calcolaOttetti(String ip) {
        if (ip.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
            String[] ottetti = ip.split("\\.");
            int[] ottettiInt = new int[4];

            for (int i = 0; i < ottetti.length; i++) {
                ottettiInt[i] = Integer.parseInt(ottetti[i]);
                if (ottettiInt[i] < 0 || ottettiInt[i] > 255)
                    throw new CalculatorException("Inserisci un indirizzo IP valido");
            }

            return ottettiInt;
        }

        return null;
    }

    /**
     * Dividi l'indirizzo IP binario in ottetti
     * @param ipBin Indirizzo IP binario
     * @return Ottetti dell'indirizzo IP binario
     */
    public static String[] dividiOttetti(String ipBin) {
        return ipBin.split("(?<=\\G.{8})");
    }

    /**
     * Calcola la maschera di sottorete
     * @param classe Classe dell'indirizzo IP
     * @param bits Numero di bit per la sottorete
     * @return Maschera di sottorete in binario
     */
    public static String calculateSubnetMask(Classe classe, int bits) {
        int totalBits = classe.getBitRete() + bits;
        return padEnd("1".repeat(totalBits), '0', 32);
    }

    /**
     * Calcola la classe dell'indirizzo IP
     * @param ottetti Ottetti dell'indirizzo IP
     * @return Classe dell'indirizzo IP
     */
    public static Classe calcolaClasse(int[] ottetti) {
        if (ottetti[0] >= 1 && ottetti[0] <= 127) {
            return Classe.A;
        } else if (ottetti[0] >= 128 && ottetti[0] <= 191) {
            return Classe.B;
        } else if (ottetti[0] >= 192 && ottetti[0] <= 223) {
            return Classe.C;
        }

        return null;
    }

    /**
     * Verifica se l'indirizzo IP è privato
     * @param ottetti Ottetti dell'indirizzo IP
     * @return True se l'indirizzo IP è privato, false altrimenti
     */
    public static boolean privato(int[] ottetti) {
        if (ottetti[0] == 10) {
            return true;
        } else if (ottetti[0] == 172 && ottetti[1] >= 16 && ottetti[1] <= 31) {
            return true;
        } else return ottetti[0] == 192 && ottetti[1] == 168;
    }

    /**
     * Aggiunge caratteri alla fine di una stringa per raggiungere una lunghezza specifica
     * @param string Stringa
     * @param character Carattere da aggiungere
     * @param length Lunghezza finale
     * @return Stringa con caratteri aggiunti alla fine
     */
    public static String padEnd(String string, char character, int length) {
        StringBuilder builder = new StringBuilder(string);
        while (builder.length() < length) {
            builder.append(character);
        }

        return builder.toString();
    }

    /**
     * Aggiunge caratteri all'inizio di una stringa per raggiungere una lunghezza specifica
     * @param string Stringa
     * @param character Carattere da aggiungere
     * @param length Lunghezza finale
     * @return Stringa con caratteri aggiunti all'inizio
     */
    public static String padStart(String string, char character, int length) {
        StringBuilder builder = new StringBuilder(string);
        while (builder.length() < length) {
            builder.insert(0, character);
        }

        return builder.toString();
    }

    /**
     * Manipola una stringa con un consumer
     * @param string Stringa
     * @param consumer Consumer
     * @return Stringa manipolata
     */
    public static String manipulate(String string, Consumer<StringBuilder> consumer) {
        StringBuilder builder = new StringBuilder(string);
        consumer.accept(builder);
        return builder.toString();
    }

    /**
     * Logaritmo in base 2
     * @param N Numero
     * @return Logaritmo in base 2
     */
    public static double log2(int N) {
        return Math.log(N) / Math.log(2);
    }

    /**
     * Converte gli ottetti in stringa
     * @param ottetti Ottetti
     * @return Stringa
     */
    public static String toString(int[] ottetti) {
        return ottetti[0] + "." + ottetti[1] + "." + ottetti[2] + "." + ottetti[3];
    }

    /**
     * Converte gli ottetti in binario
     * @param ottetti Ottetti
     * @return Stringa binaria
     */
    public static String toBinary(int[] ottetti) {
        return padStart(Integer.toBinaryString(ottetti[0]), '0', 8) + "." +
                padStart(Integer.toBinaryString(ottetti[1]), '0', 8) + "." +
                padStart(Integer.toBinaryString(ottetti[2]), '0', 8) + "." +
                padStart(Integer.toBinaryString(ottetti[3]), '0', 8);
    }

    /**
     * Ordina un array in ordine decrescente
     * @param array Array da ordinare
     */
    public static void descendingSort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] < array[j]) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    /**
     * Incrementa un numero binario di 1
     * @param binary Numero binario
     * @return Numero binario incrementato
     */
    public static String incrementBinary(String binary) {
        int decimal = Integer.parseInt(binary, 2);
        return padStart(Integer.toBinaryString(decimal + 1), '0', binary.length());
    }

    /**
     * Calcola la maschera di sottorete
     * @param classe Classe dell'indirizzo IP
     * @param bits Numero di bit per la sottorete
     * @return Maschera di sottorete
     */
    public static int[] getSubnetMask(Classe classe, int bits) {
        String subnetMask = IPUtils.calculateSubnetMask(classe, bits);
        return Arrays.stream(IPUtils.dividiOttetti(subnetMask))
                .mapToInt(s -> Integer.parseInt(s, 2))
                .toArray();
    }

    /**
     * Calcola il CIDR
     * @param subnetMask Maschera di sottorete
     * @return CIDR
     */
    public static int getCidr(int[] subnetMask) {
        int cidr = 0;
        for (int i : subnetMask) {
            cidr += Integer.bitCount(i);
        }

        return cidr;
    }
}
