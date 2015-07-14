package org.twnc.backend;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.twnc.Scope;
import org.twnc.ScopeStack;
import org.twnc.compile.exceptions.VariableNotDeclaredException;
import org.twnc.irtree.BaseASTVisitor;
import org.twnc.irtree.nodes.*;

public class BytecodeGenerator extends BaseASTVisitor implements Opcodes {
    private ClassWriter cw;
    private ClazzNode cn;
    private MethodVisitor mv;
    private Scope scope;

    private String outDir;
    private int blockCount;

    private static final String OBJ = "Ljava/lang/Object;";

    public BytecodeGenerator(String targetDirectory) {
        outDir = targetDirectory;
    }

    @Override
    public void visit(ClazzNode clazzNode) {
        cn = clazzNode;
        blockCount = 0;
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(52, ACC_PUBLIC + ACC_SUPER, clazzNode.getName(), null, clazzNode.getSuperclass(), null);

        cw.visitInnerClass("java/lang/Object", "org/twnc/runtime/Core", "Object", ACC_PUBLIC + ACC_STATIC);
        
        mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, clazzNode.getSuperclass(), "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        
        if (clazzNode.hasMain()) {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, clazzNode.getName());
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, clazzNode.getName(), "<init>", "()V", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, clazzNode.getName(), "_main", "()L" + OBJ, false);
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        
        super.visit(clazzNode);
        
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

        mv = cw.visitMethod(ACC_PUBLIC, mangle(methodNode.getSelector()),
                type.toString(), null, null);
        mv.visitCode();

        Label start = new Label();
        Label end = new Label();

        mv.visitLabel(start);
        super.visit(methodNode);
        mv.visitLabel(end);

        for (VarDeclNode decl : scope.values()) {
            mv.visitLocalVariable(decl.getName(), OBJ, null, start, end, decl.getOffset());
        }

        mv.visitInsn(ARETURN);

        mv.visitMaxs(0, 0);

        mv.visitEnd();

        if (methodNode.isTestMethod()) {
            mv = cw.visitMethod(ACC_PUBLIC, methodNode.getSelector(), "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, cn.getName(), mangle(methodNode.getSelector()), "()" + OBJ, false);
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
    }

    @Override
    public void visit(BlockNode blockNode) {
        // Come up with a name for the closure class, i.e. Foo$0
        String blockClassName = cn.getName() + "$" + blockCount;
        // Make sure the next block is not called Foo$0
        blockCount++;
        // Put it in the same directory as Foo
        String path = outDir + blockClassName + ".class";

        // KEEP a reference to the parent method
        MethodVisitor parentMethod = mv;

        ClassWriter bw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        bw.visit(52, ACC_PUBLIC + ACC_SUPER, blockClassName, null, "org/twnc/runtime/BBlock", null);

        // Plain constructor.
        mv = bw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "org/twnc/runtime/BBlock", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        // #value method override.
        mv = bw.visitMethod(ACC_PUBLIC, mangle("value"), "()" + OBJ, null, null);
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

        // Create a new instance of our closure class
        mv.visitTypeInsn(NEW, blockClassName);
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, blockClassName, "<init>", "()V", false);
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

        mv.visitInvokeDynamicInsn(mangle(sendNode.getSelector()), type.toString(), bootstrap);
    }

    @Override
    public void visit(AssignNode assignNode) {
        super.visit(assignNode);

        String var = assignNode.getVariable().getName();
        try {
            VarDeclNode decl = scope.getVarDeclNode(var);
            mv.visitInsn(DUP);
            mv.visitVarInsn(ASTORE, decl.getOffset());
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
    public void visit(LiteralNode literalNode) {
        String t = "";

        switch (literalNode.getType()) {
            case INTEGER: t = "org/twnc/runtime/BInt"; break;
            case STRING:  t = "org/twnc/runtime/BStr"; break;
            case SYMBOL:  t = "org/twnc/runtime/BSymbol"; break;
        }

        mv.visitTypeInsn(NEW, t);
        mv.visitInsn(DUP);
        mv.visitLdcInsn(literalNode.getValue());
        mv.visitMethodInsn(INVOKESPECIAL, t, "<init>", "(Ljava/lang/String;)V", false);

        super.visit(literalNode);
    }

    @Override
    public void visit(VarRefNode varRefNode) {
        String name = varRefNode.getName();

        if (ScopeStack.isSpecial(name)) {
            String object;
            switch (name) {
                case "true": object = "True"; break;
                case "false": object = "False"; break;
                default: object = "Nil"; break;
            }

            mv.visitTypeInsn(NEW, object);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, object, "<init>", "()V", false);
        } else {
            try {
                VarDeclNode decl = scope.getVarDeclNode(varRefNode.getName());
                mv.visitVarInsn(ALOAD, decl.getOffset());
            } catch (VariableNotDeclaredException e) {
                // This should not happen (ScopeChecker should have detected this and aborted compiling).
                e.printStackTrace();
            }
        }
        super.visit(varRefNode);
    }
    
    @Override
    public void visit(VarDeclNode varDeclNode) {
        // Initialize a variable to Nil.
        mv.visitTypeInsn(NEW, "Nil");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "Nil", "<init>", "()V", false);
        mv.visitInsn(DUP);
        mv.visitVarInsn(ASTORE, varDeclNode.getOffset());

        super.visit(varDeclNode);
    }

    private static String mangle(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append('_');

        Map<Character, String> rep = new HashMap<>();
        rep.put('+', "plus");
        rep.put('-', "minus");
        rep.put('/', "slash");
        rep.put('*', "star");
        rep.put('!', "bang");
        rep.put(',', "comma");
        rep.put('=', "eq");
        rep.put('<', "lt");
        rep.put('>', "gt");
        rep.put(':', "_");

        for (char c : str.toCharArray()) {
            sb.append(rep.getOrDefault(c, String.valueOf(c)));
        }

        return sb.toString();
    }
}
