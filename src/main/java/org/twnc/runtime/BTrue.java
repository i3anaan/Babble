package org.twnc.runtime;

public class BTrue extends BBool {
    public BObject _not() {
        return BBool.of(false);
    }

    public BObject _and_(final BObject that) {
        return that._asBool();
    }

    public BObject _or_(final BObject that) {
        return this._asBool();
    }

    public BObject _asBool() {
        return this;
    }

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
