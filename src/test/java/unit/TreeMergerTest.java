package unit;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.twnc.compile.exceptions.TreeMergeException;
import org.twnc.irtree.BaseASTVisitor;
import org.twnc.irtree.TreeMerger;
import org.twnc.irtree.nodes.ClazzNode;
import org.twnc.irtree.nodes.MethodNode;
import org.twnc.irtree.nodes.ProgramNode;

public class TreeMergerTest extends CompileTest{
    
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
    public void testMerge() {
        
        ProgramNode tree = getBaseTree(); 
        assertTrue(getErrors(tree).isEmpty());

        BaseASTVisitor treeMerger = new TreeMerger(tree);
        try {
            getTree("Duck1.bla").accept(treeMerger);
            
            ClazzNode duck = tree.getClazz("Duck");
            assertNotNull(duck);
            assertFalse(duck.getMethodSelectors().contains("Queck"));
            
            getTree("Duck4.bla").accept(treeMerger);
        } catch (TreeMergeException e) {
            fail();
        }

        assertTrue(treeMerger.getErrors().isEmpty());
        
        ClazzNode duck = tree.getClazz("Duck");
        assertNotNull(duck);
        
        Set<MethodNode> methods = duck.getMethods();
        Set<String> methodSelectors = duck.getMethodSelectors();
        assertEquals(methods.size(),3);
        assertEquals(methodSelectors.size(),3);
        
        assertTrue(methodSelectors.contains("Quack"));
        assertTrue(methodSelectors.contains("Queck"));
        assertTrue(methodSelectors.contains("Quick"));
    }

}
