package org.twnc;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;

import org.twnc.compile.exceptions.VariableNotDeclaredException;
import org.twnc.irtree.nodes.VarDeclNode;
import org.twnc.irtree.nodes.VarRefNode;

public class ScopeStack {
    private LinkedList<Scope> list;
    
    public ScopeStack() {
        list = new LinkedList<Scope>();
        list.push(new Scope());
    }
    
    public Scope enterScope() {
        Scope scope = new Scope();
        list.push(scope);
        return scope;
    }
    
    public Scope exitScope() {
        if (list.size() > 1) {
            return list.pop();
        } else {
            throw new EmptyStackException();
        }
    }
    
    public VarDeclNode getVarDeclNode(String name) throws VariableNotDeclaredException {
        for (Scope scope : list) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        throw new VariableNotDeclaredException();
    }
    
    public boolean contains(String name) {
        for (Scope scope : list) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean putVarDeclNode(VarDeclNode node) {
        if (!list.peek().containsKey(node.getName())) {
            list.peek().put(node.getName(), node);
            return true;
        }
        return false;
    }
    
    public Scope peek() {
        return list.peek();
    }
}
