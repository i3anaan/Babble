package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.awt.Color;

public class SymbolNode extends ExprNode {
    private final String id;
    
    public SymbolNode(String id) {
        this.id = id;
    }
    
    public String getID() {
        return id;
    }
    
    public int getNum() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return '#' + id;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public Color getColor() {
        return new Color(46, 204, 113);
    }
}
