package org.twnc.runtime;

public abstract class BTrue extends BBool {
    @Override
    public Object _and_(Object that) {
        return that;
    }

    @Override
    public Object _or_(Object that) {
        return this;
    }

    @Override
    public Object _ifTrue_ifFalse_(Object t, Object f) {
        return ((BBlock)t)._value();
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof BTrue;
    }
}
