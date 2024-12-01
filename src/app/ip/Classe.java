package app.ip;

public enum Classe {
    A(1),
    B(2),
    C(3);

    private final int ottettiRete;

    Classe(int ottettiRete) {
        this.ottettiRete = ottettiRete;
    }

    public int getOttettiRete() {
        return ottettiRete;
    }

    public int getBitRete() {
        return ottettiRete * 8;
    }

    public int getBitHost() {
        return (4 - ottettiRete) * 8;
    }
}
