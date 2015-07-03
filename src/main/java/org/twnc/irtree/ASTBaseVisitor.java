package org.twnc.irtree;

import org.twnc.irtree.nodes.*;

/** An implementation of ASTVisitor that just crawls through the tree. */
public class ASTBaseVisitor<T> extends ASTVisitor<T> {
    @Override
    public T visit(ProgramNode programNode) {
        programNode.getClasses().forEach(x -> x.accept(this));
        return null;
    }
    
    @Override
    public T visit(ClazzNode clazzNode) {
        clazzNode.getMethods().forEach(x -> x.accept(this));
        return null;
    }

    @Override
    public T visit(MethodNode methodNode) {
        methodNode.getSequence().accept(this);
        return null;
    }

    @Override
    public T visit(SequenceNode sequenceNode) {
        sequenceNode.getExpressions().forEach(x -> x.accept(this));
        return null;
    }

    @Override
    public T visit(BlockNode blockNode) {
        blockNode.getSequence().accept(this);
        return null;
    }

    @Override
    public T visit(SendNode sendNode) {
        sendNode.getExpression().ifPresent(x-> x.accept(this));
        sendNode.getArguments().forEach(x -> x.accept(this));
        return null;
    }
    
    @Override
    public T visit(AssignNode assignNode) {
        assignNode.getVariable().accept(this);
        assignNode.getExpression().accept(this);
        return null;
    }

    @Override
    public T visit(VarDeclNode varDeclNode) {
        return null;
    }

    @Override
    public T visit(DeclExprNode declExprNode) {
        declExprNode.getDeclarations().forEach(x -> x.accept(this));
        return null;
    }

    @Override
    public T visit(VarRefNode varRefNode) {
        return null;
    }

    @Override
    public T visit(LiteralNode literalNode) {
        return null;
    }
    
    public void visitError(Node node, String message) {
        System.err.println(String.format("%d:%d - %s", node.getLine(), node.getLineOffset(), message));
    }
}
