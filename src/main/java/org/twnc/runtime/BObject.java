package org.twnc.runtime;

public abstract class BObject extends Object {
    public BObject _print() {
        System.out.println(this);
        return this;
    }

    public BObject _isNil() {
        return BBool.of(false);
    }

    public BObject _asString() {
        return new BStr(toString());
    }

    public BObject _asBool() {
        return BBool.of(true);
    }

    public BObject _asInt() {
        return new BInt(0);
    }

    public BObject _eqeq_(BObject that) {
        return BBool.of(equals(that));
    }

    public BObject _bangeq_(BObject that) {
        return BBool.of(!equals(that));
    }
}
