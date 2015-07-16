package org.twnc.irtree;


import org.twnc.compile.exceptions.CompileException;
import org.twnc.compile.exceptions.TreeMergeException;
import org.twnc.irtree.nodes.ClazzNode;
import org.twnc.irtree.nodes.MethodNode;
import org.twnc.irtree.nodes.ProgramNode;

/**
 * ASTBaseVisitor that can be used to merge multiple ASTs into a single AST.
 * 
 * This visitor only visits the ProgramNode at the top of an AST.
 *
 */
public class TreeMerger extends ASTBaseVisitor {
    /** The tree which will be extended by merging extra trees in it. */
    private ProgramNode baseTree;
    
    /**
     * Constructs a new TreeMerger.
     * @param baseTree The AST in which to merge all the following trees.
     */
    public TreeMerger(ProgramNode baseTree) {
        this.baseTree = baseTree;
    }

    /**
     * Merges the extraTree into the currently contained baseTree.
     * @param extraTree The tree to merge into the current baseTree (extraTree stays unaffected.).
     */
    @Override
    public void visit(ProgramNode extraTree) throws CompileException {
        // First merge the ClazzNodes (not per se the children of the ClazzNodes.)
        for (ClazzNode extraClazz : extraTree.getClasses()) {
            ClazzNode baseClazz = baseTree.getClazz(extraClazz.getName());
            if (baseClazz != null) {
                // Found 2 classes with the same name, merge the contents of
                // these 2 ClazzNodes.
                mergeClasses(baseClazz, extraClazz);
            } else {
                // No class with this name exists yet, simply add it.
                baseTree.addClazz(extraClazz);
            }
        }

        // If there were errors merging the trees, print the errors and throw
        // an exception to abort compilation.
        if (!getErrors().isEmpty()) {
            for (String error : getErrors()) {
                System.err.println(error);
            }
            throw new TreeMergeException();
        }
    }

    /**
     * Fully merges the extraClazz into the baseClazz, giving the baseClazz every method the extraClazz has.
     * Will store an error if duplicate methods are found or if there is a discrepancy in the superclass.
     * @param baseClazz The class in which to merge the other.
     * @param extraClazz The class to merge in the other. (this ClazzNode stays unaffected.)
     */
    private void mergeClasses(ClazzNode baseClazz, ClazzNode extraClazz) {
        if (baseClazz.getSuperclass().equals(extraClazz.getSuperclass())) {
            baseClazz.getDecls().addDeclarations(extraClazz.getDecls().getDeclarations());
            for (MethodNode extraMethod : extraClazz.getMethods()) {
                if (!baseClazz.addMethod(extraMethod)) {
                    // Both baseClazz and extraClazz have a method with the same selector.
                    MethodNode baseMethod = baseClazz.getMethod(extraMethod.getSelector());
                    String message = String.format("Duplicate method declaration '%s' for class '%s' at [%s]", baseMethod.getSelector(), baseClazz.getName(), String.valueOf(extraMethod.getLocation()));
                    visitError(baseMethod, message);
                }
            }
        } else {
            // baseClazz and extraClazz have a different superclass.
            String message = String.format("Superclass discrepancy for class '%s': '%s' with '%s' at [%s]", baseClazz.getName(), baseClazz.getSuperclass(), extraClazz.getSuperclass(), String.valueOf(extraClazz.getLocation()));
            visitError(baseClazz, message);
        }
    }
}
