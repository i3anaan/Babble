package org.twnc;

import org.antlr.v4.runtime.*;
import org.twnc.backend.BytecodeGenerator;
import org.twnc.compile.exceptions.CompileException;
import org.twnc.frontend.ScopeChecker;
import org.twnc.irtree.ASTGenerator;
import org.twnc.irtree.ASTVisitor;
import org.twnc.irtree.nodes.Node;
import org.twnc.util.Graphvizitor;

import java.io.*;

public final class App {
    public static void main(String[] args) throws IOException {
        String inPath = args[0];
        String outDir = args.length > 1 ? args[1] : "target/classes/";

        File file = new File(inPath);

        CharStream chars = new ANTLRInputStream(new FileInputStream(file));
        Lexer lexer = new BabbleLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        BabbleParser parser = new BabbleParser(tokens);

        ASTGenerator generator = new ASTGenerator();
        Node irtree = generator.visitProgram(parser.program());
        
        ASTVisitor<Void> graphVisitor = new Graphvizitor(outDir);
        irtree.accept(graphVisitor);
        try {
            ScopeChecker scopeVisitor = new ScopeChecker();
            irtree.accept(scopeVisitor);
    
            ASTVisitor<Void> bytecodeVisitor = new BytecodeGenerator(outDir);
            irtree.accept(bytecodeVisitor);
    
            System.out.println(String.format("[ OK ] Compiled %s", file));
        } catch (CompileException e) {
            e.printStackTrace();
        }
    }
}
