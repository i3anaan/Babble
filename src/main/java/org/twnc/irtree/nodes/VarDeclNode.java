package org.twnc.irtree.nodes;

import java.awt.Color;

import org.twnc.irtree.ASTVisitor;

public class VarDeclNode extends VarRefNode {
    private int offset;
    
    public VarDeclNode(String name) {
        super(name);
    }
   
    public int getOffset() {
        return offset;
    }
    
    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    
    @Override
    public String toString() {
        return getName();
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public Color getColor() {
        return new Color(100, 100, 100);
    }
}
