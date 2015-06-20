package org.twnc.runtime;

/**
 * Created by wander on 6/20/15.
 */
public abstract class BBool extends BObject {
    public static BBool of(final boolean b) {
        return b ? new BTrue() : new BFalse();
    }

    public BObject _assert() {
        assert this instanceof BTrue;
        return this;
    }

    public BObject _asBool() {
        return this;
    }
}
