package org.twnc.irtree;

import org.twnc.irtree.nodes.*;

public abstract class ASTVisitor<T> {
    public abstract T visit(ProgramNode programNode);
    public abstract T visit(ClazzNode clazzNode);
    public abstract T visit(MethodNode methodNode);
    public abstract T visit(SequenceNode sequenceNode);
    public abstract T visit(BlockNode blockNode);
    public abstract T visit(SendNode sendNode);
    public abstract T visit(AssignNode assignNode);
    public abstract T visit(IntLitNode intLitNode);
    public abstract T visit(StringLitNode stringLitNode);
    public abstract T visit(SymbolNode symbolNode);
    public abstract T visit(VarRefNode varRefNode);
}
