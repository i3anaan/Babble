package org.twnc.irtree;

import org.twnc.irtree.nodes.*;

/** An implementation of ASvoidVisitor that just crawls through the tree. */
public class BaseASTVisitor extends ASTVisitor {
    @Override
    public void visit(ProgramNode programNode) {
        programNode.getClasses().forEach(x -> x.accept(this));
    }
    
    @Override
    public void visit(ClazzNode clazzNode) {
        clazzNode.getMethods().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(MethodNode methodNode) {
        methodNode.getSequence().accept(this);
    }

    @Override
    public void visit(SequenceNode sequenceNode) {
        sequenceNode.getExpressions().forEach(x -> x.accept(this));
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
    public void visit(VarRefNode varRefNode) {
    }

    @Override
    public void visit(LiteralNode literalNode) {
    }
}
