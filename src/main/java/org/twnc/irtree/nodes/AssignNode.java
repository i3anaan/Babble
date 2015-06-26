package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

public class AssignNode extends ExprNode {
    private final VarRefNode variable;
    private final ExprNode expression;
    
    public AssignNode(VarRefNode variable, ExprNode expression) {
        this.variable = variable;
        this.expression = expression;
    }
    
    public VarRefNode getVariable() {
        return variable;
    }
    
    public ExprNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "Assign";
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
