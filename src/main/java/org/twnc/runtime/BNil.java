package org.twnc.runtime;

public class BNil extends BObject {
    public String toString() {
        return "nil";
    }

    public BObject _isNil() {
        return BBool.of(true);
    }

    public BObject _asBool() {
        return BBool.of(false);
    }

    public boolean equals(Object that) {
        return that instanceof BNil;
    }
}
