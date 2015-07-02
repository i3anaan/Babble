package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.List;

public class BlockNode extends ExprNode {
    private final SequenceNode sequence;
    private final List<VarRefNode> arguments;

    public BlockNode(SequenceNode sequence, List<VarRefNode> arguments) {
        this.sequence = sequence;
        this.arguments = arguments;
    }    
    
    public List<VarRefNode> getArguments() {
        return arguments;
    }
    
    public SequenceNode getSequence() {
        return sequence;
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
