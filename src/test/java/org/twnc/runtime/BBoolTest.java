package org.twnc.runtime;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BBoolTest {
    private BBool TRUE = new BTrue();
    private BBool FALSE = new BFalse();

    @Test public void testTrueEqTrue()    { assertEquals(TRUE, TRUE); }
    @Test public void testFalseEqFalse()  { assertEquals(FALSE, FALSE); }
    @Test public void testTrueNeqFalse()  { assertNotEquals(TRUE, FALSE); }
    @Test public void testFalseNeqTrue()  { assertNotEquals(FALSE, TRUE); }

    @Test public void testBBoolOfTrue()   { assertEquals(TRUE, BBool.of(true)); }
    @Test public void testBBoolOfFalse()  { assertEquals(FALSE, BBool.of(false)); }

    @Test public void testNotTrue()       { assertEquals(FALSE, TRUE._not()); }
    @Test public void testNotFalse()      { assertEquals(TRUE, FALSE._not()); }

    @Test public void testTrueAndTrue()   { assertEquals(TRUE, TRUE._and_(TRUE)); }
    @Test public void testTrueAndFalse()  { assertEquals(FALSE, TRUE._and_(FALSE)); }
    @Test public void testFalseAndTrue()  { assertEquals(FALSE, FALSE._and_(TRUE)); }
    @Test public void testFalseAndFalse() { assertEquals(FALSE, FALSE._and_(FALSE)); }

    @Test public void testTrueOrTrue()    { assertEquals(TRUE, TRUE._or_(TRUE)); }
    @Test public void testTrueOrFalse()   { assertEquals(TRUE, TRUE._or_(FALSE)); }
    @Test public void testFalseOrTrue()   { assertEquals(TRUE, FALSE._or_(TRUE)); }
    @Test public void testFalseOrFalse()  { assertEquals(FALSE, FALSE._or_(FALSE)); }

    @Test public void testTrueAsInt()     { assertEquals(new BInt(1), TRUE._asInt()); }
    @Test public void testFalseAsInt()    { assertEquals(new BInt(0), FALSE._asInt()); }

    @Test public void testAssert() { assertEquals(TRUE, TRUE._assert()); }
}
