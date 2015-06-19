package org.twnc;

import java.util.EmptyStackException;
import java.util.Stack;

public class ScopeStack extends Stack<Scope>{
    
    public ScopeStack() {
        this.push(new Scope());
    }
    
    public Scope enterScope() {
        Scope scope = new Scope(this.peek());
        this.push(scope);
        return scope;
    }
    
    public Scope exitScope() {
        if (this.size() > 1) {
            return this.pop();
        } else {
            throw new EmptyStackException();
        }
    }
    
    public Variable getVariable(String name) {
        return this.peek().get(name);
    }
}
