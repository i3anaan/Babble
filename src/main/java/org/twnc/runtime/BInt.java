package org.twnc.runtime;

import java.math.BigInteger;
import java.util.function.BinaryOperator;

public class BInt extends BigInteger {
    public BInt(String s) {
        super(s);
    }

    public BInt(int i) {
        this(Integer.toString(i));
    }

    public BInt(BigInteger bi) {
        this(bi.toString());
    }

    private static Object op(Object a, Object b, BinaryOperator<BigInteger> lambda) {
        BigInteger ai = (BigInteger) a;
        BigInteger bi = (BigInteger) b;
        return new BInt(lambda.apply(ai, bi));
    }

    private static int cmp(Object a, Object b) {
        BigInteger ai = (BigInteger) a;
        BigInteger bi = (BigInteger) b;
        return ai.compareTo(bi);
    }

    public Object _plus_(Object that)   { return BInt.op(this, that, BigInteger::add); }
    public Object _minus_(Object that)  { return BInt.op(this, that, BigInteger::subtract); }
    public Object _star_(Object that)   { return BInt.op(this, that, BigInteger::multiply); }
    public Object _slash_(Object that)  { return BInt.op(this, that, BigInteger::divide); }
    public Object _mod_(Object that)    { return BInt.op(this, that, BigInteger::mod); }

    public Object _min_(Object that)    { return BInt.op(this, that, BigInteger::min); }
    public Object _max_(Object that)    { return BInt.op(this, that, BigInteger::max); }

    public Object _eqeq_(Object that)   { return BBool.of(cmp(this, that) == 0); }
    public Object _gt_(Object that)     { return BBool.of(cmp(this, that) > 0); }
    public Object _gteq_(Object that)   { return BBool.of(cmp(this, that) >= 0); }
    public Object _bangeq_(Object that) { return BBool.of(cmp(this, that) != 0); }
    public Object _lt_(Object that)     { return BBool.of(cmp(this, that) < 0); }
    public Object _lteq_(Object that)   { return BBool.of(cmp(this, that) <= 0); }

    public Object _abs()    { return new BInt(abs()); }
    public Object _negate() { return new BInt(negate()); }
    public Object _signum() { return new BInt(signum()); }

    public Object _to_(Object that) {
        BArray array = new BArray();
        int a = intValue();
        int b = ((Number) that).intValue();

        for (int i = a; i <= b; i++) {
            array.add(new BInt(i));
        }

        return array;
    }

    public Object _class() {
        return Core.newOpaqueClass();
    }

    public Object _asString() { return new BStr(toString()); }
}
