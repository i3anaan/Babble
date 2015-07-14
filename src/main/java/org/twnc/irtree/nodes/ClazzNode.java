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

    public boolean hasMain() {
        return methods.stream().anyMatch(MethodNode::isMainMethod);
    }
    
    public void mergeTree(ClazzNode other) {
        for (MethodNode newMethod : other.getMethods()) {
            for (MethodNode existingMethod : methods) {
                if (existingMethod.getSelector().equals(newMethod.getSelector())) {
                    String message = String.format(
                            "Duplicate Method '%s' at [%s] and [%s]",
                            newMethod.getSelector(), newMethod.getLocation(),
                            existingMethod.getLocation());
                    throw new DuplicateMethodSignatureException(message);
                }
            }
            methods.add(newMethod);
        }
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return "Class: " + name;
    }
}
