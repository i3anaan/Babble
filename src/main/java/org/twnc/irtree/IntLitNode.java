package org.twnc.irtree;

import java.util.ArrayList;
import java.util.List;

public class IntLitNode extends ExprNode {

    private int value;
    
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
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
