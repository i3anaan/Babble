package org.twnc.irtree;

import java.util.ArrayList;
import java.util.List;

public class SymbolNode extends ExprNode{

    private String id;
    
    public SymbolNode(String id) {
        this.id = id;
    }
    
    public String getID() {
        return id;
    }
    
    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<Node>();
        return children;
    }
    
    @Override
    public String toString() {
        return "#"+id;
    }
}
