package org.twnc.runtime;

import java.math.BigInteger;
import java.util.function.BinaryOperator;

/**
 * A Babble integer value. Integer values are:
 *
 * - immutable: Every operation returns a new BInt instance; a BInt instance
 *   is never modified.
 * - arbitrary precision: Operations do not overflow. Values can be as large
 *   as the available memory.
 */
public class BInt extends BigInteger {
    /** Make a new BInt from a string of digits. */
    public BInt(String s) {
        super(s);
    }

    /** Make a new BInt from a Java integer. */
    public BInt(int i) {
        this(Integer.toString(i));
    }

    /** Make a new BInt from a Java BigInteger. */
    public BInt(BigInteger bi) {
        this(bi.toString());
    }

    /** Generic binary operation helper. */
    private static Object op(Object a, Object b, BinaryOperator<BigInteger> lambda) {
        BigInteger ai = (BigInteger) a;
        BigInteger bi = (BigInteger) b;
        return new BInt(lambda.apply(ai, bi));
    }

    /** Generic comparison operation helper. */
    private static int cmp(Object a, Object b) {
        BigInteger ai = (BigInteger) a;
        BigInteger bi = (BigInteger) b;
        return ai.compareTo(bi);
    }

    // Addition, substraction, multiplication, division and modulation methods.
    public Object _plus_(Object that)   { return BInt.op(this, that, BigInteger::add); }
    public Object _minus_(Object that)  { return BInt.op(this, that, BigInteger::subtract); }
    public Object _star_(Object that)   { return BInt.op(this, that, BigInteger::multiply); }
    public Object _slash_(Object that)  { return BInt.op(this, that, BigInteger::divide); }
    public Object _mod_(Object that)    { return BInt.op(this, that, BigInteger::mod); }
    // Comparison methods.
    public Object _eqeq_(Object that)   { return Core.newBool(cmp(this, that) == 0); }
    public Object _gt_(Object that)     { return Core.newBool(cmp(this, that) > 0); }
    public Object _gteq_(Object that)   { return Core.newBool(cmp(this, that) >= 0); }
    public Object _bangeq_(Object that) { return Core.newBool(cmp(this, that) != 0); }
    public Object _lt_(Object that)     { return Core.newBool(cmp(this, that) < 0); }
    public Object _lteq_(Object that)   { return Core.newBool(cmp(this, that) <= 0); }

    /** Return either this or that, whichever is smallest. */
    public Object _min_(Object that)    { return BInt.op(this, that, BigInteger::min); }

    /** Return either this or that, whichever is largest. */
    public Object _max_(Object that)    { return BInt.op(this, that, BigInteger::max); }

    /** Return the absolute value of this BInt. */
    public Object _abs()    { return new BInt(abs()); }

    /** Return this BInt, but negated (positive becomes negative and vice-versa). */
    public Object _negate() { return new BInt(negate()); }

    /** Return the sign of this BInt as a new BInt (either -1, 0 or +1). */
    public Object _signum() { return new BInt(signum()); }


    /** Return a string representation of this BInt. */
    public Object _asString() { return new BStr(toString()); }

    /** Return an array containing the integers from this to that. */
    public Object _to_(Object that) {
        BArray array = new BArray();
        int a = intValue();
        int b = ((Number) that).intValue();

        for (int i = a; i <= b; i++) {
            array.add(new BInt(i));
        }

        return array;
    }

    /** BInt does not have a Babble-visible metaclass. */
    public Object _class() {
        return Core.newOpaqueClass();
    }
}
