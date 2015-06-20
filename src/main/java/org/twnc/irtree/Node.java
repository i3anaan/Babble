package org.twnc.irtree;

import java.util.List;

public abstract class Node {
    private int line = -1;
    private int offset = -1;

    public abstract List<Node> getChildren();

    public void setLine(int line) {
        this.line = line;
    }

    public void setLineOffset(int line, int offset) {
        this.line = line;
        this.offset = offset;
    }

    public int getLine() {
        return line;
    }

    public int getOffset() {
        return offset;
    }
}
