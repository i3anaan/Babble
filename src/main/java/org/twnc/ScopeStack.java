package org.twnc;

import java.util.EmptyStackException;

import org.twnc.compile.exceptions.VariableNotDeclaredException;
import org.twnc.irtree.nodes.Node;
import org.twnc.irtree.nodes.VarDeclNode;

/**
 * Keeps track of multiple Scopes, their precedence and thus the correct linking of variables to their declaration.
 *
 */
public class ScopeStack {
    /** The bottom most (current) Scope. */
    private Scope bottomScope;

    public ScopeStack(Node node) {
        bottomScope = new Scope(null, node);
    }

    /**
     * Adds a new, deeper, child Scope to what was the bottomScope, the child
     * will then be the bottomScope.
     * 
     * @param node Node to which the new Scope belongs.
     * @return The Scope just created.
     */
    public Scope enterScope(Node node) {
        Scope scope = new Scope(bottomScope, node);
        bottomScope = scope;
        return scope;
    }

    /**
     * Exits the current Scope, moving 1 scope up.
     * bottomScope(new) will be set to bottomScope(old).parent.
     * @return The now current Scope.
     */
    public Scope exitScope() {
        if (bottomScope.hasParentScope()) {
            bottomScope = bottomScope.getParentScope();
            return bottomScope;
        } else {
            throw new EmptyStackException();
        }
    }

    /** Delegated to the current Scope. */
    public VarDeclNode getVarDeclNode(String name) throws VariableNotDeclaredException {
        return bottomScope.getVarDeclNode(name);
    }

    /** Delegated to the current Scope. */
    public boolean contains(String name) {
        return bottomScope.contains(name);
    }

    /**
     * Sets the Scope of the given declaration to the current Scope.
     * Then delegates the putting to the current Scope.
     * @param node Declaration to put.
     * @return Whether or not the putting was successful.
     */
    public boolean putVarDeclNode(VarDeclNode node) {
        if (!bottomScope.containsKey(node.getName())) {
            node.setScope(bottomScope);
            bottomScope.put(node.getName(), node);
            return true;
        }
        return false;
    }

    public Scope peek() {
        return bottomScope;
    }
}
