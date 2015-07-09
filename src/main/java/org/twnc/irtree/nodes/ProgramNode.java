package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.List;

public class ProgramNode extends Node {
    private final List<ClazzNode> classes;
    
    public ProgramNode(List<ClazzNode> methods) {
        this.classes = methods;
    }

    public List<ClazzNode> getClasses() {
        return classes;
    }

    @Override
    public String toString() {
        return "Program";
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
