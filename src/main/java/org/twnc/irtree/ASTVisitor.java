package org.twnc.irtree;

import org.twnc.irtree.nodes.*;

/**
 * Visitor that can traverse our AST.
 * 
 * Makes use of the visitor pattern.
 * (https://en.wikipedia.org/wiki/Visitor_pattern)
 *
 */
public abstract class ASTVisitor {
    public abstract void visit(AssignNode assignNode);
    public abstract void visit(BlockNode blockNode);
    public abstract void visit(MethodNode methodNode);
    public abstract void visit(ProgramNode programNode);
    public abstract void visit(ClazzNode clazzNode);
    public abstract void visit(SendNode sendNode);
    public abstract void visit(SequenceNode sequenceNode);
    public abstract void visit(VarRefNode varRefNode);
    public abstract void visit(LiteralNode literalNode);
    public abstract void visit(VarDeclNode varDeclNode);
    public abstract void visit(DeclsNode declExprNode);
    public abstract void visit(ArrayNode arrayNode);
}
