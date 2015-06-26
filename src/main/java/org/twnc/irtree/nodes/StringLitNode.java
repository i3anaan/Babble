package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.awt.Color;

public class StringLitNode extends ExprNode {
    private String string;
    
    public StringLitNode(String string) {
        this.string = string;
    }
    
    public String getString() {
        return string;
    }
    
    @Override
    public String toString() {
        return string;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public Color getColor() {
        return new Color(26, 188, 156);
    }
}
