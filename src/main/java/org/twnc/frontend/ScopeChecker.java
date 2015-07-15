package org.twnc.frontend;

import org.twnc.Scope;
import org.twnc.ScopeStack;
import org.twnc.compile.exceptions.CompileException;
import org.twnc.compile.exceptions.ScopeException;
import org.twnc.compile.exceptions.VariableNotDeclaredException;
import org.twnc.irtree.ASTBaseVisitor;
import org.twnc.irtree.nodes.*;

/**
 * Checks and sets scopes in the AST.
 */
public class ScopeChecker extends ASTBaseVisitor {
    private ProgramNode program;
    private ScopeStack scopeStack;
    
    @Override
    public void visit(ProgramNode programNode) {
        program = programNode;
        scopeStack = new ScopeStack(programNode);
        programNode.setScope(scopeStack.peek());
        
        super.visit(programNode);
        
        if (!getErrors().isEmpty()) {
            for (String error : getErrors()) {
                System.err.println(error);
            }
            throw new ScopeException();
        }
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
        Scope methodScope = scopeStack.enterScope(methodNode);
        methodNode.setScope(methodScope);
        scopeStack.putVarDeclNode(new VarDeclNode("this"));
        super.visit(methodNode);
        scopeStack.exitScope();
    }

    @Override
    public void visit(BlockNode blockNode) {
        Scope blockScope = scopeStack.enterScope(blockNode);
        blockNode.setScope(blockScope);

        for (VarDeclNode decl : blockScope.flatten()) {
            String name = decl.getName();
            VarDeclNode newNode = new VarDeclNode(name);

            newNode.setScope(blockScope);
            blockScope.put(name, newNode);
        }

        super.visit(blockNode);
        scopeStack.exitScope();
    }

    @Override
    public void visit(VarRefNode varRefNode) {
        String name = varRefNode.getName();
        if (!program.getGlobals().containsKey(name) && !scopeStack.contains(name)) {

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
            visitError(node, String.format("Variable %s is already declared at [%s].", node.getName(), String.valueOf(previousDecl.getLocation())));
        }
        super.visit(node);
    } 
}
