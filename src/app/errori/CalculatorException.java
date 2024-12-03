package app.errori;

/**
 * Eccezione personalizzata per gli errori del calcolatore.
 */
public class CalculatorException extends RuntimeException {

    public CalculatorException(String message) {
        super(message);
    }

}
