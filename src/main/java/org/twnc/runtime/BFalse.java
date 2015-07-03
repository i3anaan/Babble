package org.twnc.runtime;

public abstract class BFalse extends BBool {
    @Override
    public BObject _and_(BObject that) {
        return _asBool();
    }

    @Override
    public BObject _or_(BObject that) {
        return that._asBool();
    }

    @Override
    public BObject _ifTrue_ifFalse_(BObject t, BObject f) {
        return ((BBlock)f)._value();
    }
    
    @Override
    public boolean equals(Object that) {
        return that instanceof BFalse;
    }
}
