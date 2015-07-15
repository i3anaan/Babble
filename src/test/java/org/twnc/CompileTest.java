package org.twnc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.twnc.App;
import org.twnc.irtree.nodes.ProgramNode;

public class CompileTest {
    public static final String outDir = "target/test/temp/";
    public static final String testFilesDir = "src/test/bla/unitTests/";
        
    public ProgramNode buildTree(String filename) {
        try {
            return App.generateIRTree(new FileInputStream(new File(testFilesDir + filename)), filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public ProgramNode buildBaseTree() {
        try {
            return App.generateIRTree(App.class.getResourceAsStream("Prelude.bla"), "Babble\\Prelude.bla");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
