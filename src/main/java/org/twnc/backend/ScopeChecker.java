package org.twnc.backend;

import org.twnc.ScopeStack;
import org.twnc.irtree.ASTBaseVisitor;
import org.twnc.irtree.nodes.AssignNode;
import org.twnc.irtree.nodes.BlockNode;
import org.twnc.irtree.nodes.ClazzNode;
import org.twnc.irtree.nodes.MethodNode;
import org.twnc.irtree.nodes.ProgramNode;
import org.twnc.irtree.nodes.VarDeclNode;
import org.twnc.irtree.nodes.VarRefNode;

public class ScopeChecker extends ASTBaseVisitor<Void> {
    private ScopeStack scopeStack;
    
    @Override
    public Void visit(ProgramNode programNode) {
        scopeStack = new ScopeStack();
        return super.visit(programNode);
    }

    @Override
    public Void visit(ClazzNode clazzNode) {
        scopeStack.enterScope();
        super.visit(clazzNode);
        scopeStack.exitScope();
        
        return null;
    }

    @Override
    public Void visit(MethodNode methodNode) {
        scopeStack.enterScope();
        super.visit(methodNode);
        scopeStack.exitScope();
        
        return null;
    }

    @Override
    public Void visit(BlockNode blockNode) {
        scopeStack.enterScope();
        super.visit(blockNode);
        scopeStack.exitScope();
        
        return null;
    }

    @Override
    public Void visit(VarRefNode varRefNode) {
        if (!scopeStack.contains(varRefNode)) {
            visitError(varRefNode, "Variable not declared.");
        }
        return super.visit(varRefNode);
    }

    @Override
    public Void visit(VarDeclNode varDeclNode) {
        for (VarRefNode node : varDeclNode.getDecls()) {
            if (!scopeStack.putVarRefNode(node)) {
                visitError(node, "Variable already declared.");
            }
        }
        return super.visit(varDeclNode);
    } 
}
