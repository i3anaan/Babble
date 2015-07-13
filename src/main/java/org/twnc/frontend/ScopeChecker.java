package org.twnc.frontend;

import org.twnc.ScopeStack;
import org.twnc.compile.exceptions.VariableNotDeclaredException;
import org.twnc.irtree.BaseASTVisitor;
import org.twnc.irtree.nodes.*;

/**
 * Checks and sets scopes in the AST.
 */
public class ScopeChecker extends BaseASTVisitor {
    private ScopeStack scopeStack;
    
    @Override
    public void visit(ProgramNode programNode) {
        scopeStack = new ScopeStack(programNode);
        programNode.setScope(scopeStack.peek());
        super.visit(programNode);
    }

    @Override
    public void visit(ClazzNode clazzNode) {
        scopeStack.enterScope(clazzNode);
        clazzNode.setScope(scopeStack.peek());
        super.visit(clazzNode);
        scopeStack.exitScope();
    }

    @Override
    public void visit(MethodNode methodNode) {
        scopeStack.enterScope(methodNode);
        methodNode.setScope(scopeStack.peek());
        super.visit(methodNode);
        scopeStack.exitScope();
    }

    @Override
    public void visit(BlockNode blockNode) {
        scopeStack.enterScope(blockNode);
        blockNode.setScope(scopeStack.peek());
        super.visit(blockNode);
        scopeStack.exitScope();
    }

    @Override
    public void visit(VarRefNode varRefNode) {
        String name = varRefNode.getName();
        if (!ScopeStack.isSpecial(name) && !scopeStack.contains(name)) {
            visitError(varRefNode, String.format("Variable %s is not declared.", name));
        }
        super.visit(varRefNode);
    }

    @Override
    public void visit(VarDeclNode node) {
        if (!scopeStack.putVarDeclNode(node)) {
            VarDeclNode previousDecl;
            try {
                previousDecl = scopeStack.getVarDeclNode(node.getName());
            } catch (VariableNotDeclaredException e) {
                throw new RuntimeException(e);
            }
            visitError(node, String.format("Variable %s is already declared at %s:%s.", node.getName(), previousDecl.getLine(), previousDecl.getLineOffset()));
        }
        super.visit(node);
    } 
}