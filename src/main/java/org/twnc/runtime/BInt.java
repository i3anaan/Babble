package org.twnc.runtime;

import java.math.BigInteger;
import java.util.function.BinaryOperator;

public class BInt {
    private final BigInteger integer;

    public BInt(int i) {
        integer = BigInteger.valueOf(i);
    }

    public BInt(BigInteger bi) {
        integer = bi;
    }

    public BInt(String s) {
        integer = new BigInteger(s);
    }

    public BInt(BInt i) {
        integer = i.integer;
    }

    private static Object op(Object a, Object b, BinaryOperator<BigInteger> lambda) {
        BigInteger ai = ((BInt) a)._asInt().integer;
        BigInteger bi = ((BInt) b)._asInt().integer;
        return new BInt(lambda.apply(ai, bi));
    }

    private static int cmp(Object a, Object b) {
        BigInteger ai = ((BInt) a)._asInt().integer;
        BigInteger bi = ((BInt) b)._asInt().integer;
        return ai.compareTo(bi);
    }

    public Object _plus_(Object that)   { return BInt.op(this, that, BigInteger::add); }
    public Object _minus_(Object that)  { return BInt.op(this, that, BigInteger::subtract); }
    public Object _star_(Object that)   { return BInt.op(this, that, BigInteger::multiply); }
    public Object _slash_(Object that)  { return BInt.op(this, that, BigInteger::divide); }
    public Object _mod_(Object that)    { return BInt.op(this, that, BigInteger::mod); }

    public Object _min_(Object that)    { return BInt.op(this, that, BigInteger::min); }
    public Object _max_(Object that)    { return BInt.op(this, that, BigInteger::max); }

    public Object _eqeq_(Object that)   { return BBool.of(BInt.cmp(this, that) == 0); }
    public Object _gt_(Object that)     { return BBool.of(BInt.cmp(this, that) > 0); }
    public Object _gteq_(Object that)   { return BBool.of(BInt.cmp(this, that) >= 0); }
    public Object _bangeq_(Object that) { return BBool.of(BInt.cmp(this, that) != 0); }
    public Object _lteq_(Object that)   { return BBool.of(BInt.cmp(this, that) <= 0); }

    public Object _abs()    { return new BInt(integer.abs()); }
    public Object _negate() { return new BInt(integer.negate()); }
    public Object _signum() { return new BInt(integer.signum()); }

    public BInt _asInt() {
        return new BInt(this);
    }

    @Override
    public String toString() {
        return integer.toString();
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof BInt && ((BInt)that).integer.equals(integer);
    }

    @Override
    public int hashCode() {
        return integer.hashCode();
    }
}
