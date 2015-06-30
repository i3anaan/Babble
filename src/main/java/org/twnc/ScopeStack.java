package org.twnc;

import java.util.EmptyStackException;
import java.util.Stack;

import org.twnc.irtree.nodes.VarRefNode;

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
    
    public Variable getVariable(VarRefNode node) {
        return this.peek().get(node.getName());
    }
    
    public boolean contains(VarRefNode node) {
        return this.peek().containsKey(node.getName());
    }
    
    public boolean putVarRefNode(VarRefNode node) {
        if (!this.peek().containsKey(node.getName())) {
            Variable var = new Variable(); //TODO: construct this Variable based on the VarRefNode.
            this.peek().put(node.getName(), var);
            return true;
        }
        return false;
    }
}
