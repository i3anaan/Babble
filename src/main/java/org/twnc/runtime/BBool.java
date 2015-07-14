package org.twnc.runtime;

public abstract class BBool {
    public static Object of(boolean b) {
        return b ? Core.newTrue() : Core.newFalse();
    }

    public Object _assert() {
        assert this instanceof BTrue;
        return this;
    }
    
    public Object _ifTrue_(Object t) {
        return _ifTrue_ifFalse_(t, new BBlock());
    }

    public Object _ifFalse_(Object f) {
        return _ifTrue_ifFalse_(new BBlock(), f);
    }
    
    public abstract Object _not();
    public abstract Object _and_(Object that);
    public abstract Object _or_(Object that);

    public abstract Object _ifTrue_ifFalse_(Object t, Object f);
}
