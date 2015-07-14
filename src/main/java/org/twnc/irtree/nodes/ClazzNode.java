package org.twnc.irtree.nodes;

import org.twnc.compile.exceptions.DuplicateMethodSignatureException;
import org.twnc.irtree.ASTVisitor;

import java.util.List;
import java.util.stream.Collectors;

public class ClazzNode extends Node {
    private final String name;
    private final String superclass;
    private final List<MethodNode> methods;

    public ClazzNode(String name, String superclass, List<MethodNode> methods) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
    }
    
    public String getName() {
        return name;
    }

    public String getSuperclass() {
        return superclass;
    }

    public List<MethodNode> getMethods() {
        return methods;
    }

    public List<String> getMethodSelectors() {
        return methods.stream().map(MethodNode::getSelector).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Class: " + name;
    }

    public boolean hasMain() {
        return methods.stream().anyMatch(MethodNode::isMainMethod);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
    
    public void mergeTree(ClazzNode other) {
        for (MethodNode method : other.getMethods()) {
            if (!methods.contains(method)) {
                methods.add(method);
            } else {
                throw new DuplicateMethodSignatureException();
            }
        }
    }
}
