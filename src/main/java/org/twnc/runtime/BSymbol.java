package org.twnc.runtime;

public class BSymbol extends BObject {
    private int num;
    private String symbol;

    public BSymbol(String symbol, int num) {
        this.num = num;
        this.symbol = symbol;
    }

    public BObject _asInt() {
        return new BInt(num);
    }

    public String toString() {
        return "#" + symbol;
    }
}
