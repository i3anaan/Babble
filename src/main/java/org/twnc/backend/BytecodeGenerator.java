package org.twnc.backend;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.twnc.irtree.BaseASTVisitor;
import org.twnc.irtree.nodes.*;

public class BytecodeGenerator extends BaseASTVisitor<Void> implements Opcodes {
    public static final String OUTPUT_PATH = "target/classes/";

    private ClassWriter cw;
    private ClazzNode cn;
    private MethodVisitor mv;
    
    @Override
    public Void visit(ClazzNode clazzNode) {
        cn = clazzNode;
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS);
        cw.visit(52, ACC_PUBLIC + ACC_SUPER, clazzNode.getName(), null, "org/twnc/runtime/BObject", null);

        cw.visitInnerClass("org/twnc/runtime/BObject", "org/twnc/runtime/Core", "BObject", ACC_PUBLIC + ACC_STATIC);
        
        mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "org/twnc/runtime/BObject", "<init>", "()V", false);
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
        
        String path = OUTPUT_PATH + clazzNode.getName() + ".class";
        
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
        
        mv.visitTypeInsn(NEW, "org/twnc/runtime/BNil");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "org/twnc/runtime/BNil", "<init>", "()V", false);

        super.visit(methodNode);
        
        mv.visitInsn(ARETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        if (methodNode.isTestMethod()) {
            mv = cw.visitMethod(ACC_PUBLIC, methodNode.getSelector(), "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, cn.getName(), methodNode.getSelector(), "()Lorg/twnc/runtime/BObject;", false);
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
    public Void visit(IntLitNode intLitNode) {
        mv.visitTypeInsn(NEW, "org/twnc/runtime/BInt");
        mv.visitInsn(DUP);
        mv.visitLdcInsn(intLitNode.getValue());
        mv.visitMethodInsn(INVOKESPECIAL, "org/twnc/runtime/BInt", "<init>", "(Ljava/lang/String;)V", false);
        
        return super.visit(intLitNode);
    }

    @Override
    public Void visit(StringLitNode stringLitNode) {
        mv.visitTypeInsn(NEW, "org/twnc/runtime/BStr");
        mv.visitInsn(DUP);
        mv.visitLdcInsn(stringLitNode.getString());
        mv.visitMethodInsn(INVOKESPECIAL, "org/twnc/runtime/BStr", "<init>", "(Ljava/lang/String;)V", false);
        
        return super.visit(stringLitNode);
    }

    @Override
    public Void visit(SymbolNode symbolNode) {
        mv.visitTypeInsn(NEW, "org/twnc/runtime/BSymbol");
        mv.visitInsn(DUP);
        mv.visitLdcInsn(symbolNode.getID());
        mv.visitIntInsn(BIPUSH, symbolNode.getNum());
        mv.visitMethodInsn(INVOKESPECIAL, "org/twnc/runtime/BSymbol", "<init>", "(Ljava/lang/String;I)V", false);
        
        return super.visit(symbolNode);
    }

    @Override
    public Void visit(VarRefNode varRefNode) {
        //TODO actually refer to a variable.
        String object;
        switch (varRefNode.getName()) {
            case "true": object = "org/twnc/runtime/BTrue"; break;
            case "false": object = "org/twnc/runtime/BFalse"; break;
            case "this": object = cn.getName(); break; // TODO this is not correct
            default: object = "org/twnc/runtime/BNil"; break;
        }
        mv.visitTypeInsn(NEW, object);
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, object, "<init>", "()V", false);
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
