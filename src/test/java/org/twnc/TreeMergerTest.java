package org.twnc;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.twnc.compile.exceptions.TreeMergeException;
import org.twnc.irtree.ASTBaseVisitor;
import org.twnc.irtree.TreeMerger;
import org.twnc.irtree.nodes.ClazzNode;
import org.twnc.irtree.nodes.MethodNode;
import org.twnc.irtree.nodes.ProgramNode;
import org.twnc.irtree.nodes.VarDeclNode;

public class TreeMergerTest extends CompileTest {
    
    @Test
    public void testDuplicateFieldSignature() {
        ProgramNode tree = buildTree("Duck1.bla");
        assertTrue(getErrors(tree).isEmpty());

        ASTBaseVisitor treeMerger = new TreeMerger(tree);
        try {
            buildTree("Duck5.bla").accept(treeMerger);
            fail();
        } catch (TreeMergeException e) {
            assertFalse(treeMerger.getErrors().isEmpty());  
            String error = treeMerger.getErrors().get(0);
            assertEquals("[Duck1.bla - 3:1] - Duplicate Method declaration 'quack' at [Duck2.bla - 4:1]", error);  
        }
    }
    
    @Test
    public void testDuplicateMethodSignature() {
        ProgramNode tree = buildTree("Duck1.bla");
        assertTrue(getErrors(tree).isEmpty());

        ASTBaseVisitor treeMerger = new TreeMerger(tree);
        try {
            buildTree("Duck2.bla").accept(treeMerger);
            fail();
        } catch (TreeMergeException e) {
            assertFalse(treeMerger.getErrors().isEmpty());  
            String error = treeMerger.getErrors().get(0);
            assertEquals("[Duck1.bla - 3:1] - Duplicate Method declaration 'quack' at [Duck2.bla - 4:1]", error);  
        }
    }
    
    @Test
    public void testDuplicateMethodSignatureMultiple() {
        ProgramNode tree = buildTree("Duck1.bla");
        assertTrue(getErrors(tree).isEmpty());

        ASTBaseVisitor treeMerger = new TreeMerger(tree);
        try {
            buildTree("Duck1.bla").accept(treeMerger);
            fail();
        } catch (TreeMergeException e) {
            assertTrue(treeMerger.getErrors().size() > 1);
        }
    }
    
    @Test
    public void testSuperClassDiscrepancy() {
        ProgramNode tree = buildTree("Duck1.bla");
        assertTrue(getErrors(tree).isEmpty());

        ASTBaseVisitor treeMerger = new TreeMerger(tree);
        try {
            buildTree("Duck3.bla").accept(treeMerger);
            fail();
        } catch (TreeMergeException e) {
            assertFalse(treeMerger.getErrors().isEmpty());
            String error = treeMerger.getErrors().get(0);
            assertEquals("[Duck1.bla - 1:0] - Superclass discrepancy for Duck: java/lang/Object with Rock at [Duck3.bla - 1:0]", error);
        }
    }

    @Test
    public void testMerge() {
        ProgramNode tree = buildBaseTree(); 
        assertTrue(getErrors(tree).isEmpty());

        ASTBaseVisitor treeMerger = new TreeMerger(tree);
        try {
            buildTree("Duck1.bla").accept(treeMerger);
            
            ClazzNode duck = tree.getClazz("Duck");
            assertNotNull(duck);

            assertFalse(duck.getDecls().getDeclarations().contains("rightLeg"));
            assertFalse(duck.getMethodSelectors().contains("Queck"));
            
            buildTree("Duck4.bla").accept(treeMerger);
        } catch (TreeMergeException e) {
            fail();
        }

        assertTrue(treeMerger.getErrors().isEmpty());
        
        ClazzNode duck = tree.getClazz("Duck");
        assertNotNull(duck);
        
        List<VarDeclNode> decls = duck.getDecls().getDeclarations();
        assertEquals(decls.size(), 2);
        assertTrue(decls.contains("leftLeg"));
        assertTrue(decls.contains("rightLeg"));
        
        Set<MethodNode> methods = duck.getMethods();
        Set<String> methodSelectors = duck.getMethodSelectors();
        assertEquals(methods.size(),3);
        assertEquals(methodSelectors.size(),3);
        
        assertTrue(methodSelectors.contains("quack"));
        assertTrue(methodSelectors.contains("queck"));
        assertTrue(methodSelectors.contains("quick"));
    }

}
