package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.awt.Color;

public class VarRefNode extends ExprNode {
    
    private final String name;
    
    public VarRefNode(String name) {
        this.name = name;
    }
    
    public static VarRefNode newTrue() {
        return new VarRefNode("true");
    }
    
    public static VarRefNode newFalse() {
        return new VarRefNode("false");
    }
    
    public static VarRefNode newNil() {
        return new VarRefNode("nil");
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
