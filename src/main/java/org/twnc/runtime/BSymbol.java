package org.twnc.runtime;

/**
 * A Babble symbol value. Symbol values are designed to be used as lightweight
 * 'tag' strings, similar to enum values in other languages. They are immutable.
 */
public class BSymbol {
    private String symbol;

    /** Make a new BSymbol from a Java String. */
    public BSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Return an integer representation of this symbol. Equal symbols will
     * have equal integer representations, but no other guarantees are given.
     */
    public Object _asInt() {
        return new BInt(symbol.hashCode());
    }

    /** Return a String representation of this symbol. */
    public Object _asString() {
        return new BStr("#" + symbol);
    }

    /** Compare this symbol with another symbol (==). */
    public Object _eqeq_(Object that) {
        return Core.newBool(equals(that));
    }

     /** BSymbol does not have a Babble-visible metaclass. */
    public Object _class() {
        return Core.newOpaqueClass();
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof BSymbol && ((BSymbol)that).symbol.equals(this.symbol);
    }
}
