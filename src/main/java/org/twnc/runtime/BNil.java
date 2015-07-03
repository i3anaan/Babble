package org.twnc.runtime;

public abstract class BNil extends BObject {
    @Override
    public boolean equals(Object that) {
        return that instanceof BNil;
    }
}
