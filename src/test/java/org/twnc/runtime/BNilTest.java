package org.twnc.runtime;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BNilTest {
    @Test
    public void testToString() {
        assertEquals("nil", new BNil().toString());
    }
}
