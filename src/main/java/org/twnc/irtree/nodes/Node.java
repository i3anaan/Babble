package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.awt.Color;

public abstract class Node {
    public abstract void accept(ASTVisitor visitor);

    public Color getColor() {
        return new Color(234, 97, 83);
    }
}
