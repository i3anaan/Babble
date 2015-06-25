package org.twnc.irtree.nodes;

import java.util.ArrayList;
import java.util.List;

import org.twnc.irtree.ASTVisitor;

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
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
