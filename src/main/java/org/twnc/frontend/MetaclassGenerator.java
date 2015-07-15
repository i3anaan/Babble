package org.twnc.frontend;

import org.twnc.irtree.BaseASTVisitor;
import org.twnc.irtree.nodes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MetaclassGenerator extends BaseASTVisitor {
    private List<ClazzNode> metaclasses;
    private Map<String, String> globals;

    @Override
    public void visit(ProgramNode programNode) {
        metaclasses = new ArrayList<>();
        globals = new HashMap<>();

        super.visit(programNode);

        programNode.getClasses().addAll(metaclasses);
        programNode.getGlobals().putAll(globals);
    }

    @Override
    public void visit(ClazzNode clazz) {
        super.visit(clazz);

        // Collect method signatures
        ArrayList<ExprNode> selectors = new ArrayList<>();
        for (MethodNode method : clazz.getMethods()) {
            selectors.add(new LiteralNode(LiteralNode.Type.STRING, method.getSelector()));
        }
        // Generate new, name, methods and class methods on the metaclass
        Set<MethodNode> methods = new HashSet<>();
        methods.add(new MethodNode("new", new SequenceNode(new LiteralNode(LiteralNode.Type.CLASS, clazz.getName()))));
        methods.add(new MethodNode("name", new SequenceNode(new LiteralNode(LiteralNode.Type.STRING, clazz.getName()))));
        methods.add(new MethodNode("methods", new SequenceNode(new ArrayNode(selectors))));
        methods.add(new MethodNode("class", new SequenceNode(new LiteralNode(LiteralNode.Type.CLASS, "org/twnc/runtime/BClass"))));

        // Add the metaclass to the program
        String className = clazz.getName();
        String metaclassName = className + "Class";
        metaclasses.add(new ClazzNode(metaclassName, "org/twnc/runtime/BClass", new DeclsNode(), methods));
        globals.put(className, metaclassName);

        // Add a 'class' method to the original class
        clazz.getMethods().add(new MethodNode("class", new SequenceNode(new LiteralNode(LiteralNode.Type.CLASS, metaclassName))));
    }
}
