package org.twnc.backend;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.twnc.Scope;
import org.twnc.compile.exceptions.CompileException;
import org.twnc.compile.exceptions.UnknownVariableDeclarationLocation;
import org.twnc.compile.exceptions.VariableNotDeclaredException;
import org.twnc.irtree.ASTBaseVisitor;
import org.twnc.irtree.nodes.*;
import org.twnc.runtime.Core;

public class BytecodeGenerator extends ASTBaseVisitor implements Opcodes {
    private ProgramNode pn;
    private ClazzNode cn;
    private ClassWriter cw;
    private MethodVisitor mv;
    private Scope scope; //TODO Only some Nodes have Scopes currently.

    private String outDir;
    private int blockCount;

    private static final String OBJ = "Ljava/lang/Object;";

    public BytecodeGenerator(String targetDirectory) {
        outDir = targetDirectory;
    }

    @Override
    public void visit(ProgramNode programNode) throws CompileException {
        pn = programNode;
        super.visit(programNode);
    }

    @Override
    public void visit(ClazzNode clazzNode) {
        scope = clazzNode.getScope();
        cn = clazzNode;
        blockCount = 0;

        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(52, ACC_PUBLIC + ACC_SUPER, clazzNode.getName(), null, clazzNode.getSuperclass(), null);

        cw.visitInnerClass("java/lang/Object", "org/twnc/runtime/Core", "Object", ACC_PUBLIC + ACC_STATIC);

        for (VarDeclNode decl : scope.values()) {
            FieldVisitor fv = cw.visitField(ACC_PUBLIC, decl.getName(), OBJ, null, null);
            fv.visitEnd();
        }

        mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, clazzNode.getSuperclass(), "<init>", "()V", false);

        visit(clazzNode.getDecls());

        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        if (clazzNode.hasMain()) {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, clazzNode.getName());
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, clazzNode.getName(), "<init>", "()V", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, clazzNode.getName(), "_main", "()" + OBJ, false);
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        clazzNode.getMethods().forEach(x -> visit(x));

        cw.visitEnd();

        String path = outDir + clazzNode.getName() + ".class";

        try (OutputStream out = new FileOutputStream(path)) {
            out.write(cw.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(MethodNode methodNode) {
        scope = methodNode.getScope();

        StringBuilder type = new StringBuilder();
        type.append('(');

        for (int i = 0; i < methodNode.getArguments().size(); i++) {
            type.append(OBJ);
        }

        type.append(")");
        type.append(OBJ);

        mv = cw.visitMethod(ACC_PUBLIC, Core.mangle(methodNode.getSelector()),
                type.toString(), null, null);
        mv.visitCode();

        Label start = new Label();
        Label end = new Label();

        mv.visitLabel(start);
        methodNode.getSequence().accept(this);

        if (methodNode.getSequence().getExpressions().isEmpty()) {
            // Return Nil if the method is empty.
            mv.visitTypeInsn(NEW, "Nil");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "Nil", "<init>", "()V", false);
        }

        mv.visitLabel(end);

        for (VarDeclNode decl : methodNode.getScope().values()) {
            mv.visitLocalVariable(decl.getName(), OBJ, null, start, end, decl.getOffset());
        }

        mv.visitInsn(ARETURN);

        mv.visitMaxs(0, 0);

        mv.visitEnd();

        if (methodNode.isTestMethod()) {
            mv = cw.visitMethod(ACC_PUBLIC, methodNode.getSelector(), "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, cn.getName(), Core.mangle(methodNode.getSelector()), "()" + OBJ, false);
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
    }

    @Override
    public void visit(BlockNode blockNode) {
        // Come up with a name for the closure class, i.e. Foo$0
        blockNode.setName(cn.getName() + "$" + blockCount);
        // Make sure the next block is not called Foo$0
        blockCount++;
        // Put it in the same directory as Foo
        String path = outDir + blockNode.getName() + ".class";

        // KEEP a reference to the parent method and scope
        MethodVisitor parentMethod = mv;
        Scope parentScope = scope;

        ClassWriter bw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        bw.visit(52, ACC_PUBLIC + ACC_SUPER, blockNode.getName(), null, "Block", null);

        scope = blockNode.getScope();

        // Add fields for storing closure variables
        for (VarDeclNode decl : blockNode.getScope().values()) {
            FieldVisitor fv = bw.visitField(ACC_PUBLIC, decl.getName(), OBJ, null, null);
            fv.visitEnd();
        }

        // Plain constructor.
        mv = bw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "Block", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        // #value method override.
        mv = bw.visitMethod(ACC_PUBLIC, Core.mangle("value"), "()" + OBJ, null, null);
        mv.visitCode();

        super.visit(blockNode);

        mv.visitInsn(ARETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        bw.visitEnd();

        try (OutputStream out = new FileOutputStream(path)) {
            out.write(bw.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // RESTORE the reference to the parent method as the current method
        mv = parentMethod;
        scope = parentScope;

        // Create a new instance of our closure class
        newObject(blockNode.getName());

        // Fill the closure instance fields with values from our scope
        for (VarDeclNode copy : blockNode.getScope().values()) {
            try {
                VarDeclNode original = scope.getVarDeclNode(copy.getName());
                mv.visitInsn(DUP);
                loadFromDecl(original);
                mv.visitFieldInsn(PUTFIELD, ((BlockNode) copy.getScope().getNode()).getName(), copy.getName(), OBJ);
            } catch (VariableNotDeclaredException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void visit(SendNode sendNode) {
        super.visit(sendNode);

        MethodType bmt = MethodType.methodType(
                CallSite.class,
                MethodHandles.Lookup.class,
                String.class,
                MethodType.class
            );

        Handle bootstrap = new Handle(Opcodes.H_INVOKESTATIC,
                "org/twnc/runtime/Core", "bootstrap",
                bmt.toMethodDescriptorString());

        StringBuilder type = new StringBuilder();
        type.append('(');

        for (int i = 0; i < sendNode.getArguments().size() + 1; i++) {
            type.append(OBJ);
        }

        type.append(")");
        type.append(OBJ);

        mv.visitInvokeDynamicInsn(Core.mangle(sendNode.getSelector()), type.toString(), bootstrap);
    }

    @Override
    public void visit(AssignNode assignNode) {
        super.visit(assignNode);

        String var = assignNode.getVariable().getName();
        try {
            VarDeclNode decl = scope.getVarDeclNode(var);
            storeToDecl(decl);
        } catch (VariableNotDeclaredException e) {
            // This should not happen (ScopeChecker should have detected this and aborted compiling).
            e.printStackTrace();
        }
    }

    @Override
    public void visit(SequenceNode sequenceNode) {
        List<ExprNode> exprs = sequenceNode.getExpressions();

        for (int i = 0; i < exprs.size(); i++) {
            if (i > 0) {
                mv.visitInsn(POP);
            }

            exprs.get(i).accept(this);
        }
    }

    @Override
    public void visit(ArrayNode arrayNode) {
        List<ExprNode> expressions = arrayNode.getExpressions();

        // Put an empty array on the stack, of the correct size...
        mv.visitIntInsn(BIPUSH, expressions.size());
        mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
        // ...twice, to have a copy for aastore.
        mv.visitInsn(DUP);

        for (int i = 0; i < expressions.size(); i++) {
            // AASTORE expects stack to be [... array, index, value.
            // Push the index first.
            mv.visitIntInsn(BIPUSH, i);

            // Calculate expression, which pushes value.
            expressions.get(i).accept(this);

            // Chuck it into the array.
            mv.visitInsn(AASTORE);

            // That consumed array, so dup our original array reference.
            mv.visitInsn(DUP);
        }

        // Pop the duplicate reference.
        mv.visitInsn(POP);

        // The filled array is now ready, turn it into a BArray
        mv.visitMethodInsn(
            INVOKESTATIC,
            "org/twnc/runtime/BArray",
            "make",
            "([Ljava/lang/Object;)Lorg/twnc/runtime/BArray;",
            false);
    }

    @Override
    public void visit(LiteralNode literalNode) {
        switch (literalNode.getType()) {
            case INTEGER: newFromString("org/twnc/runtime/BInt", literalNode.getValue()); break;
            case STRING:  newFromString("org/twnc/runtime/BStr", literalNode.getValue()); break;
            case SYMBOL:  newFromString("org/twnc/runtime/BSymbol", literalNode.getValue()); break;
            case CLASS:   newObject(literalNode.getValue()); break;
        }

        super.visit(literalNode);
    }
    @Override
    public void visit(VarRefNode varRefNode) {
        String name = varRefNode.getName();

        try {
            VarDeclNode decl = scope.getVarDeclNode(name);
            loadFromDecl(decl);
        } catch (VariableNotDeclaredException e) {
            // Maybe it's a global?

            String globalType = pn.getGlobals().get(name);

            if (globalType != null) {
                // Yes, it's a global
                newObject(globalType);
            } else {
                // This should not happen (ScopeChecker should have detected this and aborted compiling).
                e.printStackTrace();
            }
        }

        super.visit(varRefNode);
    }

    @Override
    public void visit(VarDeclNode varDeclNode) {
        // Initialize a variable to Nil.
        newObject("Nil");
        storeToDecl(varDeclNode);
    }

    private void newObject(String t) {
        mv.visitTypeInsn(NEW, t);
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, t, "<init>", "()V", false);
    }

    private void newFromString(String t, String value) {
        mv.visitTypeInsn(NEW, t);
        mv.visitInsn(DUP);
        mv.visitLdcInsn(value);
        mv.visitMethodInsn(INVOKESPECIAL, t, "<init>", "(Ljava/lang/String;)V", false);
    }

    private void loadFromDecl(VarDeclNode decl) {
        if (decl.isMethodVariable()) {
            mv.visitVarInsn(ALOAD, decl.getOffset());
        } else if (decl.isClassField()) {
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, ((ClazzNode) decl.getScope().getNode()).getName(), decl.getName(), OBJ);
        } else if (decl.isClosureCopy()) {
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, ((BlockNode) decl.getScope().getNode()).getName(), decl.getName(), OBJ);
        } else {
            throw new UnknownVariableDeclarationLocation();
        }
    }

    private void storeToDecl(VarDeclNode decl) {
        if (decl.isMethodVariable()) {
            mv.visitInsn(DUP);
            mv.visitVarInsn(ASTORE, decl.getOffset());
        } else if (decl.isClassField()) {
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(SWAP);
            mv.visitFieldInsn(PUTFIELD, ((ClazzNode) decl.getScope().getNode()).getName(), decl.getName(), OBJ);
        } else if (decl.isClosureCopy()) {
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(SWAP);
            mv.visitFieldInsn(PUTFIELD, ((BlockNode) decl.getScope().getNode()).getName(), decl.getName(), OBJ);
        } else {
            throw new UnknownVariableDeclarationLocation();
        }
    }

}
