package org.twnc;

import java.util.HashMap;
import java.util.Map;

import org.twnc.compile.exceptions.VariableNotDeclaredException;
import org.twnc.irtree.nodes.VarDeclNode;

public class Scope extends HashMap<String, VarDeclNode>{
    private Scope parent;
    
    public Scope(Scope parent) {
        this.parent = parent;
    }
    
    @Override
    public VarDeclNode put(String name, VarDeclNode decl) {
        decl.setOffset(this.size());        
        return super.put(name, decl);
    }
    
    public VarDeclNode getVarDeclNode(String name) throws VariableNotDeclaredException {
        Scope scope = this;
        while (scope != null) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
            scope = scope.getParent();
        }
        throw new VariableNotDeclaredException();
    }
    
    public Scope getParent() {
        return parent;
    }
}
