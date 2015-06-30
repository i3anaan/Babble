package org.twnc;

import java.util.HashMap;
import java.util.Map;

public class Scope extends HashMap<String, Variable>{
    
    public Scope() {
    }
    
    public Scope(Map scope) {
        super(scope);
    }
}
