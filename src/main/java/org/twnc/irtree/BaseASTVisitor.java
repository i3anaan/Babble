package org.twnc.irtree;

import org.twnc.irtree.nodes.AssignNode;
import org.twnc.irtree.nodes.BlockNode;
import org.twnc.irtree.nodes.IntLitNode;
import org.twnc.irtree.nodes.MethodNode;
import org.twnc.irtree.nodes.ProgramNode;
import org.twnc.irtree.nodes.SendNode;
import org.twnc.irtree.nodes.SequenceNode;
import org.twnc.irtree.nodes.StringLitNode;
import org.twnc.irtree.nodes.SymbolNode;
import org.twnc.irtree.nodes.VarRefNode;

/** An implementation of ASTVisitor that just crawls through the tree. */
public class BaseASTVisitor<T> extends ASTVisitor<T> {
    @Override
    public T visit(AssignNode assignNode) {
        assignNode.getVariable().accept(this);
        assignNode.getExpression().accept(this);
        return null;
    }

    @Override
    public T visit(BlockNode blockNode) {
        blockNode.getSequence().accept(this);
        return null;
    }

    @Override
    public T visit(MethodNode methodNode) {
        methodNode.getObjectName().ifPresent(x -> x.accept(this));
        methodNode.getArguments().forEach(x -> x.accept(this));
        methodNode.getSequence().accept(this);
        return null;
    }

    @Override
    public T visit(ProgramNode programNode) {
        programNode.getMethods().forEach(x -> x.accept(this));
        return null;
    }

    @Override
    public T visit(SendNode sendNode) {
        sendNode.getExpression().ifPresent(x-> x.accept(this));
        sendNode.getArguments().forEach(x -> x.accept(this));
        return null;
    }

    @Override
    public T visit(SequenceNode sequenceNode) {
        sequenceNode.getExpressions().forEach(x -> x.accept(this));
        return null;
    }

    @Override
    public T visit(IntLitNode intLitNode) {
        return null;
    }


    @Override
    public T visit(StringLitNode stringLitNode) {
        return null;
    }

    @Override
    public T visit(SymbolNode symbolNode) {
        return null;
    }

    @Override
    public T visit(VarRefNode varRefNode) {
        return null;
    }
}
