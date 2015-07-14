package org.twnc.runtime;

import java.util.Arrays;

public class BClass {
    public Object _name() {
        return new BStr("Class");
    }

    public Object _methods() {
        return new BArray(Arrays.asList(new BStr("name"), new BStr("methods"), new BStr("class")));
    }

    public Object _class() {
        return this;
    }

    public Object _respondsTo_(Object selector) {
        return ((BArray)_methods())._includes_(selector);
    }
}
