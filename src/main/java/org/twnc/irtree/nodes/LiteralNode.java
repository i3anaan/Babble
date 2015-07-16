package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.awt.Color;

/**
 * A Node that represents a literal object in Babble code.
 *
 */
public class LiteralNode extends ExprNode {
    /** Literals can be of 4 types. */
    public static enum Type {
        INTEGER,
        STRING,
        SYMBOL,
        CLASS
    }

    /** The type of this literal. */
    private final Type type;
    /** The value of this literal. */
    private final String value;

    public LiteralNode(Type t, String v) {
        type = t;
        value = v;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Color getColor() {
        switch (type) {
            case INTEGER:
                return new Color(52, 152, 255);
            case STRING:
                return new Color(204, 88, 56);
            case SYMBOL:
                return new Color(46, 204, 113);
            default:
                return new Color(0, 0, 0);
        }
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
