package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A Node that represents a message send, method call, in the Babble code.
 *
 */
public class SendNode extends ExprNode {
    /** The expression (object) for which this message is intended. */
    private final ExprNode expression;
    /** Selector of the method to call. */
    private final String selector;
    /** List of arguments the method requires. */
    private final List<ExprNode> arguments;

    public SendNode(ExprNode expression, String selector, List<ExprNode> arguments) {
        this.expression = expression;
        this.selector = selector;
        this.arguments = arguments;
    }

    public SendNode(ExprNode expression, String selector) {
        this(expression, selector, new ArrayList<>());
    }

    public ExprNode getExpression() {
        return expression;
    }

    public String getSelector() {
        return selector;
    }

    public List<ExprNode> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "Send "+selector;
    }

    @Override
    public Color getColor() {
        return new Color(189, 195, 199);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
