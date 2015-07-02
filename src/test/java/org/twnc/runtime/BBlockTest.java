package org.twnc.runtime;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BBlockTest {
    @Test
    public void testDefaultBlockReturnsNil() {
        assertEquals(Core.newNil(), new BBlock()._value());
    }
}
