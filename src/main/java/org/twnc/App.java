package org.twnc;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class App extends BabbleBaseListener implements Opcodes {

    private ClassWriter cw;
    private MethodVisitor mv;
    private Label top;
    private Label bottom;

    public static void main(String[] args) throws Exception {
        String program = "" +
                "env true print." +
                "env true not not print." +
                "10 print." +
                "10 negate print." +
                "10 + 10 print." +
                "20 / 10 print." +
        "";

        CharStream chars = new ANTLRInputStream(program);
        Lexer lexer = new BabbleLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        BabbleParser parser = new BabbleParser(tokens);

        ParseTreeWalker walker = new ParseTreeWalker();
        ParseTree tree = parser.program();
        walker.walk(new App(), tree);

        System.out.println("[ OK ]");
    }
}
