package org.twnc.runtime;

public class BStr {
    private final String str;

    public BStr(String str) {
        this.str = str;
    }

    public BStr(BStr bstr) {
        str = bstr.toString();
    }

    public Object _upper() {
        return new BStr(str.toUpperCase());
    }

    public Object _lower() {
        return new BStr(str.toLowerCase());
    }

    public Object _startsWith_(Object that) {
        return BBool.of(str.startsWith(that.toString()));
    }

    public Object _endsWith_(Object that) {
        return BBool.of(str.endsWith(that.toString()));
    }

    public Object _contains_(Object that) {
        return BBool.of(str.contains(that.toString()));
    }

    public Object _replace_with_(Object search, Object replace) {
        return new BStr(str.replace(search.toString(), replace.toString()));
    }

    public Object _plus_(Object that) {
        return new BStr(str.concat(that.toString()));
    }

    public Object _eqeq_(Object that) {
        return BBool.of(equals(that));
    }

    public Object _print() {
        System.out.println(str);
        return this;
    }

    @Override
    public String toString() {
        return str;
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof BStr && toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }
}
