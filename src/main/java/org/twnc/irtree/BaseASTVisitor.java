package org.twnc.irtree;

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
        sendNode.getStatement().accept(this);
        sendNode.getArguments().forEach(x -> x.accept(this));
        return null;
    }

    @Override
    public T visit(SequenceNode sequenceNode) {
        sequenceNode.getStatements().forEach(x -> x.accept(this));
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
