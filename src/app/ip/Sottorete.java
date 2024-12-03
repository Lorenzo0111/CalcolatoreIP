package app.ip;

import app.utils.IPUtils;

public record Sottorete(
        int[] netId,
        int[] firstHost,
        int[] lastHost,
        int[] gateway,
        int[] broadcast,
        int[] subnetMask,
        int hosts
) {

    /**
     * @param ip Ottetti dell'indirizzo ip
     * @param classe Classe dell'indirizzo ip
     * @param netIdBin Id della sottorete in binario
     * @param bits Numero di bit per la sottorete
     * @param hosts Numero di host per la sottorete
     * @return Sottorete
     */
    public static Sottorete create(int[] ip, Classe classe, String netIdBin, int bits, int hosts) {
        String netId = IPUtils.padEnd(netIdBin, '0', classe.getBitHost());
        String firstHost = IPUtils.padEnd(netIdBin, '0', classe.getBitHost() - 1) + "1";
        String lastHost = IPUtils.manipulate(IPUtils.padEnd(netIdBin, '1', classe.getBitHost()), builder ->
                builder.setCharAt(builder.length() - 2, '0'));
        String gateway = IPUtils.padEnd(netIdBin, '1', classe.getBitHost() - 1) + "0";
        String broadcast = IPUtils.padEnd(netIdBin, '1', classe.getBitHost());

        return new Sottorete(
                prepare(ip, classe, netId),
                prepare(ip, classe, firstHost),
                prepare(ip, classe, lastHost),
                prepare(ip, classe, gateway),
                prepare(ip, classe, broadcast),
                IPUtils.getSubnetMask(classe, bits),
                hosts
        );
    }

    /**
     * Aggiunge gli ottetti della sottorete all'indirizzo ip
     * @param ip Ottetti dell'indirizzo ip
     * @param classe Classe dell'indirizzo ip
     * @param bits Bit della sottorete
     * @return Ottetti dell'indirizzo ip con quelli della sottorete
     */
    private static int[] prepare(int[] ip, Classe classe, String bits) {
        String[] ottetti = IPUtils.dividiOttetti(bits);
        int ottettiRete = classe.getOttettiRete();

        int[] result = new int[4];
        System.arraycopy(ip, 0, result, 0, ottettiRete);

        for (int i = 0; i < 4 - ottettiRete; i++) {
            result[ottettiRete + i] = Integer.parseInt(ottetti[i], 2);
        }

        return result;
    }

    @Override
    public String toString() {
        return """
                NetId: %d.%d.%d.%d
                Primo Host: %d.%d.%d.%d
                Ultimo Host: %d.%d.%d.%d
                Gateway: %d.%d.%d.%d
                Broadcast: %d.%d.%d.%d
                Subnet Mask: %d.%d.%d.%d /%d
                """.formatted(
                netId[0], netId[1], netId[2], netId[3],
                firstHost[0], firstHost[1], firstHost[2], firstHost[3],
                lastHost[0], lastHost[1], lastHost[2], lastHost[3],
                gateway[0], gateway[1], gateway[2], gateway[3],
                broadcast[0], broadcast[1], broadcast[2], broadcast[3],
                subnetMask[0], subnetMask[1], subnetMask[2], subnetMask[3], IPUtils.getCidr(subnetMask)
        );
    }

    public String toBinary() {
        return """
                NetId: %s
                Primo Host: %s
                Ultimo Host: %s
                Gateway: %s
                Broadcast: %s
                Subnet Mask: %s /%d
                """.formatted(
                IPUtils.toBinary(netId),
                IPUtils.toBinary(firstHost),
                IPUtils.toBinary(lastHost),
                IPUtils.toBinary(gateway),
                IPUtils.toBinary(broadcast),
                IPUtils.toBinary(subnetMask), IPUtils.getCidr(subnetMask)
        );
    }
}
