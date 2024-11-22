package app.ip;

public enum Classe {
    A(24),
    B(16),
    C(8);

    private final int bitHost;

    Classe(int bitHost) {
        this.bitHost = bitHost;
    }

    public int getBitHost() {
        return bitHost;
    }
}
