package org.twnc.irtree;

public class VarRefNode extends ExprNode{

    private String name;
    
    public VarRefNode(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
