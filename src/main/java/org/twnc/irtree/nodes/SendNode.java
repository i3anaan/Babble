package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SendNode extends ExprNode {
    private final Optional<ExprNode> expression;
    private final String selector;
    private final List<ExprNode> arguments;

    public SendNode(ExprNode expression, String selector, List<ExprNode> arguments) {
        this.expression = Optional.ofNullable(expression);
        this.selector = selector;
        this.arguments = arguments;
    }

    public SendNode(ExprNode expression, String selector) {
        this(expression, selector, new ArrayList<>());
    }

    public SendNode(String selector, List<ExprNode> arguments) {
        this(null, selector, arguments);
    }

    public Optional<ExprNode> getExpression() {
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
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public Color getColor() {
        return new Color(189, 195, 199);
    }
}
