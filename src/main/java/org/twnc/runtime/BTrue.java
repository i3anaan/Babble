package org.twnc.runtime;

public abstract class BTrue extends BBool {
    @Override
    public BObject _and_(BObject that) {
        return that._asBool();
    }

    @Override
    public BObject _or_(BObject that) {
        return _asBool();
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof BTrue;
    }
}
