package org.twnc.irtree.nodes;

import java.util.ArrayList;
import java.util.List;

import org.twnc.irtree.ASTVisitor;

public class ClazzNode extends Node {
    String name;
    List<MethodNode> methods;

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
    public List<Node> getChildren() {
        return new ArrayList<>(methods);
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return "Class: " + name;
    }

}
