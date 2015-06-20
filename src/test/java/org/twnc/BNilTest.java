package org.twnc;

import org.junit.Test;
import org.twnc.runtime.BNil;

import static org.junit.Assert.*;

public class BNilTest {
    @Test
    public void testToString() {
        assertEquals("nil", new BNil().toString());
    }
}
