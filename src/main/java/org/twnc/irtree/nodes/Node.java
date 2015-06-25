package org.twnc.irtree.nodes;

import java.util.ArrayList;
import java.util.List;

import org.twnc.irtree.ASTVisitor;

public abstract class Node {
    public abstract <T> T accept(ASTVisitor<T> visitor);

    public List<Node> getChildren() {
        return new ArrayList<Node>();
    }
}
