package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class MethodNode extends Node {
    private final String selector;
    private final List<VarRefNode> arguments;
    private final SequenceNode sequence;

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

    public boolean isMainMethod() {
        return selector.equals("main");
    }

    public boolean isTestMethod() {
        return selector.startsWith("test") && arguments.isEmpty();
    }
}
