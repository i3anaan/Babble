package org.twnc.runtime;

public class BSymbol {
    private String symbol;

    public BSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Object _asInt() {
        return new BInt(symbol.hashCode());
    }

    public Object _asString() {
        return new BStr("#" + symbol);
    }

    public Object _eqeq_(Object that) {
        return BBool.of(equals(that));
    }

    public Object _class() {
        return Core.newOpaqueClass();
    }

    public boolean equals(Object that) {
        return that instanceof BSymbol && ((BSymbol)that).symbol.equals(this.symbol);
    }
}
