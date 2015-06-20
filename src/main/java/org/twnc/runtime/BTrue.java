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
        return this;
    }

    @Override
    public BObject _asInt() {
        return new BInt(1);
    }

    public String toString() {
        return "true";
    }

    public boolean equals(Object that) {
        return that instanceof BTrue;
    }
}
