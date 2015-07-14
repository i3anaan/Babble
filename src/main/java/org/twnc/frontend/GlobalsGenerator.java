package org.twnc.frontend;

import org.twnc.compile.exceptions.CompileException;
import org.twnc.irtree.BaseASTVisitor;
import org.twnc.irtree.nodes.ProgramNode;

import java.util.Map;

public class GlobalsGenerator extends BaseASTVisitor {
    @Override
    public void visit(ProgramNode programNode) throws CompileException {
        Map<String, String> globals = programNode.getGlobals();
        globals.put("true", "True");
        globals.put("false", "False");
        globals.put("nil", "Nil");
        super.visit(programNode);
    }
}
