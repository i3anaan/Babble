package org.twnc;

import org.antlr.v4.runtime.*;
import org.apache.commons.cli.*;
import org.twnc.backend.BytecodeGenerator;
import org.twnc.compile.exceptions.CompileException;
import org.twnc.frontend.GlobalsGenerator;
import org.twnc.frontend.MetaclassGenerator;
import org.twnc.frontend.ScopeChecker;
import org.twnc.irtree.ASTBaseVisitor;
import org.twnc.irtree.ASTGenerator;
import org.twnc.irtree.ASTVisitor;
import org.twnc.irtree.TreeMerger;
import org.twnc.irtree.nodes.Node;
import org.twnc.irtree.nodes.ProgramNode;
import org.twnc.util.Graphvizitor;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        ArrayList<ProgramNode> trees = new ArrayList<>();
        
        ProgramNode prelude = generateIRTree(App.class.getResourceAsStream("Prelude.bla"), "Babble\\Prelude.bla");
        trees.add(prelude);
        
        for (String inPath : line.getArgs()) {
            File file = new File(inPath);
            ProgramNode babbleFile = generateIRTree(new FileInputStream(file), file.getPath());
            
            trees.add(babbleFile);
        }
        
        compileTrees(outDir, trees.toArray(new ProgramNode[0]));
        if (verbose) {
            System.out.println("[ OK ] Compiled.");
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("app", options);
    }
    
    static List<String> compileTrees(String outDir, ProgramNode... trees) {
        ProgramNode program = new ProgramNode();
        ASTBaseVisitor treeMerger = new TreeMerger(program);
        try {
            for (ProgramNode tree : trees) {
                tree.accept(treeMerger);
            }
            
            return compileTree(outDir, program);
        } catch (CompileException e) {
            return treeMerger.getErrors();
        }
    }
    
    static List<String> compileTree(String outDir, ProgramNode program) {
        new File(outDir).mkdirs();
        ASTBaseVisitor visitor = null;
        try {
            visitor = new Graphvizitor(outDir);
            program.accept(visitor);
            
            visitor = new GlobalsGenerator();
            program.accept(visitor);
    
            visitor = new MetaclassGenerator();
            program.accept(visitor);
    
            visitor = new ScopeChecker();
            program.accept(visitor);
    
            visitor = new BytecodeGenerator(outDir);
            program.accept(visitor);
        } catch (CompileException e) {
            return visitor.getErrors();
        }
        
        return Collections.emptyList();
        
    }
    
    static ProgramNode generateIRTree(InputStream stream, String filename) throws IOException {
            CharStream chars = new ANTLRInputStream(stream);
            Lexer lexer = new BabbleLexer(chars);
            TokenStream tokens = new CommonTokenStream(lexer);
            BabbleParser parser = new BabbleParser(tokens);
            ASTGenerator generator = new ASTGenerator(filename);
            ProgramNode irtree = (ProgramNode) generator.visitProgram(parser.program());
            
            if (verbose) {
                System.out.println("Parsed: " + filename);
            }

            return irtree;
    }
}
