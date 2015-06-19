package org.twnc.irtree;

import java.util.Arrays;
import java.util.List;

public class ProgramNode {

    private List<MethodNode> methods;
    
    public ProgramNode(MethodNode... methods) {
        this.methods = Arrays.asList(methods);
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
}
