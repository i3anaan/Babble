package org.twnc.runtime;

/**
 * A Babble String. Strings are immutable: operations will always return a
 * new String.
 *
 * BStr is a simple wrapper around java.lang.String.
 */
public class BStr {
    /** The Java string we're wrapping. */
    private final String str;

    /** Make a new BStr from a Java String. */
    public BStr(String str) {
        this.str = str;
    }

    /** Make a new BStr from another BStr. */
    public BStr(BStr bstr) {
        this(bstr.toString());
    }

    /** Return an uppercased version of this string. */
    public Object _upper() {
        return new BStr(str.toUpperCase());
    }

    /** Return a lowercased version of this string. */
    public Object _lower() {
        return new BStr(str.toLowerCase());
    }

    /** Return True if the string starts with the given substring. */
    public Object _startsWith_(Object that) {
        return BBool.of(str.startsWith(that.toString()));
    }

    /** Return True if the string ends with the given substring. */
    public Object _endsWith_(Object that) {
        return BBool.of(str.endsWith(that.toString()));
    }

    /** Return True if the string contains the given substring. */
    public Object _contains_(Object that) {
        return BBool.of(str.contains(that.toString()));
    }

    /**
     * Return this string, but with all occurrences of the first argument
     * replaced with the second argument.
     */
    public Object _replace_with_(Object search, Object replace) {
        return new BStr(str.replace(search.toString(), replace.toString()));
    }

    /** Return the concatenation of this string with another string. */
    public Object _plus_(Object that) {
        return new BStr(str.concat(that.toString()));
    }

    /** ==: Return True if this string is equal to the other string. */
    public Object _eqeq_(Object that) {
        return BBool.of(equals(that));
    }

    /** !=: Return True if this string is not equal to the other string. */
    public Object _bangeq_(Object that) {
        return BBool.of(!equals(that));
    }

    /** Print this string to standard output. */
    public Object _print() {
        System.out.println(str);
        return this;
    }

    /** BInt does not have a Babble-visible metaclass. */
    public Object _class() {
        return Core.newOpaqueClass();
    }

    @Override
    public String toString() {
        return str;
    }

    @Override
    public boolean equals(Object that) {
        return (that instanceof BStr) && str.equals(((BStr) that).str);
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }
}
