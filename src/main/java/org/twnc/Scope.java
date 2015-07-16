package org.twnc;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.twnc.compile.exceptions.VariableNotDeclaredException;
import org.twnc.irtree.nodes.Node;
import org.twnc.irtree.nodes.VarDeclNode;

/**
 * Represents a Scope in which variables can be declared.
 *
 */
public class Scope {
    /** Null for top-most Scopes. */
    private Scope parent;
    
    /**
     * Each Node has a reference to one Scope, multiple Nodes can refer to the
     * same Scope. Each Scope refers to the topmost Node in its Scope.
     */
    private Node node;
    /**
     * Links names of variables to their VarDeclNode (if they have one) in this
     * Scope.
     */
    private Map<String, VarDeclNode> mapping;

    public Scope(Scope parent, Node node) {
        this.parent = parent;
        this.node = node;
        mapping = new HashMap<String,VarDeclNode>();
    }

    /**
     * Puts the given VarDeclNode in the variable mapping, under the given name.
     * This mutates decl, setting its offset.
     * @param name  key
     * @param decl  value
     * @return The newly put VarDeclNode.
     */
    public VarDeclNode put(String name, VarDeclNode decl) {
        decl.setOffset(mapping.size());
        return mapping.put(name, decl);
    }

    /**
     * @param name The name of the variable to get.
     * @return The declaration belonging to the given name.
     * @throws VariableNotDeclaredException When there is no declaration with the given name.
     */
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

    /**
     * @param name The name of the variable to check.
     * @return Whether or not this Scope contains a declaration for the given variable.
     */
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
    
    public Node getNode() {
        return node;
    }

    /** Return a flattened view of all VarDecls in and above this Scope. */
    public Collection<VarDeclNode> flatten() {
        if (hasParentScope()) {
            Collection<VarDeclNode> set = new HashSet<>(parent.flatten());
            set.addAll(mapping.values());
            return set;
        } else {
            return mapping.values();
        }
    }
}
