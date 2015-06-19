package org.twnc.irtree;

import java.util.ArrayList;
import java.util.List;

public class StringLitNode extends ExprNode{

    private String string;
    
    public StringLitNode(String string) {
        this.string = string;
    }
    
    public String getString() {
        return string;
    }
    
    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<Node>();
        return children;
    }
}
