package org.twnc.runtime;

public abstract class BBool extends BObject {
    public static BObject of(boolean b) {
        return b ? Core.newTrue() : Core.newFalse();
    }

    public BObject _assert() {
        assert this instanceof BTrue;
        return this;
    }

    public abstract BObject _not();
    public abstract BObject _and_(BObject that);
    public abstract BObject _or_(BObject that);
}
