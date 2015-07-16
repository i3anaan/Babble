package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A Node that represents a sequence of expressions in the Babble code.
 *
 */
public class SequenceNode extends Node {
    /** List of the expressions contained in this sequence. */
    private final List<ExprNode> expressions;

    public SequenceNode() {
        this(Collections.emptyList());
    }

    public SequenceNode(ExprNode... expressions) {
        this(Arrays.asList(expressions));
    }

    public SequenceNode(List<ExprNode> expressions) {
        this.expressions = expressions;
    }

    public List<ExprNode> getExpressions() {
        return expressions;
    }

    @Override
    public String toString() {
        return "Sequence";
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
