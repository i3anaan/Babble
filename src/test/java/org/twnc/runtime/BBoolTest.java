package org.twnc.runtime;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BBoolTest {
    private static BObject TRUE;
    private static BObject FALSE;

    static {
        TRUE = BBool.of(true);
        FALSE = BBool.of(false);
    }

    @Test public void testTrueEqTrue()    { assertEquals(TRUE, TRUE); }
    @Test public void testFalseEqFalse()  { assertEquals(FALSE, FALSE); }
    @Test public void testTrueNeqFalse()  { assertNotEquals(TRUE, FALSE); }
    @Test public void testFalseNeqTrue()  { assertNotEquals(FALSE, TRUE); }

    @Test public void testBBoolOfTrue()   { assertEquals(TRUE, BBool.of(true)); }
    @Test public void testBBoolOfFalse()  { assertEquals(FALSE, BBool.of(false)); }
}
