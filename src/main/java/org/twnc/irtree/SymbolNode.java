package org.twnc.irtree;

public class SymbolNode extends ExprNode{

    private String id;
    
    public SymbolNode(String id) {
        this.id = id;
    }
    
    public String getID() {
        return id;
    }
}
