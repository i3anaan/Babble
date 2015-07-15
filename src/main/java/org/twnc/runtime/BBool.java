package org.twnc.runtime;

public abstract class BBool {
    public static Object of(boolean b) {
        return b ? Core.newTrue() : Core.newFalse();
    }

    public Object _assert() {
        assert this instanceof BTrue;
        return this;
    }
}
