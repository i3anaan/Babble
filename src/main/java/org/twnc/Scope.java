package org.twnc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.twnc.compile.exceptions.VariableNotDeclaredException;
import org.twnc.irtree.nodes.VarDeclNode;

public class Scope {
    /** Null for top-most Scopes. */
    private Scope parent;
    private Map<String, VarDeclNode> mapping;

    public Scope(Scope parent) {
        this.parent = parent;
        mapping = new HashMap<>();
    }

    /**
     * Mutates decl, setting its offset.
     * @param name  key
     * @param decl  value
     * @return The newly put VarDeclNode.
     */
    public VarDeclNode put(String name, VarDeclNode decl) {
        decl.setOffset(mapping.size());
        return mapping.put(name, decl);
    }

    public VarDeclNode getVarDeclNode(String name) throws VariableNotDeclaredException {
        Scope scope = this;
        while (scope != null) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
            scope = scope.getParentScope();
        }
        throw new VariableNotDeclaredException();
    }

    protected boolean contains(String name){
        Scope scope = this;
        while (scope != null) {
            if (scope.containsKey(name)) {
                return true;
            }
            scope = scope.getParentScope();
        }

        return false;
    }

    public boolean containsKey(String name) {
        return mapping.containsKey(name);
    }

    public VarDeclNode get(String name) {
        return mapping.get(name);
    }

    public Collection<VarDeclNode> values() {
        return mapping.values();
    }

    public Scope getParentScope() {
        return parent;
    }

    public boolean hasParentScope() {
        return parent != null;
    }
}
