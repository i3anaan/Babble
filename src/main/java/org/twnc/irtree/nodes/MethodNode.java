package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class MethodNode extends Node {
    private final String selector;
    private final List<VarDeclNode> arguments;
    private final SequenceNode sequence;

    public MethodNode(String selector,  List<VarDeclNode> arguments, SequenceNode sequence) {
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

    public List<VarDeclNode> getArguments() {
        return arguments;
    }

    public SequenceNode getSequence() {
        return sequence;
    }
    
    public int getArity() {
        return arguments.size();
    }

    @Override
    public String toString() {
        return "Method "+selector;
    }

    public boolean isMainMethod() {
        return selector.equals("main");
    }

    public boolean isTestMethod() {
        return selector.startsWith("test") && arguments.isEmpty();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public boolean equals(Object other) {
        return other != null && (other instanceof MethodNode) && ((MethodNode) other).getSelector().equals(this.getSelector());
    }
    
    @Override
    public int hashCode() {
        return selector.hashCode();
    }
}
