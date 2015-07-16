package org.twnc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.twnc.App;
import org.twnc.irtree.nodes.ProgramNode;

/**
 * Intended as a base class for testing phases of compilation
 *
 */
public class CompileTest {
    /** The directory to place the generated .class files during compilation. */
    public static final String outDir = "target/test/temp/";
    /** The directory where several .bla files designed for testing are stored. */
    public static final String testFilesDir = "src/test/bla/unitTests/";
    
    /**
     * Convenience method to easily create an AST for a test .bla file.
     * @param filename The name of the file to compile.
     * @return AST build from ANTLR parsing the file (no other visitors have been applied).
     */
    public ProgramNode buildTree(String filename) {
        try {
            return App.generateIRTree(new FileInputStream(new File(testFilesDir + filename)), filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Convenience method to easily create an AST for the Prelude.bla file.
     * @return AST build from ANTLR parsing Prelude.bla (no other visitors have been applied).
     */
    public ProgramNode buildBaseTree() {
        try {
            return App.generateIRTree(App.class.getResourceAsStream("Prelude.bla"), "Babble\\Prelude.bla");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
