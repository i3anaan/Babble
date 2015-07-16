package org.twnc.irtree.nodes;

import java.awt.Color;

import org.twnc.irtree.ASTVisitor;

/**
 * A Node that represents a declaration of a variable in the Babble code.
 *
 */
public class VarDeclNode extends VarRefNode {
    /**
     * The stack offset this declaration has if it is a local variable inside a
     * method, required to be able to tell the JVM where this variable is
     * stored.
     */
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
    
    /** @return True if this declaration is done in a method.*/
    public boolean isMethodVariable() {
        return getScope().getNode() instanceof MethodNode;
    }
    
    /** @return True if this declaration is done in a Class.*/
    public boolean isClassField() {
        return getScope().getNode() instanceof ClazzNode;
    }

    /** @return True if this declaration is a copy to achieve lambda closure.*/
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
    
    /**
     * Overridden to easily detect duplicate declarations. Babble does not care if the
     * offset or scope of a variable is different, it only checks the variable's name.
     */
    
    @Override
    public boolean equals(Object other) {
        return other != null && other instanceof VarDeclNode && this.getName().equals(((VarDeclNode) other).getName());
    }
    
    /**
     * In accordance with the new equals.
     */
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
