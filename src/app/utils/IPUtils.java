package app.utils;

import app.errori.CalculatorException;
import app.ip.Classe;

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

    public static String padEnd(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padStart(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    public static double log2(int N) {
        return Math.log(N) / Math.log(2);
    }
}
