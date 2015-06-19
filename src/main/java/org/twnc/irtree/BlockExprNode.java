package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockExprNode extends ExprNode{


    private SequenceNode sequence;
    private List<VarRefNode> arguments;
    
    public BlockExprNode(SequenceNode sequence, List<VarRefNode> arguments) {
        this.sequence = sequence;
        this.arguments = arguments;
    }
    
    
    public List<VarRefNode> getArguments() {
        return arguments;
    }
    
    public VarRefNode getArgument(int index) {
        return arguments.get(index);
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
}
