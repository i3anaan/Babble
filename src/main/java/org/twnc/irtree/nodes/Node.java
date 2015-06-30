package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.awt.Color;

public abstract class Node {
    public abstract <T> T accept(ASTVisitor<T> visitor);

    private int line = -1;
    private int lineOffset = -1;

    public void setLine(int line) {
        this.line = line;
    }

    public void setLineOffset(int line, int lineOffset) {
        this.line = line;
        this.lineOffset = lineOffset;
    }

    public int getLine() {
        return line;
    }

    public int getLineOffset() {
        return lineOffset;
    }

    public Color getColor() {
        return new Color(234, 97, 83);
    }
}
