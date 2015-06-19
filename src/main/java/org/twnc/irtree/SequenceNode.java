package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequenceNode extends Node{

    private List<StatNode> statements;
    
    public SequenceNode(List<StatNode> statements) {
        this.statements = statements;
    }
    
    public List<StatNode> getStatements() {
        return statements;
    }
    
    public StatNode getStatement(int index) {
        return statements.get(index);
    }
    

    
    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<Node>();
        children.addAll(statements);
        
        return children;
    }
    
    @Override
    public String toString() {
        return "Sequence";
    }
}
