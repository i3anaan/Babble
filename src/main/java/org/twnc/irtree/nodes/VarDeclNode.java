package org.twnc.irtree.nodes;

import java.awt.Color;
import java.util.List;

import org.twnc.irtree.ASTVisitor;

public class VarDeclNode extends VarRefNode {
    public VarDeclNode(String name) {
        super(name);
    }
        
    @Override
    public String toString() {
        return "| |";
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
