package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

/**
 * A Node that represents an assign statement in Babble code.
 *
 */
public class AssignNode extends ExprNode {
    /** The variable to change. */
    private final VarRefNode variable;
    
    /** The expression (value) the variable should become. */
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
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
