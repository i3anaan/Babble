package org.twnc.irtree;

import java.util.ArrayList;
import java.util.List;

public class IntLitNode extends ExprNode {

    int value;
    
    public IntLitNode(int value) {
        this.value = value;
    }
    
    public IntLitNode(String value) {
        this.value = Integer.parseInt(value);
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<Node>();
        return children;
    }
    
    @Override
    public String toString() {
        return value+"";
    }
}
