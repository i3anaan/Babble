package org.twnc.runtime;

public class BFalse extends BBool {
    public BObject _not() {
        return BBool.of(true);
    }

    public BObject _and_(BObject that) {
        return this._asBool();
    }

    public BObject _or_(BObject that) {
        return that._asBool();
    }

    public String toString() {
        return "false";
    }

    public BObject _asInt() {
        return new BInt(0);
    }

    public boolean equals(Object that) {
        return that instanceof BFalse;
    }
}
