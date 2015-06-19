package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodNode extends Node{

    private VarRefNode objectName;
    private String selector;
    private List<VarRefNode> arguments;
    private SequenceNode sequence;

    public MethodNode(String selector,  List<VarRefNode> arguments, SequenceNode sequence) {
        this.objectName = null; //TODO not so pretty
        this.selector = selector;
        this.arguments = arguments;
        this.sequence = sequence;
    }
    
    public MethodNode(VarRefNode objectName, String selector,  List<VarRefNode> arguments, SequenceNode sequence) {
        this.objectName = objectName;
        this.selector = selector;
        this.arguments = arguments;
        this.sequence = sequence;
    }

    public VarRefNode getObjectName() {
        return objectName;
    }

    public String getSelector() {
        return selector;
    }

    public List<VarRefNode> getArguments() {
        return arguments;
    }
    
    public VarRefNode getArgument(int index) {
        return arguments.get(index);
    }
    
    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<Node>();
        if (objectName != null) {
            children.add(objectName);
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
