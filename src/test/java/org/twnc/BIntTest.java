package org.twnc;

import org.junit.Test;
import org.twnc.runtime.BFalse;
import org.twnc.runtime.BInt;
import org.twnc.runtime.BTrue;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class BIntTest {
    @Test public void testEquals() { assertEquals(new BInt(10), new BInt(10)); }

    @Test public void testFromBigInt()  { assertEquals(new BInt(10), new BInt(new BigInteger("10"))); }
    @Test public void testFromString()  { assertEquals(new BInt(10), new BInt("10")); }
    @Test public void testFromBInt()    { assertEquals(new BInt(10), new BInt(new BInt(10))); }

    @Test public void testEqEqTrue()    { assertEquals(new BTrue(), new BInt(10)._eqeq_(new BInt(10))); }
    @Test public void testEqEqFalse()   { assertEquals(new BFalse(), new BInt(10)._eqeq_(new BInt(15))); }

    @Test public void testBangEqTrue()  { assertEquals(new BFalse(), new BInt(10)._bangeq_(new BInt(10))); }
    @Test public void testBangEqFalse() { assertEquals(new BTrue(), new BInt(10)._bangeq_(new BInt(15))); }

    @Test public void testTenPlusTen()  { assertEquals(new BInt(20), new BInt(10)._plus_(new BInt(10))); }
    @Test public void testTenSubTen()   { assertEquals(new BInt(0), new BInt(10)._minus_(new BInt(10))); }
    @Test public void testTenTimesTen() { assertEquals(new BInt(100), new BInt(10)._star_(new BInt(10))); }
    @Test public void testTenDivTen()   { assertEquals(new BInt(1), new BInt(10)._slash_(new BInt(10))); }

    @Test(expected=java.lang.ArithmeticException.class)
    public void testDivByZero() {
        new BInt(10)._slash_(new BInt(0));
    }
}