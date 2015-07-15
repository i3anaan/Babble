package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.Set;
import java.util.stream.Collectors;

public class ClazzNode extends Node {
    private final String name;
    private final String superclass;
    private final Set<MethodNode> methods;
    private final DeclsNode declarations;

    public ClazzNode(String name, String superclass, DeclsNode declarations, Set<MethodNode> methods) {
        this.name = name;
        this.superclass = superclass;
        this.declarations = declarations;
        this.methods = methods;
    }
    
    public String getName() {
        return name;
    }

    public String getSuperclass() {
        return superclass;
    }

    public DeclsNode getDecls() {
        return declarations;
    }

    public Set<MethodNode> getMethods() {
        return methods;
    }

    public Set<String> getMethodSelectors() {
        return methods.stream().map(MethodNode::getSelector).collect(Collectors.toSet());
    }

    public boolean hasMain() {
        return methods.stream().anyMatch(MethodNode::isMainMethod);
    }

    public boolean addMethod(MethodNode extraMethod) {
        return methods.add(extraMethod);
    }

    public MethodNode getMethod(String selector) {
        for (MethodNode method : getMethods()) {
            if (method.getSelector().equals(selector)) {
                return method;
            }
        }

        return null;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Class: " + name;
    }

    @Override
    public boolean equals(Object other) {
        return other != null && (other instanceof ClazzNode) && ((ClazzNode) other).getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
