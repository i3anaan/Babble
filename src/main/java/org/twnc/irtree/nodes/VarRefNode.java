package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.awt.Color;

/**
 * A Node that represents a reference to a variable in the Babble code.
 *
 */
public class VarRefNode extends ExprNode {
    /** Identifier of the variable referenced. */
    private final String name;
    
    public VarRefNode(String name) {
        this.name = name;
    }
    
    /** @return a new 'true' variable. */
    public static VarRefNode newTrue() {
        return new VarRefNode("true");
    }
    
    /** @return a new 'true' variable. */
    public static VarRefNode newFalse() {
        return new VarRefNode("false");
    }
    
    /** @return a new 'true' variable. */
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
    public Color getColor() {
        return new Color(233, 139, 57);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
