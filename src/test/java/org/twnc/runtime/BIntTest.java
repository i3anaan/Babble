package org.twnc.runtime;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class BIntTest {
    @Test public void testEquals() { assertEquals(new BInt(10), new BInt(10)); }

    @Test public void testFromBigInt()  { assertEquals(new BInt(10), new BInt(new BigInteger("10"))); }
    @Test public void testFromString()  { assertEquals(new BInt(10), new BInt("10")); }
    @Test public void testFromBInt()    { assertEquals(new BInt(10), new BInt(new BInt(10))); }

    @Test public void testEqEqTrue()    { assertEquals(t(), new BInt(10)._eqeq_(new BInt(10))); }
    @Test public void testEqEqFalse()   { assertEquals(f(), new BInt(10)._eqeq_(new BInt(15))); }

    @Test public void testBangEqTrue()  { assertEquals(f(), new BInt(10)._bangeq_(new BInt(10))); }
    @Test public void testBangEqFalse() { assertEquals(t(), new BInt(10)._bangeq_(new BInt(15))); }

    @Test public void testTenPlusTen()  { assertEquals(new BInt(20), new BInt(10)._plus_(new BInt(10))); }
    @Test public void testTenSubTen()   { assertEquals(new BInt(0), new BInt(10)._minus_(new BInt(10))); }
    @Test public void testTenTimesTen() { assertEquals(new BInt(100), new BInt(10)._star_(new BInt(10))); }
    @Test public void testTenDivTen()   { assertEquals(new BInt(1), new BInt(10)._slash_(new BInt(10))); }

    @Test public void testTenModThree() { assertEquals(new BInt(1), new BInt(10)._mod_(new BInt(3))); }
    @Test public void testTenGtThree()  { assertEquals(t(), new BInt(10)._gt_(new BInt(3))); }
    @Test public void testThreeLtTen()  { assertEquals(t(), new BInt(3)._lt_(new BInt(10))); }

    @Test public void testTenGtEqTen()  { assertEquals(t(), new BInt(10)._gteq_(new BInt(10))); }
    @Test public void testTenGtEqOne()  { assertEquals(t(), new BInt(10)._gteq_(new BInt(1))); }
    @Test public void testTenLtEqOne()  { assertEquals(f(), new BInt(10)._lteq_(new BInt(1))); }

    @Test public void testTenMaxOne()   { assertEquals(new BInt(10), new BInt(10)._max_(new BInt(1))); }
    @Test public void testOneMaxTen()   { assertEquals(new BInt(10), new BInt(1)._max_(new BInt(10))); }
    @Test public void testTenMinOne()   { assertEquals(new BInt(1), new BInt(10)._min_(new BInt(1))); }
    @Test public void testOneMinTen()   { assertEquals(new BInt(1), new BInt(1)._min_(new BInt(10))); }

    @Test public void testMinusOneAbs() { assertEquals(new BInt(1), new BInt(-1)._abs()); }
    @Test public void testOneNegate()   { assertEquals(new BInt(-1), new BInt(1)._negate()); }
    @Test public void testMinusTwoSig() { assertEquals(new BInt(-1), new BInt(-2)._signum()); }

    @Test(expected=java.lang.ArithmeticException.class)
    public void testDivByZero() {
        new BInt(10)._slash_(new BInt(0));
    }

    private BObject t() {
        return BBool.of(true);
    }

    private BObject f() {
        return BBool.of(false);
    }
}
