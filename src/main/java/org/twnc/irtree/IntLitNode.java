package org.twnc.irtree;

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
}
