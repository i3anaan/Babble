package org.twnc.runtime;

public class BSymbol extends BObject {
    private String symbol;

    public BSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public BObject _asInt() {
        return new BInt(symbol.hashCode());
    }

    public BObject _asString() {
        return new BStr("#" + symbol);
    }

    public boolean equals(Object that) {
        return that instanceof BSymbol && ((BSymbol)that).symbol.equals(this.symbol);
    }
}
