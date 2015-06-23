package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SendNode extends ExprNode {
    private Optional<ExprNode> expression;
    private String selector;
    private List<ExprNode> arguments;
    
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
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<Node>();
        if(expression.isPresent()) {
            children.add(expression.get());
        }
        children.addAll(arguments);
        
        return children;
    }
    
    @Override
    public String toString() {
        return "Send "+selector;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        arguments.forEach(x -> x.accept(visitor));
        visitor.visit(this);
    }
}
