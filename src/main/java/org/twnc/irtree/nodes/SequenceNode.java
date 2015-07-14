package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SequenceNode extends Node {
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
