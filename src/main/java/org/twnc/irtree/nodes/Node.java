package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.awt.Color;

public abstract class Node {
    public abstract <T> T accept(ASTVisitor<T> visitor);

    public Color getColor() {
        return new Color(234, 97, 83);
    }
}
