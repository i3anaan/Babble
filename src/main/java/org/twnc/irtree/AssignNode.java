package org.twnc.irtree;

import java.util.ArrayList;
import java.util.List;

public class AssignNode extends StatNode{

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
        List<Node> children = new ArrayList<Node>();
        children.add(variable);
        children.add(expression);
        
        return children;
    }
}
