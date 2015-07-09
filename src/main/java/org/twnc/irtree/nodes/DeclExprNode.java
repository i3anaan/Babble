package org.twnc.irtree.nodes;

import java.util.List;

import org.twnc.irtree.ASTVisitor;

public class DeclExprNode extends ExprNode {
    private final List<VarDeclNode> declarations;

    public DeclExprNode(List<VarDeclNode> declarations) {
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
