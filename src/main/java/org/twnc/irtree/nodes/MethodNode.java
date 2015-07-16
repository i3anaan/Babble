package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * A Node that represents a method in Babble code.
 *
 */
public class MethodNode extends Node {
    /** The selector, or signature, that is used to identify this method. */
    private final String selector;
    /** Local variables declared in the scope of this method. */
    private final List<VarDeclNode> arguments;
    /** List of expressions that define the functionallity of the method. */
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
    
    /**
     * @return The amount of parameters this method has.
     */
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
    
    /**
     * Overriden to easily detect duplicate methods. Babble does not care if the
     * functionallity of a method is different, it only checks the method's
     * signature.
     */
    @Override
    public boolean equals(Object other) {
        return other != null && (other instanceof MethodNode) && ((MethodNode) other).getSelector().equals(this.getSelector());
    }
    
    /**
     * In accordance with the new equals.
     */
    @Override
    public int hashCode() {
        return selector.hashCode();
    }
}
