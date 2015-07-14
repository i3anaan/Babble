package org.twnc;

import org.antlr.v4.runtime.*;
import org.apache.commons.cli.*;
import org.twnc.backend.BytecodeGenerator;
import org.twnc.frontend.IntrospectionPass;
import org.twnc.frontend.ScopeChecker;
import org.twnc.irtree.ASTGenerator;
import org.twnc.irtree.ASTVisitor;
import org.twnc.irtree.nodes.Node;
import org.twnc.irtree.nodes.ProgramNode;
import org.twnc.util.Graphvizitor;

import java.io.*;

public final class App {
    private static boolean verbose = false;
    public static void main(String[] args) throws IOException {
        Options options = new Options();

        options.addOption(Option.builder("d")
            .longOpt("dest")
            .desc("Set destination directory for .class files")
            .hasArg()
            .argName("dir")
            .build());

        options.addOption(Option.builder("v")
            .longOpt("verbose")
            .desc("Make the compiler more noisy")
            .build()
        );

        options.addOption(Option.builder("h")
            .longOpt("help")
            .desc("Show this help information.")
            .build()
        );

        CommandLineParser cliparser = new DefaultParser();
        CommandLine line;

        try {
            line = cliparser.parse(options, args);
        } catch (ParseException exp) {
            System.err.println(exp.getMessage());
            printHelp(options);
            return;
        }

        if (line.hasOption('h')) {
            printHelp(options);
            return;
        }

        String outDir = line.getOptionValue('d', "target" + File.separator + "classes");
        if (!outDir.endsWith(File.separator)) {
            outDir += File.separator;
        }

        verbose = line.hasOption('v');

        for (String inPath : line.getArgs()) {
            File file = new File(inPath);
            compileFile(file, outDir);

            if (verbose) {
                System.out.println(String.format("[ OK ] Compiled %s", file));
            }
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("app", options);
    }

    private static Node compileFile(File file, String outDir) throws IOException {
        //TODO Better Prelude.bla loading.
        Node preludeTree = generateIRTree(App.class.getResourceAsStream("Prelude.bla"), "Babble\\Prelude.bla");
        Node programTree = generateIRTree(new FileInputStream(file), file.getPath());
        Node combinedTree = ((ProgramNode) preludeTree).addTree((ProgramNode) programTree);
        
        new File(outDir).mkdirs();

        ASTVisitor graphVisitor = new Graphvizitor(outDir);
        combinedTree.accept(graphVisitor);

        IntrospectionPass introspectionPass = new IntrospectionPass();
        combinedTree.accept(introspectionPass);

        ScopeChecker scopeVisitor = new ScopeChecker();
        combinedTree.accept(scopeVisitor);

        ASTVisitor bytecodeVisitor = new BytecodeGenerator(outDir);
        combinedTree.accept(bytecodeVisitor);

        return combinedTree;
    }
    
    private static Node generateIRTree(InputStream stream, String filename) throws IOException {
            CharStream chars = new ANTLRInputStream(stream);
            Lexer lexer = new BabbleLexer(chars);
            TokenStream tokens = new CommonTokenStream(lexer);
            BabbleParser parser = new BabbleParser(tokens);

            ASTGenerator generator = new ASTGenerator(filename);
            Node irtree = generator.visitProgram(parser.program());
            
            if (verbose) {
                System.out.println("Parsed: " + filename);
            }
            return irtree;
    }
}
