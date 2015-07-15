package org.twnc.irtree.nodes;

import java.util.HashSet;
import java.util.Set;

import org.twnc.irtree.ASTVisitor;

public class DeclsNode extends ExprNode {
    private final Set<VarDeclNode> declarations;

    public DeclsNode() {
        this.declarations = new HashSet<>();
    }
    
    public DeclsNode(Set<VarDeclNode> declarations) {
        this.declarations = declarations;
    }

    public Set<VarDeclNode> getDeclarations() {
        return declarations;
    }
    
    public VarDeclNode getDeclaration(String name) {
        for (VarDeclNode decl : declarations) {
            if (decl.getName().equals(name)) {
                return decl;
            }
        }
        return null;
    }
    
    public boolean addDeclaration(VarDeclNode decl) {
        return declarations.add(decl);
    }

    public boolean addDeclarations(Set<VarDeclNode> decls) {
        return declarations.addAll(decls);
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
