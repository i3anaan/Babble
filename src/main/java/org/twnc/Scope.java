package org.twnc;

import java.util.HashMap;

public class Scope extends HashMap<String, Variable>{
    
    public Scope() {
        
    }
    
    public Scope(Scope scope) {
        super(scope);
    }
}
