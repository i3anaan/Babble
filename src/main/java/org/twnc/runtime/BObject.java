package org.twnc.runtime;

import java.util.Objects;

public abstract class BObject {
    public BObject _print() {
        System.out.println(this);
        return this;
    }

    public BObject _isNil() {
        return BBool.of(false);
    }

    public BObject _asString() {
        return new BStr("Object");
    }

    public BObject _asBool() {
        return BBool.of(true);
    }

    public BObject _asInt() {
        return new BInt(0);
    }

    public BObject _hashCode() {
        return new BInt(Objects.hashCode(this));
    }

    public BObject _eqeq_(BObject that) {
        return BBool.of(equals(that));
    }

    public BObject _bangeq_(BObject that) {
        return BBool.of(!equals(that));
    }

    @Override
    public String toString() {
        return _asString().toString();
    }

    @Override
    public int hashCode() {
        return ((BInt)_hashCode()).hashCode();
    }
}
