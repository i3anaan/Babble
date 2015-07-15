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
    
    public boolean isMethodVariable() {
        return getScope().getNode() instanceof MethodNode;
    }
    
    public boolean isClassField() {
        return getScope().getNode() instanceof ClazzNode;
    }

    public boolean isClosureCopy() {
        return getScope().getNode() instanceof BlockNode;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public Color getColor() {
        return new Color(100, 100, 100);
    }
    
    @Override
    public boolean equals(Object other) {
        return other != null && other instanceof VarDeclNode && this.getName().equals(((VarDeclNode) other).getName());
    }
    
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
