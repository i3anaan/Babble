package org.twnc.irtree.nodes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.twnc.irtree.ASTVisitor;

public class SymbolNode extends ExprNode {
    private String id;
    
    public SymbolNode(String id) {
        this.id = id;
    }
    
    public String getID() {
        return id;
    }
    
    @Override
    public String toString() {
        return "#"+id;
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
