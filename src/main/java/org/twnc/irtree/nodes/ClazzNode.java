package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.List;

public class ClazzNode extends Node {
    private final String name;
    private final String superclass;
    private final DeclsNode declarations;
    private final List<MethodNode> methods;

    public ClazzNode(String name, String superclass, DeclsNode declarations, List<MethodNode> methods) {
        this.name = name;
        this.superclass = superclass;
        this.declarations = declarations;
        this.methods = methods;
    }
    
    public String getName() {
        return name;
    }

    public String getSuperclass() {
        return superclass;
    }

    public DeclsNode getDecls() {
        return declarations;
    }
    
    public List<MethodNode> getMethods() {
        return methods;
    }

    @Override
    public String toString() {
        return "Class: " + name;
    }

    public boolean hasMain() {
        return methods.stream().anyMatch(MethodNode::isMainMethod);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
