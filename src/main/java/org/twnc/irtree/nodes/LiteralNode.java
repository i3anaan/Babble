package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.awt.Color;

public class LiteralNode extends ExprNode {
    public enum Type {
        INTEGER,
        STRING,
        SYMBOL
    }

    private final Type type;
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
                return new Color(52, 152, 219);
            case STRING:
                return new Color(26, 188, 156);
            case SYMBOL:
                return new Color(46, 204, 113);
            default:
                return new Color(0, 0, 0);
        }
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
