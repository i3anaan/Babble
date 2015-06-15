package org.twnc;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;

public class App implements Opcodes {
    public static void main(String[] args) throws Exception {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS);

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "Hello", null, "org/twnc/Bootstrap", null);

        cw.visitInnerClass("org/twnc/Bootstrap$BObject", "org/twnc/Bootstrap", "BObject", ACC_PUBLIC);

        {
            MethodVisitor mv;
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "org/twnc/Bootstrap", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "LHello;", null, l0, l1, 0);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            MethodVisitor mv;
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitTypeInsn(NEW, "Hello");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "Hello", "<init>", "()V", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "Hello", "run", "()V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitInsn(RETURN);
            mv.visitLocalVariable("args", "[Ljava/lang/String;", null, l0, l1, 0);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            MethodVisitor mv;
            mv = cw.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitTypeInsn(NEW, "org/twnc/Bootstrap$BObject");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "org/twnc/Bootstrap$BObject", "<init>", "(Lorg/twnc/Bootstrap;)V", false);
            mv.visitVarInsn(ASTORE, 1);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn("create");
            mv.visitInsn(ICONST_0);
            mv.visitTypeInsn(ANEWARRAY, "org/twnc/Bootstrap$BObject");
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/twnc/Bootstrap$BObject", "invoke", "(Lorg/twnc/Bootstrap$BObject;Ljava/lang/String;[Lorg/twnc/Bootstrap$BObject;)Lorg/twnc/Bootstrap$BObject;", false);
            mv.visitInsn(POP);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitInsn(RETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", "LHello;", null, l0, l3, 0);
            mv.visitLocalVariable("world", "Lorg/twnc/Bootstrap$BObject;", null, l1, l3, 1);
            mv.visitMaxs(4, 2);
            mv.visitEnd();
        }

        cw.visitEnd();

        FileOutputStream fos = new FileOutputStream("target/classes/Hello.class");
        fos.write(cw.toByteArray());
        fos.close();
    }
}
