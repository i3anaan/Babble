package org.twnc.irtree;

import java.util.ArrayList;
import java.util.List;

public class StringLitNode extends ExprNode {
    private String string;
    
    public StringLitNode(String string) {
        this.string = string;
    }
    
    public String getString() {
        return string;
    }
    
    @Override
    public String toString() {
        return string;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
