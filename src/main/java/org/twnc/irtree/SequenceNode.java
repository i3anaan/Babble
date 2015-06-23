package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequenceNode extends Node{

    private List<ExprNode> statements;
    
    public SequenceNode(List<ExprNode> statements) {
        this.statements = statements;
    }
    
    public List<ExprNode> getStatements() {
        return statements;
    }
        
    @Override
    public List<Node> getChildren() {
        return new ArrayList<Node>(statements);
    }
    
    @Override
    public String toString() {
        return "Sequence";
    }
}
