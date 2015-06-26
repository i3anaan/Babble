package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.awt.Color;

public class VarRefNode extends ExprNode {
    public static VarRefNode TRUE = new VarRefNode("true");
    public static VarRefNode FALSE = new VarRefNode("false");
    public static VarRefNode NIL = new VarRefNode("nil");
    
    private final String name;
    
    public VarRefNode(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return '@' + name;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public Color getColor() {
        return new Color(233, 139, 57);
    }
}
