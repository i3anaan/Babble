package org.twnc.irtree;

import java.util.Arrays;
import java.util.List;

public class SequenceNode {

    private List<StatNode> statements;
    
    public SequenceNode(StatNode... statements) {
        this.statements = Arrays.asList(statements);
    }
    
    public List<StatNode> getStatements() {
        return statements;
    }
    
    public StatNode getStatement(int index) {
        return statements.get(index);
    }
}
