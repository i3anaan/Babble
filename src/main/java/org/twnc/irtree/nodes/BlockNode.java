package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.List;

public class BlockNode extends ExprNode {
    private final SequenceNode sequence;

    private String name;

    public BlockNode(SequenceNode sequence) {
        this.sequence = sequence;
    }
    
    public SequenceNode getSequence() {
        return sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Block";
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
