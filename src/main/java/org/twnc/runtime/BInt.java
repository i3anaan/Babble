package org.twnc.runtime;

import java.math.BigInteger;
import java.util.function.BinaryOperator;

public class BInt extends BObject {
    private BigInteger integer;

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
        integer = i.getInteger();
    }

    private static BObject op(BObject a, BObject b, BinaryOperator<BigInteger> lambda) {
        BigInteger ai = ((BInt) a._asInt()).getInteger();
        BigInteger bi = ((BInt) b._asInt()).getInteger();
        return new BInt(lambda.apply(ai, bi));
    }

    private static int cmp(BObject a, BObject b) {
        BigInteger ai = ((BInt) a._asInt()).getInteger();
        BigInteger bi = ((BInt) b._asInt()).getInteger();
        return ai.compareTo(bi);
    }

    public BObject _plus_(BObject that) {
        return op(this, that, BigInteger::add);
    }

    public BObject _minus_(BObject that) {
        return op(this, that, BigInteger::subtract);
    }

    public BObject _star_(BObject that) {
        return op(this, that, BigInteger::multiply);
    }

    public BObject _slash_(BObject that) {
        return op(this, that, BigInteger::divide);
    }

    public BObject _mod_(BObject that) {
        return op(this, that, BigInteger::mod);
    }

    public BObject _lt_(BObject that) {
        return BBool.of(cmp(this, that) < 0);
    }

    public BObject _eqeq_(BObject that) {
        return BBool.of(cmp(this, that) == 0);
    }

    public BObject _gt_(BObject that) {
        return BBool.of(cmp(this, that) > 0);
    }

    public BObject _gteq_(BObject that) {
        return BBool.of(cmp(this, that) >= 0);
    }

    public BObject _bangeq_(BObject that) {
        return BBool.of(cmp(this, that) != 0);
    }

    public BObject _lteq_(BObject that) {
        return BBool.of(cmp(this, that) <= 0);
    }

    public BObject _negate() {
        return new BInt(this.getInteger().negate());
    }

    public BObject _abs() {
        return new BInt(this.getInteger().abs());
    }

    public BObject _asBool() {
        return BBool.of(!this.getInteger().equals(BigInteger.ZERO));
    }

    public BInt _asInt() {
        return new BInt(this);
    }

    private BigInteger getInteger() {
        return integer;
    }

    @Override
    public String toString() {
        return integer.toString();
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof BInt && ((BInt)that).getInteger().equals(this.getInteger());
    }

    @Override
    public int hashCode() {
        return this.getInteger().hashCode();
    }
}
