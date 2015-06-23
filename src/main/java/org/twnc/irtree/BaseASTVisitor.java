package org.twnc.irtree;

/** An implementation of ASTVisitor that does nothing. */
public class BaseASTVisitor<T> extends ASTVisitor<T> {
    @Override
    public T visit(AssignNode assignNode) {
        return null;
    }

    @Override
    public T visit(BlockNode blockNode) {
        return null;
    }

    @Override
    public T visit(IntLitNode intLitNode) {
        return null;
    }

    @Override
    public T visit(MethodNode methodNode) {
        return null;
    }

    @Override
    public T visit(ProgramNode programNode) {
        return null;
    }

    @Override
    public T visit(SendNode sendNode) {
        return null;
    }

    @Override
    public T visit(SequenceNode sequenceNode) {
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
