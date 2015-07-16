package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.List;

/**
 * A Node that represents an Array literal in Babble code.
 *
 */
public class ArrayNode extends ExprNode {
    /**
     * List of expressions in the array, these are the array elements.
     */
    private final List<ExprNode> expressions;

    public ArrayNode(List<ExprNode> expressions) {
        this.expressions = expressions;
    }

    public List<ExprNode> getExpressions() {
        return expressions;
    }

    @Override
    public String toString() {
        return "{}";
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
