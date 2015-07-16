package org.twnc.frontend;

import org.twnc.irtree.ASTBaseVisitor;
import org.twnc.irtree.nodes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Intermediate compilation step that generates metaclasses for every Babble
 * class.
 *
 * Metaclasses allow Babble code some measure of introspection. They are the
 * classes of which you get an instance when you call the 'class' method of
 * an object.
 *
 * Also adds the names of the classes to the globals table: 'Duck' will get
 * you an instance of the generated DuckClass metaclass, allowing you to do
 * e.g. 'Duck new'.
 */
public class MetaclassGenerator extends ASTBaseVisitor {
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
        methods.add(new MethodNode("class", new SequenceNode(new LiteralNode(LiteralNode.Type.CLASS, "Class"))));

        // Add the metaclass to the program
        String className = clazz.getName();
        String metaclassName = className + "Class";
        metaclasses.add(new ClazzNode(metaclassName, "Class", new DeclsNode(), methods));
        globals.put(className, metaclassName);

        // Add a 'class' method to the original class
        clazz.getMethods().add(new MethodNode("class", new SequenceNode(new LiteralNode(LiteralNode.Type.CLASS, metaclassName))));
    }
}
