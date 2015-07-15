package org.twnc;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import junit.runner.Version;

import org.junit.Test;
import org.twnc.App;
import org.twnc.backend.BytecodeGenerator;
import org.twnc.compile.exceptions.CompileException;
import org.twnc.frontend.GlobalsGenerator;
import org.twnc.frontend.MetaclassGenerator;
import org.twnc.frontend.ScopeChecker;
import org.twnc.irtree.ASTVisitor;
import org.twnc.irtree.ASTBaseVisitor;
import org.twnc.irtree.TreeMerger;
import org.twnc.irtree.nodes.ProgramNode;
import org.twnc.util.Graphvizitor;

public class CompileTest {
    public static final String outDir = "target/test/temp";
    public static final String testFilesDir = "src/test/bla/unitTests/";
    
    public List<String> getErrors(ProgramNode baseTree) {
        ASTBaseVisitor visitor = null;
        new File(outDir).mkdirs();
        try {
            visitor = new Graphvizitor(outDir);
            baseTree.accept(visitor);
            
            GlobalsGenerator globalsGen = new GlobalsGenerator();
            baseTree.accept(globalsGen);

            MetaclassGenerator metaclassGen = new MetaclassGenerator();
            baseTree.accept(metaclassGen);
    
            visitor = new ScopeChecker();
            baseTree.accept(visitor);
    
            visitor = new BytecodeGenerator(outDir);
            baseTree.accept(visitor);
        } catch (CompileException e) {
            return visitor.getErrors();
        }
        return Collections.emptyList();
    }
    
    public ProgramNode combine(ProgramNode tree1, ProgramNode tree2) {
        ASTVisitor treeMerger = new TreeMerger(tree1);
        tree2.accept(treeMerger);
        return tree1;
    }
    
    public ProgramNode buildTree(String filename) {
        try {
            return App.generateIRTree(new FileInputStream(testFilesDir + filename), filename);
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
