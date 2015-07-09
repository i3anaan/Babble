package org.twnc.frontend;

import org.twnc.ScopeStack;
import org.twnc.compile.exceptions.VariableNotDeclaredException;
import org.twnc.irtree.ASTBaseVisitor;
import org.twnc.irtree.nodes.*;

/**
 * Checks and sets scopes in the AST.
 */
public class ScopeChecker extends ASTBaseVisitor<Void> {
    private ScopeStack scopeStack;
    
    @Override
    public Void visit(ProgramNode programNode) {
        scopeStack = new ScopeStack();
        programNode.setScope(scopeStack.peek());
        return super.visit(programNode);
    }

    @Override
    public Void visit(ClazzNode clazzNode) {
        scopeStack.enterScope();
        clazzNode.setScope(scopeStack.peek());
        super.visit(clazzNode);
        scopeStack.exitScope();
        
        return null;
    }

    @Override
    public Void visit(MethodNode methodNode) {
        scopeStack.enterScope();
        methodNode.setScope(scopeStack.peek());
        super.visit(methodNode);
        scopeStack.exitScope();
        
        return null;
    }

    @Override
    public Void visit(BlockNode blockNode) {
        scopeStack.enterScope();
        blockNode.setScope(scopeStack.peek());
        super.visit(blockNode);
        scopeStack.exitScope();
        
        return null;
    }

    @Override
    public Void visit(VarRefNode varRefNode) {
        if (!scopeStack.contains(varRefNode.getName())) {
            visitError(varRefNode, String.format("Variable %s is not declared.", varRefNode.getName()));
        }
        return super.visit(varRefNode);
    }

    @Override
    public Void visit(VarDeclNode node) {
        if (!scopeStack.putVarDeclNode(node)) {
            VarDeclNode previousDecl;
            try {
                previousDecl = scopeStack.getVarDeclNode(node.getName());
            } catch (VariableNotDeclaredException e) {
                throw new RuntimeException(e);
            }
            visitError(node, String.format("Variable %s is already declared at %s:%s.", node.getName(), previousDecl.getLine(), previousDecl.getLineOffset()));
        }
        return super.visit(node);
    } 
}
