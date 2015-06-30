package org.twnc.runtime;

public abstract class BBool extends BObject {
    public static BBool of(boolean b) {
        return b ? new BTrue() : new BFalse();
    }

    public BObject _assert() {
        assert this instanceof BTrue;
        return this;
    }

    public BObject _asBool() {
        return this;
    }

    public abstract BObject _not();
    public abstract BObject _and_(BObject that);
    public abstract BObject _or_(BObject that);
}
