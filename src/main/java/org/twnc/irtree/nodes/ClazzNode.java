package org.twnc.irtree.nodes;

import org.twnc.Scope;
import org.twnc.irtree.ASTVisitor;

import java.util.List;

public class ClazzNode extends Node {
    private final String name;
    private final List<MethodNode> methods;
    private Scope scope;

    public ClazzNode(String name, List<MethodNode> methods) {
        this.name = name;
        this.methods = methods;
    }
    
    public String getName() {
        return name;
    }
    
    public List<MethodNode> getMethods() {
        return methods;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return "Class: " + name;
    }

    public boolean hasMain() {
        return methods.stream().anyMatch(MethodNode::isMainMethod);
    }
}
