package org.twnc.irtree.nodes;

import java.util.HashSet;
import java.util.Set;

import org.twnc.irtree.ASTVisitor;

/**
 * A Node that represents a set of variable declarations in Babble code
 * (ie: '| x y |.').
 *
 */
public class DeclsNode extends ExprNode {
    /**
     * The actual variables declared. Since this is a set, duplicate
     * declarations are automatically disallowed.
     * */
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
    
    /**
     * Looks for a declaration in this set of declaration with a given name.
     * @param name The declaration to look for
     * @return The declaration with the requested name, or null if none found.
     */
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
