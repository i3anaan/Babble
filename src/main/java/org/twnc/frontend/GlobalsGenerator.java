package org.twnc.frontend;

import org.twnc.compile.exceptions.CompileException;
import org.twnc.irtree.ASTBaseVisitor;
import org.twnc.irtree.nodes.ProgramNode;

import java.util.Map;

/**
 * Intermediate compilation step that adds a handful of useful globals to the
 * program node in the intermediate representation.
 */
public class GlobalsGenerator extends ASTBaseVisitor {
    @Override
    public void visit(ProgramNode programNode) throws CompileException {
        Map<String, String> globals = programNode.getGlobals();

        // true, false and nil work as expected.
        globals.put("true", "True");
        globals.put("false", "False");
        globals.put("nil", "Nil");

        // Special global for `System failure`, `System print` and `System read`.
        globals.put("System", "org/twnc/runtime/Core");

        super.visit(programNode);
    }
}
