package org.twnc.irtree;


import org.twnc.compile.exceptions.CompileException;
import org.twnc.compile.exceptions.TreeMergeException;
import org.twnc.irtree.nodes.ClazzNode;
import org.twnc.irtree.nodes.MethodNode;
import org.twnc.irtree.nodes.ProgramNode;

public class TreeMerger extends ASTBaseVisitor {
    private ProgramNode baseTree;
    
    public TreeMerger(ProgramNode baseTree) {
        this.baseTree = baseTree;
    }

    @Override
    public void visit(ProgramNode extraTree) throws CompileException {
        for (ClazzNode extraClazz : extraTree.getClasses()) {
            ClazzNode baseClazz = baseTree.getClazz(extraClazz.getName());
            if (baseClazz != null) {
                mergeClasses(baseClazz, extraClazz);
            } else {
                baseTree.addClazz(extraClazz);
            }
        }

        if (!getErrors().isEmpty()) {
            for (String error : getErrors()) {
                System.err.println(error);
            }
            throw new TreeMergeException();
        }
    }

    private void mergeClasses(ClazzNode baseClazz, ClazzNode extraClazz) {
        if (baseClazz.getSuperclass().equals(extraClazz.getSuperclass())) {
            baseClazz.getDecls().addDeclarations(extraClazz.getDecls().getDeclarations());
            for (MethodNode extraMethod : extraClazz.getMethods()) {
                if (!baseClazz.addMethod(extraMethod)) {
                    MethodNode baseMethod = baseClazz.getMethod(extraMethod.getSelector());
                    String message = String.format("Duplicate method declaration '%s' for class '%s' at [%s]", baseMethod.getSelector(), baseClazz.getName(), String.valueOf(extraMethod.getLocation()));
                    visitError(baseMethod, message);
                }
            }
        } else {
            String message = String.format("Superclass discrepancy for class '%s': '%s' with '%s' at [%s]", baseClazz.getName(), baseClazz.getSuperclass(), extraClazz.getSuperclass(), String.valueOf(extraClazz.getLocation()));
            visitError(baseClazz, message);
        }
    }
}
