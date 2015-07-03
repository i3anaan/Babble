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
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.twnc.irtree.BaseASTVisitor;
import org.twnc.irtree.nodes.*;

public class BytecodeGenerator extends BaseASTVisitor<Void> implements Opcodes {
    private ClassWriter cw;
    private ClazzNode cn;
    private MethodVisitor mv;

    private String outDir;

    public BytecodeGenerator(String targetDirectory) {
        outDir = targetDirectory;
    }

    @Override
    public Void visit(ClazzNode clazzNode) {
        cn = clazzNode;
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS);
        cw.visit(52, ACC_PUBLIC + ACC_SUPER, clazzNode.getName(), null, clazzNode.getSuperclass(), null);

        cw.visitInnerClass("org/twnc/runtime/BObject", "org/twnc/runtime/Core", "BObject", ACC_PUBLIC + ACC_STATIC);
        
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
            mv.visitMethodInsn(INVOKEVIRTUAL, clazzNode.getName(), "_main", "()Lorg/twnc/runtime/BObject;", false);
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
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public Void visit(MethodNode methodNode) {
        StringBuilder type = new StringBuilder();
        type.append('(');

        for (int i = 0; i < methodNode.getArguments().size(); i++) {
            type.append("Lorg/twnc/runtime/BObject;");
        }

        type.append(")Lorg/twnc/runtime/BObject;");
        mv = cw.visitMethod(ACC_PUBLIC, mangle(methodNode.getSelector()),
                type.toString(), null, null);
        mv.visitCode();
        
        super.visit(methodNode);
        
        mv.visitInsn(ARETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        if (methodNode.isTestMethod()) {
            mv = cw.visitMethod(ACC_PUBLIC, methodNode.getSelector(), "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, cn.getName(), mangle(methodNode.getSelector()), "()Lorg/twnc/runtime/BObject;", false);
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        return null;
    }

    @Override
    public Void visit(SendNode sendNode) {
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
            type.append("Lorg/twnc/runtime/BObject;");
        }

        type.append(")Lorg/twnc/runtime/BObject;");

        mv.visitInvokeDynamicInsn(mangle(sendNode.getSelector()), type.toString(), bootstrap);
        
        return null;
    }

    @Override
    public Void visit(SequenceNode sequenceNode) {
        List<ExprNode> exprs = sequenceNode.getExpressions();

        for (int i = 0; i < exprs.size(); i++) {
            if (i > 0) {
                mv.visitInsn(POP);
            }

            exprs.get(i).accept(this);
        }

        return null;
    }

    @Override
    public Void visit(LiteralNode literalNode) {
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
        
        return super.visit(literalNode);
    }

    @Override
    public Void visit(VarRefNode varRefNode) {
        //TODO actually refer to a variable.

        if (varRefNode.getName().equals("this")) {
            mv.visitIntInsn(ALOAD, 0);
        } else {
            String object;
            switch (varRefNode.getName()) {
                case "true": object = "org/twnc/runtime/BTrue"; break;
                case "false": object = "org/twnc/runtime/BFalse"; break;
                default: object = "org/twnc/runtime/BNil"; break;
            }
            mv.visitTypeInsn(NEW, object);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, object, "<init>", "()V", false);
        }
        return super.visit(varRefNode);
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
