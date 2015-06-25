package org.twnc.irtree.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.twnc.irtree.ASTVisitor;

public class MethodNode extends Node {
    private String selector;
    private List<VarRefNode> arguments;
    private SequenceNode sequence;

    public MethodNode(String selector,  List<VarRefNode> arguments, SequenceNode sequence) {
        this.selector = selector;
        this.arguments = arguments;
        this.sequence = sequence;
    }
    
    public MethodNode(String selector, SequenceNode sequence) {
        this(selector, new ArrayList<>(), sequence);
    }

    public String getSelector() {
        return selector;
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
        children.addAll(arguments);
        children.add(sequence);
        
        return children;
    }

    @Override
    public String toString() {
        return "Method "+selector;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
