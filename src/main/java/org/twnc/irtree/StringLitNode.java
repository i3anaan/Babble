package org.twnc.irtree;

public class StringLitNode extends ExprNode{

    private String string;
    
    public StringLitNode(String string) {
        this.string = string;
    }
    
    public String getString() {
        return string;
    }
}
