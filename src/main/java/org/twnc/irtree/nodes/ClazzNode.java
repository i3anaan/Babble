package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Node that represents a Class in Babble code.
 *
 */
public class ClazzNode extends Node {
    /** The name of the class. */
    private final String name;
    /** Superclass of this class, will be set to Object by default.*/
    private final String superclass;
    /** The methods defined in this class. */
    private final Set<MethodNode> methods;
    /** The variables declared in the class scope (class fields). */
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

    /**
     * Looks for a method in this class with a given selector.
     * @param selector The method signature to look for
     * @return The method with the requested signature, or null if none found.
     */
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
    
    /**
     * Overridden to easily detect duplicate classes. Babble does not care if the
     * contents of a class is different, it only checks the class's name.
     */
    @Override
    public boolean equals(Object other) {
        return other != null && (other instanceof ClazzNode) && ((ClazzNode) other).getName().equals(this.getName());
    }

    
    /**
     * In accordance with the new equals.
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
