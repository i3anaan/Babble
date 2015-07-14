package org.twnc.irtree.nodes;

import org.twnc.compile.exceptions.DuplicateMethodSignatureException;
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
        for (ClazzNode clazz1 : classes) {
            for (int i = 1; i < classes.size(); i++) {
                if (clazz1.getName().equals(classes.get(i))) {
                    clazz1.mergeTree(classes.remove(i));
                }
            }
        }
    }
}
