package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequenceNode extends Node {
    private List<ExprNode> expressions;

    public SequenceNode(List<ExprNode> expressions) {
        this.expressions = expressions;
    }

    public List<ExprNode> getExpressions() {
        return expressions;
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<Node>(expressions);
    }

    @Override
    public String toString() {
        return "Sequence";
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
