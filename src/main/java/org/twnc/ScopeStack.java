package org.twnc;

import java.util.EmptyStackException;

import org.twnc.compile.exceptions.VariableNotDeclaredException;
import org.twnc.irtree.nodes.Node;
import org.twnc.irtree.nodes.VarDeclNode;

public class ScopeStack {
    private Scope bottomScope;
    public static final String[] SPECIAL_VARS = {"true", "false", "nil"};

    public ScopeStack(Node node) {
        bottomScope = new Scope(null, node);
    }

    public Scope enterScope(Node node) {
        Scope scope = new Scope(bottomScope, node);
        bottomScope = scope;
        return scope;
    }

    public Scope exitScope() {
        if (bottomScope.hasParentScope()) {
            bottomScope = bottomScope.getParentScope();
            return bottomScope;
        } else {
            throw new EmptyStackException();
        }
    }

    public VarDeclNode getVarDeclNode(String name) throws VariableNotDeclaredException {
        return bottomScope.getVarDeclNode(name);
    }

    public boolean contains(String name) {
        return bottomScope.contains(name);
    }

    public boolean putVarDeclNode(VarDeclNode node) {
        if (!bottomScope.containsKey(node.getName()) && !isSpecial(node.getName())) {
            node.setScope(bottomScope);
            bottomScope.put(node.getName(), node);
            return true;
        }
        return false;
    }

    public Scope peek() {
        return bottomScope;
    }

    public static boolean isSpecial(String varName) {
        for (String s : SPECIAL_VARS) {
            if (varName.equals(s)) {
                return true;
            }
        }

        return false;
    }
}
