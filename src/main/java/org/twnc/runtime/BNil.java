package org.twnc.runtime;

public class BNil extends BObject {
    public String toString() {
        return "nil";
    }

    @Override
    public BObject _isNil() {
        return BBool.of(true);
    }

    @Override
    public BObject _asBool() {
        return BBool.of(false);
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof BNil;
    }

    @Override
    public int hashCode() {
        return -1;
    }
}
