package org.twnc.runtime;

public class BTrue extends BBool {
    @Override
    public BObject _not() {
        return BBool.of(false);
    }

    @Override
    public BObject _and_(BObject that) {
        return that._asBool();
    }

    @Override
    public BObject _or_(BObject that) {
        return _asBool();
    }

    @Override
    public BObject _asBool() {
        return new BTrue();
    }

    @Override
    public BObject _asInt() {
        return new BInt(1);
    }

    @Override
    public String toString() {
        return "true";
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof BTrue;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
