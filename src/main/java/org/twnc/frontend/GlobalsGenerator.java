package org.twnc.frontend;

import org.twnc.compile.exceptions.CompileException;
import org.twnc.irtree.ASTBaseVisitor;
import org.twnc.irtree.nodes.ProgramNode;

import java.util.Map;

public class GlobalsGenerator extends ASTBaseVisitor {
    @Override
    public void visit(ProgramNode programNode) throws CompileException {
        Map<String, String> globals = programNode.getGlobals();
        globals.put("true", "True");
        globals.put("false", "False");
        globals.put("nil", "Nil");
        globals.put("System", "org/twnc/runtime/Core");
        super.visit(programNode);
    }
}
