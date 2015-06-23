package org.twnc.irtree;

import java.util.ArrayList;
import java.util.List;

public class VarRefNode extends ExprNode{

    public static VarRefNode TRUE = new VarRefNode("true");
    public static VarRefNode FALSE = new VarRefNode("false");
    public static VarRefNode NIL = new VarRefNode("nil");
    
    private String name;
    
    public VarRefNode(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return "@"+name;
    }
}
