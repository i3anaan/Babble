package org.twnc.runtime;

public class BFalse extends BBool {
    @Override
    public BObject _not() {
        return BBool.of(true);
    }

    @Override
    public BObject _and_(BObject that) {
        return _asBool();
    }

    @Override
    public BObject _or_(BObject that) {
        return that._asBool();
    }

    @Override
    public String toString() {
        return "false";
    }

    @Override
    public BObject _asInt() {
        return new BInt(0);
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof BFalse;
    }
}
