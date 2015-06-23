package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendNode extends ExprNode{

    private ExprNode statement;
    private String selector;
    private List<ExprNode> arguments;
    
    public SendNode(ExprNode statement, String selector) {
        this.statement = statement;
        this.selector = selector;
        this.arguments = new ArrayList<>();
    }
    
    public SendNode(ExprNode statement, String selector, List<ExprNode> arguments) {
        this.statement = statement;
        this.selector = selector;
        this.arguments = arguments;
    }
    
    public ExprNode getStatement() {
        return statement;
    }
    
    public String getSelector() {
        return selector;
    }
    
    public List<ExprNode> getArguments() {
        return arguments;
    }
    
    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<Node>();
        children.add(statement);
        children.addAll(arguments);
        
        return children;
    }
    
    @Override
    public String toString() {
        return "Send "+selector;
    }
}
