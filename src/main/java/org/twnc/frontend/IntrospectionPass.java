package org.twnc.frontend;

import org.twnc.irtree.BaseASTVisitor;
import org.twnc.irtree.nodes.ArrayNode;
import org.twnc.irtree.nodes.ClazzNode;
import org.twnc.irtree.nodes.DeclsNode;
import org.twnc.irtree.nodes.ExprNode;
import org.twnc.irtree.nodes.LiteralNode;
import org.twnc.irtree.nodes.MethodNode;
import org.twnc.irtree.nodes.ProgramNode;
import org.twnc.irtree.nodes.SequenceNode;

import java.util.ArrayList;
import java.util.List;

public class IntrospectionPass extends BaseASTVisitor {
    ProgramNode program;
    List<ClazzNode> clazzes;

    @Override
    public void visit(ProgramNode programNode) {
        program = programNode;
        clazzes = new ArrayList<>();

        super.visit(programNode);

        programNode.getClasses().addAll(clazzes);
    }

    @Override
    public void visit(ClazzNode clazz) {
        super.visit(clazz);

        // Collect method signatures
        ArrayList<ExprNode> selectors = new ArrayList<>();
        for (MethodNode method : clazz.getMethods()) {
            selectors.add(new LiteralNode(LiteralNode.Type.STRING, method.getSelector()));
        }

        // Generate name, methods and class methods on the metaclass
        ArrayList<MethodNode> methods = new ArrayList<>();
        methods.add(new MethodNode("name", new SequenceNode(new LiteralNode(LiteralNode.Type.STRING, clazz.getName()))));
        methods.add(new MethodNode("methods", new SequenceNode(new ArrayNode(selectors))));
        methods.add(new MethodNode("class", new SequenceNode(new LiteralNode(LiteralNode.Type.CLASS, "org/twnc/runtime/BClass"))));

        // Add the metaclass to the program
        String metaclassName = clazz.getName() + "Class";
        clazzes.add(new ClazzNode(metaclassName, "org/twnc/runtime/BClass", new DeclsNode(), methods));

        // Add a 'class' method to the original class
        clazz.getMethods().add(new MethodNode("class", new SequenceNode(new LiteralNode(LiteralNode.Type.CLASS, metaclassName))));
    }
}
