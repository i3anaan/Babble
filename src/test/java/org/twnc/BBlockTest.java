package org.twnc;

import org.junit.Test;
import org.twnc.runtime.BBlock;
import org.twnc.runtime.BNil;

import static org.junit.Assert.*;

public class BBlockTest {
    @Test
    public void testDefaultBlockReturnsNil() {
        assertEquals(new BNil(), new BBlock()._value());
    }
}