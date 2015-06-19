package org.twnc.irtree;

import java.util.Arrays;
import java.util.List;

public class MethodNode extends Node{

    private VarRefNode objectName;
    private String selector;
    private List<VarRefNode> arguments;

    public MethodNode(String selector,  List<VarRefNode> arguments) {
        this.objectName = null; //TODO not so pretty
        this.selector = selector;
        this.arguments = arguments;
    }
    
    public MethodNode(VarRefNode objectName, String selector,  List<VarRefNode> arguments) {
        this.objectName = objectName;
        this.selector = selector;
        this.arguments = arguments;
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
}
