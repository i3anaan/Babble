package org.twnc.irtree.nodes;

import org.twnc.Scope;
import org.twnc.irtree.ASTVisitor;

import java.util.List;

public class BlockNode extends ExprNode {
    private final SequenceNode sequence;
    private final List<VarRefNode> arguments;
    private Scope scope;

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

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "Block";
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
