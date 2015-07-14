package org.twnc.irtree;

import java.util.ArrayList;
import java.util.List;

import org.twnc.compile.exceptions.CompileException;
import org.twnc.compile.exceptions.ScopeException;
import org.twnc.irtree.nodes.*;

/** An implementation of ASTVisitor that just crawls through the tree. */
public class BaseASTVisitor extends ASTVisitor {
    private List<String> errors;

    public BaseASTVisitor() {
        errors = new ArrayList<String>();
    }

    @Override
    public void visit(ProgramNode programNode) throws CompileException {
        programNode.getClasses().forEach(x -> x.accept(this));

        if (!errors.isEmpty()) {
            for (String error : errors) {
                System.err.println(error);
            }
            throw new CompileException();
        }
    }
    
    @Override
    public void visit(ClazzNode clazzNode) {
        clazzNode.getDecls().accept(this);
        clazzNode.getMethods().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(MethodNode methodNode) {
        methodNode.getArguments().forEach(x -> x.accept(this));
        methodNode.getSequence().accept(this);
    }

    @Override
    public void visit(SequenceNode sequenceNode) {
        sequenceNode.getExpressions().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(ArrayNode arrayNode) {
        arrayNode.getExpressions().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(BlockNode blockNode) {
        blockNode.getSequence().accept(this);
    }

    @Override
    public void visit(SendNode sendNode) {
        sendNode.getExpression().ifPresent(x-> x.accept(this));
        sendNode.getArguments().forEach(x -> x.accept(this));
    }
    
    @Override
    public void visit(AssignNode assignNode) {
        assignNode.getVariable().accept(this);
        assignNode.getExpression().accept(this);
    }

    @Override
    public void visit(VarDeclNode varDeclNode) {
    }

    @Override
    public void visit(DeclsNode declsNode) {
        declsNode.getDeclarations().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(VarRefNode varRefNode) {
    }

    @Override
    public void visit(LiteralNode literalNode) {
    }

    public void visitError(Node node, String message) {
        errors.add(String.format("%d:%d - %s", node.getLine(), node.getLineOffset(), message));
    }
    
    public List<String> getErrors() {
        return errors;
    }
}
