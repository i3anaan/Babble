package org.twnc;

import org.antlr.v4.runtime.*;
import org.twnc.backend.BytecodeGenerator;
import org.twnc.irtree.ASTGenerator;
import org.twnc.irtree.ASTVisitor;
import org.twnc.irtree.nodes.Node;
import org.twnc.util.Graphvizivier;

import java.io.*;

public final class App {
    public static void main(String[] args) throws IOException {
        for (String path : args) {
            File file = new File(path);

            CharStream chars = new ANTLRInputStream(new FileInputStream(file));
            Lexer lexer = new BabbleLexer(chars);
            TokenStream tokens = new CommonTokenStream(lexer);
            BabbleParser parser = new BabbleParser(tokens);

            ASTGenerator generator = new ASTGenerator();
            Node irtree = generator.visitProgram(parser.program());

            String dotFile = "target/classes/" + file.getName().replace(".bla", ".dot");
            try (PrintWriter out = new PrintWriter(dotFile)) {
                out.print(new Graphvizivier(irtree).toGraph());
                out.close();
            }

            ASTVisitor<Void> visitor = new BytecodeGenerator();
            irtree.accept(visitor);

            System.out.println(String.format("[ OK ] Compiled %s", file));
        }
    }
}
