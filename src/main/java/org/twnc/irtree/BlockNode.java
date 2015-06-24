package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockNode extends ExprNode {
    private SequenceNode sequence;
    private List<VarRefNode> arguments;
    
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
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<Node>();
        children.add(sequence);
        children.addAll(arguments);
        return children;
    }
    
    @Override
    public String toString() {
        return "Block";
    }
}