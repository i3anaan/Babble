package unit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import junit.runner.Version;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.junit.Test;
import org.twnc.App;
import org.twnc.BabbleLexer;
import org.twnc.BabbleParser;
import org.twnc.backend.BytecodeGenerator;
import org.twnc.compile.exceptions.CompileException;
import org.twnc.compile.exceptions.TreeMergeException;
import org.twnc.frontend.IntrospectionPass;
import org.twnc.frontend.ScopeChecker;
import org.twnc.irtree.ASTGenerator;
import org.twnc.irtree.ASTVisitor;
import org.twnc.irtree.BaseASTVisitor;
import org.twnc.irtree.TreeMerger;
import org.twnc.irtree.nodes.Node;
import org.twnc.irtree.nodes.ProgramNode;
import org.twnc.util.Graphvizitor;

public class TestCompileExceptions {
    public static final String outDir = "target/test/temp";
    public static final String testFilesDir = "src/test/bla/";
    
    
    @Test
    public void testDuplicateMethodSignature() {
        ProgramNode tree = getTree("Duck1.bla");
        assertTrue(getErrors(tree).isEmpty());

        BaseASTVisitor treeMerger = new TreeMerger(tree);
        try {
            getTree("Duck2.bla").accept(treeMerger);
            fail();
        } catch (TreeMergeException e) {
            assertFalse(treeMerger.getErrors().isEmpty());  
            String error = treeMerger.getErrors().get(0);
            assertEquals("[Duck1.bla - 2:1] - Duplicate Method declaration 'Quack' at [Duck2.bla - 4:1]", error);  
        }
    }
    
    @Test
    public void testDuplicateMethodSignatureMultiple() {
        ProgramNode tree = getTree("Duck1.bla");
        assertTrue(getErrors(tree).isEmpty());

        BaseASTVisitor treeMerger = new TreeMerger(tree);
        try {
            getTree("Duck1.bla").accept(treeMerger);
            fail();
        } catch (TreeMergeException e) {
            assertTrue(treeMerger.getErrors().size() > 1);
        }
    }
    
    @Test
    public void testSuperClassDiscrepancy() {
        ProgramNode tree = getTree("Duck1.bla");
        assertTrue(getErrors(tree).isEmpty());

        BaseASTVisitor treeMerger = new TreeMerger(tree);
        try {
            getTree("Duck3.bla").accept(treeMerger);
            fail();
        } catch (TreeMergeException e) {
            assertFalse(treeMerger.getErrors().isEmpty());
            String error = treeMerger.getErrors().get(0);
            assertEquals("[Duck1.bla - 1:0] - Superclass discrepancy for Duck: java/lang/Object with Rock at [Duck3.bla - 1:0]", error);
        }
    }

    @Test
    public void testJUnitVersion() {
        assertEquals("4.12", Version.id());
    }
    
    
    
    private List<String> getErrors(ProgramNode baseTree) {
        BaseASTVisitor visitor = null;
        new File(outDir).mkdirs();
        try {
            visitor = new Graphvizitor(outDir);
            baseTree.accept(visitor);
    
            visitor = new IntrospectionPass();
            baseTree.accept(visitor);
    
            visitor = new ScopeChecker();
            baseTree.accept(visitor);
    
            visitor = new BytecodeGenerator(outDir);
            baseTree.accept(visitor);
        } catch (CompileException e) {
            return visitor.getErrors();
        }
        return Collections.emptyList();
    }
    
    private ProgramNode combine(ProgramNode tree1, ProgramNode tree2) {
        ASTVisitor treeMerger = new TreeMerger(tree1);
        tree2.accept(treeMerger);
        return tree1;
    }
    
    private ProgramNode getTree(String filename) {
        try {
            return App.generateIRTree(new FileInputStream(testFilesDir + filename), filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private ProgramNode getBaseTree() {
        try {
            return App.generateIRTree(App.class.getResourceAsStream("Prelude.bla"), "Babble\\Prelude.bla");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
