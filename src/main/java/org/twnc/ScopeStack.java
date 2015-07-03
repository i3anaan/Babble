package org.twnc;

import java.util.EmptyStackException;
import java.util.Stack;

import org.twnc.irtree.nodes.VarDeclNode;
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
    
    public VarDeclNode getVarDeclNode(String name) {
        return this.peek().get(name);
    }
    
    public boolean contains(VarRefNode node) {
        return this.peek().containsKey(node.getName());
    }
    
    public boolean putVarDeclNode(VarDeclNode node) {
        if (!this.peek().containsKey(node.getName())) {
            this.peek().put(node.getName(), node);
            return true;
        }
        return false;
    }
}
