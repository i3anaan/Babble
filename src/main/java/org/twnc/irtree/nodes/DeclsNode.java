package org.twnc.irtree.nodes;

import java.util.ArrayList;
import java.util.List;

import org.twnc.irtree.ASTVisitor;

public class DeclsNode extends ExprNode {
    private final List<VarDeclNode> declarations;

    public DeclsNode() {
        this.declarations = new ArrayList<>();
    }
    
    public DeclsNode(List<VarDeclNode> declarations) {
        this.declarations = declarations;
    }

    public List<VarDeclNode> getDeclarations() {
        return declarations;
    }

    @Override
    public String toString() {
        return "Sequence";
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

}
