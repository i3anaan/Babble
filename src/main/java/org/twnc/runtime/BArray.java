package org.twnc.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BArray extends ArrayList<Object> {
    public BArray(List<Object> objects) {
        super(objects);
    }

    public Object _at_(Object index) {
        return Core.nilOf(get(((Number) index).intValue()));
    }

    public Object _at_put_(Object index, Object value) {
        set(((Number) index).intValue(), value);
        return value;
    }

    public Object _size() {
        return new BInt(size());
    }

    public Object _isEmpty() {
        return Core.newBool(isEmpty());
    }

    public Object _notEmpty() {
        return Core.newBool(!isEmpty());
    }

    public Object _add_(Object value) {
        add(value);
        return value;
    }

    public Object _addAll_(Object value) {
        addAll((Collection<?>) value);
        return value;
    }

    public Object _first() {
        return _at_(0);
    }

    public Object _last() {
        return _at_(size() - 1);
    }

    public Object _includes_(Object value) {
        return Core.newBool(contains(value));
    }

    public Object _reverse() {
        BArray result = new BArray(this);
        Collections.reverse(result);
        return result;
    }

    public Object _asString() {
        return new BStr(toString());
    }

    public Object _class() {
        return Core.newOpaqueClass();
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    public static BArray make(Object[] objects) {
        return new BArray(Arrays.asList(objects));
    }
}
