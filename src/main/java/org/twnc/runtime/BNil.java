package org.twnc.runtime;

public abstract class BNil {
    public Object _eqeq_(Object that) {
        return BBool.of(equals(that));
    }

    public Object _bangeq_(Object that) {
        return BBool.of(!equals(that));
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof BNil;
    }
}
