package app;

import app.finestre.OpzioneAScreen;
import app.finestre.OpzioneBScreen;
import app.finestre.OpzioneCScreen;
import app.finestre.OpzioneDScreen;
import app.ip.Sottorete;
import app.utils.Colore;

import java.util.List;
import java.util.Scanner;

/**
 * Classe per l'implementazione della CLI
 */
public class CLI {

    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.init();
    }

    public void init() {
        int opzione = 0;

        Scanner scanner = new Scanner(System.in);

        do {
            try {
                // Stampa il menu
                System.out.println(Colore.YELLOW + "-----------------------------------------" + Colore.RESET);
                System.out.println(Colore.GREEN + "           Calcolatore di IP" + Colore.RESET);
                System.out.println(Colore.YELLOW + "-----------------------------------------" + Colore.RESET);

                System.out.println(Colore.GREEN + "Opzioni disponibili:\n" + Colore.RESET);
                System.out.println("1. Determina classe IP");
                System.out.println("2. Determina se due IP sono nello stesso subnet");
                System.out.println("3. Calcola sottoreti");
                System.out.println("4. Calcola sottoreti a maschera variabile");
                System.out.println("5. Esci\n");

                System.out.print("\n> ");
                opzione = scanner.nextInt();
                System.out.println("\n");

                // Esegue l'opzione scelta
                switch (opzione) {
                    // Opzione A
                    case 1:
                        System.out.print(Colore.YELLOW + "Inserisci l'indirizzo IP: ");
                        String ip = scanner.next();
                        System.out.println(Colore.RESET);

                        OpzioneAScreen.Risultato risultato = OpzioneAScreen.esegui(ip);
                        System.out.println(Colore.CYAN + "Classe IP: " + Colore.BLUE + risultato.classe());
                        System.out.println(Colore.CYAN + "Visibilita: " + Colore.BLUE + risultato.visibilita());
                        break;
                    // Opzione B
                    case 2:
                        System.out.print(Colore.YELLOW + "Inserisci il primo indirizzo IP: ");
                        String ip1 = scanner.next();

                        System.out.print(Colore.YELLOW + "Inserisci la maschera del primo indirizzo IP: ");
                        String sm1 = scanner.next();

                        System.out.print(Colore.YELLOW + "Inserisci il secondo indirizzo IP: ");
                        String ip2 = scanner.next();

                        System.out.print(Colore.YELLOW + "Inserisci la maschera del secondo indirizzo IP: ");
                        String sm2 = scanner.next();

                        boolean stessoNetId = OpzioneBScreen.esegui(ip1, ip2, sm1, sm2);
                        if (stessoNetId)
                            System.out.println(Colore.CYAN + "\nI due indirizzi " + Colore.GREEN + "appartengono" + Colore.CYAN + " alla stessa sottorete.");
                        else
                            System.out.println(Colore.CYAN + "\nI due indirizzi " + Colore.RED + "non appartengono" + Colore.CYAN + " alla stessa sottorete.");
                        break;
                    // Opzione C
                    case 3:
                        System.out.print(Colore.YELLOW + "Inserisci l'indirizzo IP: ");
                        String ipC = scanner.next();

                        System.out.print(Colore.YELLOW + "Inserisci il numero di sottoreti: ");
                        int numSottoreti = scanner.nextInt();

                        List<Sottorete> sottoreti = OpzioneCScreen.esegui(ipC, numSottoreti);
                        System.out.println(Colore.CYAN + "Sottoreti:");
                        for (Sottorete sottorete : sottoreti) {
                            System.out.println(Colore.BLUE + sottorete.toString());
                            System.out.println(Colore.RESET + "\n--------------------\n");
                        }
                        break;
                    // Opzione D
                    case 4:
                        System.out.print(Colore.YELLOW + "Inserisci l'indirizzo IP: ");
                        String ipD = scanner.next();

                        System.out.print(Colore.YELLOW + "Inserisci il numero di sottoreti: ");
                        int numSottoretiD = scanner.nextInt();

                        OpzioneDScreen.validate(ipD, numSottoretiD);

                        int[] hostPerSottorete = new int[numSottoretiD];
                        for (int i = 0; i < numSottoretiD; i++) {
                            System.out.print("Inserisci il numero di host per la sottorete " + (i + 1));
                            hostPerSottorete[i] = scanner.nextInt();
                        }

                        List<Sottorete> sottoretiD = OpzioneDScreen.esegui(ipD, numSottoretiD, hostPerSottorete);
                        System.out.println(Colore.CYAN + "\nSottoreti:");
                        for (Sottorete sottorete : sottoretiD) {
                            System.out.println(Colore.BLUE + "Numero di host: " + sottorete.hosts());
                            System.out.println(Colore.BLUE + sottorete.toString());
                            System.out.println(Colore.RESET + "\n--------------------\n");
                        }
                        break;
                    case 5:
                        break;
                    default:
                        System.out.println(Colore.RED + "Opzione non valida");
                        break;
                }
            } catch (Exception e) {
                System.out.println(Colore.RED + "Errore: " + (e.getMessage() == null ? "Input inserito non valido" : e.getMessage()));
            }

            System.out.println(Colore.GREEN + "Premi INVIO per continuare...");
            scanner.nextLine();
            scanner.nextLine();

            System.out.println("\n\n");
        } while (opzione != 5);
    }
}