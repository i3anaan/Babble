package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

/**
 * A Node that represents a Block in Babble code.
 *
 */
public class BlockNode extends ExprNode {
    /** Sequence of the expressions contained inside this Block. */
    private final SequenceNode sequence;

    /** The name of the block, this is used to generate its class. */
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
