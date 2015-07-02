package org.twnc;

import org.antlr.v4.runtime.*;
import org.apache.commons.cli.*;
import org.twnc.backend.BytecodeGenerator;
import org.twnc.irtree.ASTGenerator;
import org.twnc.irtree.ASTVisitor;
import org.twnc.irtree.nodes.Node;
import org.twnc.util.Graphvizitor;

import java.io.*;

public final class App {
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

        boolean verbose = line.hasOption('v');

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
        CharStream chars = new ANTLRInputStream(new FileInputStream(file));
        Lexer lexer = new BabbleLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        BabbleParser parser = new BabbleParser(tokens);

        ASTGenerator generator = new ASTGenerator();
        Node irtree = generator.visitProgram(parser.program());

        ASTVisitor<Void> graphVisitor = new Graphvizitor(outDir);
        irtree.accept(graphVisitor);

        ASTVisitor<Void> bytecodeVisitor = new BytecodeGenerator(outDir);
        irtree.accept(bytecodeVisitor);

        return irtree;
    }
}
