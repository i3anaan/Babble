package org.twnc.irtree.nodes;

import org.twnc.Scope;
import org.twnc.irtree.ASTVisitor;

import java.awt.Color;

public abstract class Node {
    public abstract void accept(ASTVisitor visitor);

    private int line = -1;
    private int lineOffset = -1;
    
    /**
     * Each Node has a reference to one Scope, multiple Nodes can refer to the
     * same Scope. Each Scope refers to the topmost Node in its Scope.
     */
    private Scope scope;

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

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Color getColor() {
        return new Color(234, 97, 83);
    }
}
