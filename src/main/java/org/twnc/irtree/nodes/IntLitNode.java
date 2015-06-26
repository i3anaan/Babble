package org.twnc.irtree.nodes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.twnc.irtree.ASTVisitor;

public class IntLitNode extends ExprNode {

    private String value;
    
    
    public IntLitNode(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public Color getColor() {
        return new Color(52, 152, 219);
    }
}
