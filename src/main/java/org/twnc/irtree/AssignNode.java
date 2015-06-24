package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssignNode extends ExprNode {
    private VarRefNode variable;
    private ExprNode expression;
    
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
    public List<Node> getChildren() {
        return Arrays.asList(variable, expression);
    }
    
    @Override
    public String toString() {
        return "Assign";
    }
}
