package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MethodNode extends Node {
    private Optional<VarRefNode> objectName;
    private String selector;
    private List<VarRefNode> arguments;
    private SequenceNode sequence;

    public MethodNode(String selector,  List<VarRefNode> arguments, SequenceNode sequence) {
        this.objectName = Optional.empty(); //TODO not so pretty
        this.selector = selector;
        this.arguments = arguments;
        this.sequence = sequence;
    }
    
    public MethodNode(VarRefNode objectName, String selector,  List<VarRefNode> arguments, SequenceNode sequence) {
        this.objectName = Optional.ofNullable(objectName);
        this.selector = selector;
        this.arguments = arguments;
        this.sequence = sequence;
    }

    public Optional<VarRefNode> getObjectName() {
        return objectName;
    }

    public String getSelector() {
        return selector;
    }

    public List<VarRefNode> getArguments() {
        return arguments;
    }
    
    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<Node>();
        if (objectName.isPresent()) {
            children.add(objectName.get());
        }
        children.addAll(arguments);
        children.add(sequence);
        
        return children;
    }
    
    @Override
    public String toString() {
        return "Method "+selector;
    }
}
