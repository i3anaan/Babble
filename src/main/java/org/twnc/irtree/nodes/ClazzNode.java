package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.List;

public class ClazzNode extends Node {
    private final String name;
    private final List<MethodNode> methods;

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
