package org.twnc.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An array of Babble objects. Arrays are:
 *
 * - polymorphic: elements of the array do not have to have the same type.
 * - dynamic: adding an object to an array will grow the array if needed.
 * - zero-based: the first element is at index 0.
 *
 * Unset elements are returned as Nil.
 */
public class BArray extends ArrayList<Object> {
    /** Construct a new BArray with the given contents. */
    public BArray(List<Object> objects) {
        super(objects);
    }

    /** Return the object at the given index, or nil if that index is unset. */
    public Object _at_(Object index) {
        return Core.nilOf(get(((Number) index).intValue()));
    }

    /** Put the object in the array at the given index. */
    public Object _at_put_(Object index, Object value) {
        set(((Number) index).intValue(), value);
        return value;
    }

    /** Return the number of elements in the array. */
    public Object _size() {
        return new BInt(size());
    }

    /** Return True when the array is empty, or False otherwise. */
    public Object _isEmpty() {
        return BBool.of(isEmpty());
    }

    /** Return True when the array contains at least one element, or False otherwise. */
    public Object _notEmpty() {
        return BBool.of(!isEmpty());
    }

    /** Add an element to the end of the array. */
    public Object _add_(Object value) {
        add(value);
        return value;
    }

    /** Add every element from the given array to this array. */
    public Object _addAll_(Object value) {
        addAll((Collection<?>) value);
        return value;
    }

    /** Return the first element of this array. */
    public Object _first() {
        return _at_(0);
    }

    /** Return the last element of this array. */
    public Object _last() {
        return _at_(size() - 1);
    }

    /** Return True if the array includes the given value, False otherwise. */
    public Object _includes_(Object value) {
        return BBool.of(contains(value));
    }

    /** Return a new array with the same elements as this one, but reversed. */
    public Object _reverse() {
        BArray result = new BArray(this);
        Collections.reverse(result);
        return result;
    }

    /** Return a string representation of this array. */
    public Object _asString() {
        return new BStr(toString());
    }

    /** BInt does not have a Babble-visible metaclass. */
    public Object _class() {
        return Core.newOpaqueClass();
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    /** BArray factory used in Babble-based bytecode. */
    public static BArray make(Object[] objects) {
        return new BArray(Arrays.asList(objects));
    }
}
