package org.twnc.runtime;

public class BStr extends BObject {
    private String str;

    public BStr(String str) {
        this.str = str;
    }

    public BStr(BStr bstr) {
        this.str = bstr.toString();
    }

    public BObject _asBool() {
        return BBool.of(!str.isEmpty());
    }

    public BObject _upper() {
        return new BStr(str.toUpperCase());
    }

    public BObject _lower() {
        return new BStr(str.toLowerCase());
    }

    public BObject _startsWith_(BObject that) {
        return BBool.of(str.startsWith(that.toString()));
    }

    public BObject _endsWith_(BObject that) {
        return BBool.of(str.endsWith(that.toString()));
    }

    public BObject _contains_(BObject that) {
        return BBool.of(str.contains(that.toString()));
    }

    public BObject _replace_with_(BObject search, BObject replace) {
        return new BStr(str.replace(search.toString(), replace.toString()));
    }

    public BObject _comma_(BObject that) {
        return new BStr(str.concat(that.toString()));
    }

    @Override
    public String toString() {
        return str;
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof BStr && this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }
}
