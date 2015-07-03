package org.twnc.backend;

import javax.management.RuntimeErrorException;

import org.twnc.ScopeStack;
import org.twnc.compile.exceptions.VariableNotDeclaredException;
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
        methodNode.setScope(scopeStack.peek());
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
