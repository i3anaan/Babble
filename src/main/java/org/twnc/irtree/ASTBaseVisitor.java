package org.twnc.irtree;

import java.util.ArrayList;
import java.util.List;

import org.twnc.compile.exceptions.CompileException;
import org.twnc.compile.exceptions.ScopeException;
import org.twnc.irtree.nodes.*;

/**
 * A basic implementation of ASTVisitor that just crawls through the tree,
 * intended for other visitors to extend.
 * 
 * Each visit method calls the accept() method on the Node's children, in
 * accordance with the visitor pattern.
 */
public class ASTBaseVisitor extends ASTVisitor {
    /** A list of errors found while traversing the tree. */
    private List<String> errors;

    public ASTBaseVisitor() {
        errors = new ArrayList<String>();
    }

    @Override
    public void visit(ProgramNode programNode) throws CompileException {
        programNode.getClasses().forEach(x -> x.accept(this));
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
        sendNode.getExpression().accept(this);
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
        declsNode.getDeclarations().forEach(x -> x .accept(this));
    }

    @Override
    public void visit(VarRefNode varRefNode) {
    }

    @Override
    public void visit(LiteralNode literalNode) {
    }

    /**
     * Method to store error messages for errors that occured during tree traversal.
     * @param node  The Node for which the error occured.
     * @param message A message that can be used to display the error.
     */
    public void visitError(Node node, String message) {
        errors.add(String.format("[%s] - %s", String.valueOf(node.getLocation()), message));
    }
    
    public List<String> getErrors() {
        return errors;
    }
}
