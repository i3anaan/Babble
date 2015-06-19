package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendNode extends StatNode{

    private StatNode statement;
    private String selector; //TODO better name?
    private List<ExprNode> arguments;
    
    public SendNode(StatNode statement, String selector) {
        this.statement = statement;
        this.selector = selector;
        this.arguments = new ArrayList<>();
    }
    
    public SendNode(StatNode statement, String selector, ExprNode... arguments) {
        this.statement = statement;
        this.selector = selector;
        this.arguments = Arrays.asList(arguments);
    }
    
    public StatNode getStatement() {
        return statement;
    }
    
    public String getSelector() {
        return selector;
    }
    
    public List<ExprNode> getArguments() {
        return arguments;
    }
    
    public ExprNode getArgument(int index) {
        return arguments.get(index);
    }
}
