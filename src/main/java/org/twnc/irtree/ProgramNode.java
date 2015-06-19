package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramNode extends Node{

    private List<MethodNode> methods;
    
    public ProgramNode(List<MethodNode> methods) {
        this.methods = methods;
    }
    
    public List<MethodNode> getMethods() {
        return methods;
    }
    
    public MethodNode getMethod(int index) {
        return methods.get(index);
    }
    
    public MethodNode getMain() {
        for (MethodNode m : methods) {
            if (m.getSelector().equals("main")) {
                return m;
            }
        }
        return null;
    }
    
    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<Node>();
        children.addAll(methods);
        
        return children;
    }
    
    @Override
    public String toString() {
        return "Program";
    }
}
