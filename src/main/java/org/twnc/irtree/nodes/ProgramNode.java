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
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
    
    public ProgramNode mergeTree(ProgramNode other) {
        this.classes.addAll(other.getClasses());
        return this;
    }
    
    public void compress() {
        for (int a = 0; a < classes.size(); a++) {
            ClazzNode clazz1 = classes.get(a);
            for (int b = a+1; b < classes.size(); b++) {
                if (clazz1.getName().equals(classes.get(b).getName())) {
                    clazz1.mergeTree(classes.remove(b));
                    b--;
                }
            }
        }
    }
}
