package org.twnc.irtree.nodes;

import java.util.ArrayList;
import java.util.List;

import org.twnc.irtree.ASTVisitor;

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
