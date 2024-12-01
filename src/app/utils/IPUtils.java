package app.utils;

import app.errori.CalculatorException;
import app.ip.Classe;

import java.util.function.Consumer;

public final class IPUtils {

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

    public static String[] dividiOttetti(String ipBin) {
        return ipBin.split("(?<=\\G.{8})");
    }

    public static String calculateSubnetMask(Classe classe, int bits) {
        int totalBits = classe.getBitRete() + bits;
        return padEnd("1".repeat(totalBits), '0', 32);
    }

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

    public static boolean privato(int[] ottetti) {
        if (ottetti[0] == 10) {
            return true;
        } else if (ottetti[0] == 172 && ottetti[1] >= 16 && ottetti[1] <= 31) {
            return true;
        } else return ottetti[0] == 192 && ottetti[1] == 168;
    }

    public static String padEnd(String string, char character, int length) {
        StringBuilder builder = new StringBuilder(string);
        while (builder.length() < length) {
            builder.append(character);
        }

        return builder.toString();
    }

    public static String padStart(String string, char character, int length) {
        StringBuilder builder = new StringBuilder(string);
        while (builder.length() < length) {
            builder.insert(0, character);
        }

        return builder.toString();
    }

    public static String manipulate(String string, Consumer<StringBuilder> consumer) {
        StringBuilder builder = new StringBuilder(string);
        consumer.accept(builder);
        return builder.toString();
    }

    public static double log2(int N) {
        return Math.log(N) / Math.log(2);
    }

    public static String toString(int[] ottetti) {
        return ottetti[0] + "." + ottetti[1] + "." + ottetti[2] + "." + ottetti[3];
    }

    public static String toBinary(int[] ottetti) {
        return padStart(Integer.toBinaryString(ottetti[0]), '0', 8) + "." +
                padStart(Integer.toBinaryString(ottetti[1]), '0', 8) + "." +
                padStart(Integer.toBinaryString(ottetti[2]), '0', 8) + "." +
                padStart(Integer.toBinaryString(ottetti[3]), '0', 8);
    }
}
