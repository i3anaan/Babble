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
    
    public boolean addClazz(ClazzNode clazz) {
        return classes.add(clazz);
    }
    
    public ClazzNode getClazz(String name) {
        for (ClazzNode clazz : getClasses()) {
            if (clazz.getName().equals(name)) {
                return clazz;
            }
        }

        return null;
    }
}
