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

public abstract class ASTVisitor<T> {
    public abstract T visit(AssignNode assignNode);
    public abstract T visit(BlockNode blockNode);
    public abstract T visit(IntLitNode intLitNode);
    public abstract T visit(MethodNode methodNode);
    public abstract T visit(ProgramNode programNode);
    public abstract T visit(SendNode sendNode);
    public abstract T visit(SequenceNode sequenceNode);
    public abstract T visit(StringLitNode stringLitNode);
    public abstract T visit(SymbolNode symbolNode);
    public abstract T visit(VarRefNode varRefNode);
}
