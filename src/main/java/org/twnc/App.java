package org.twnc;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends BabbleBaseListener implements Opcodes {

    private ClassWriter cw;
    private MethodVisitor mv;
    private Label top;
    private Label bottom;

    public static void main(String[] args) throws Exception {
        String program = "" +
                "\"--- Booleans ---\" print." +
                "true print." +
                "true not print." +
                "true or:true print." +
                "true xor:true print." +
                "true or:false print." +
                "\"--- Maths ---\" print." +
                "12 print." +
                "13 negate print." +
                "20 + 30 print." +
                "40 / 11 print." +
                "\"--- Strings ---\" print." +
                "\"hello\" print." +
                "\"WORLD\" lower print." +
        "";

        CharStream chars = new ANTLRInputStream(program);
        Lexer lexer = new BabbleLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        BabbleParser parser = new BabbleParser(tokens);

        ParseTreeWalker walker = new ParseTreeWalker();
        ParseTree tree = parser.program();
        App app = new App();
        walker.walk(app, tree);
        app.writeBytecode("target/classes/Hello.class");

        System.out.println("[ OK ]");
    }

    public void writeBytecode(String path) throws IOException {
        OutputStream out = new FileOutputStream(path);
        out.write(cw.toByteArray());
        out.close();
    }

    @Override
    public void enterProgram(BabbleParser.ProgramContext ctx) {
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS);

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "Hello", null, "java/lang/Object", null);

        cw.visitInnerClass("Core$BObject", "Core", "BObject", ACC_PUBLIC + ACC_STATIC);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            mv.visitCode();
        }
    }

    @Override
    public void exitKeywordSend(BabbleParser.KeywordSendContext ctx) {
        List<String> bits = new ArrayList<>();
        ctx.ID().forEach(id -> bits.add(id.getText()));
        visitInvoke(mangle(bits.toArray()) + "_", bits.size());
    }

    @Override
    public void exitVarExpr(BabbleParser.VarExprContext ctx) {
        mv.visitTypeInsn(NEW, "Core$BObject");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "Core$BObject", "<init>", "()V", false);
    }

    @Override
    public void exitUnarySend(BabbleParser.UnarySendContext ctx) {
        visitInvoke(mangle(ctx.method.getText()), 0);
    }

    @Override
    public void exitNilExpr(BabbleParser.NilExprContext ctx) {
        mv.visitTypeInsn(NEW, "Core$BNil");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "Core$BNil", "<init>", "()V", false);
    }

    @Override
    public void exitFalseExpr(BabbleParser.FalseExprContext ctx) {
        mv.visitTypeInsn(NEW, "Core$BFalse");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "Core$BFalse", "<init>", "()V", false);
    }

    @Override
    public void exitTrueExpr(BabbleParser.TrueExprContext ctx) {
        mv.visitTypeInsn(NEW, "Core$BTrue");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "Core$BTrue", "<init>", "()V", false);
    }

    @Override
    public void exitStrExpr(BabbleParser.StrExprContext ctx) {
        String str = ctx.STRING().getText();
        mv.visitTypeInsn(NEW, "Core$BStr");
        mv.visitInsn(DUP);
        mv.visitLdcInsn(str.substring(1, str.length() - 1));
        mv.visitMethodInsn(INVOKESPECIAL, "Core$BStr", "<init>", "(Ljava/lang/String;)V", false);
    }

    @Override
    public void exitInfixSend(BabbleParser.InfixSendContext ctx) {
        visitInvoke(mangle(ctx.method.getText()) + "_", 1);
    }

    @Override
    public void exitIntExpr(BabbleParser.IntExprContext ctx) {
        mv.visitTypeInsn(NEW, "Core$BInt");
        mv.visitInsn(DUP);
        mv.visitLdcInsn(ctx.getText());
        mv.visitMethodInsn(INVOKESPECIAL, "Core$BInt", "<init>", "(Ljava/lang/String;)V", false);
    }

    @Override
    public void exitProgram(BabbleParser.ProgramContext ctx) {
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        cw.visitEnd();
    }

    private void visitInvoke(String selector, int numArgs) {
        // [ ... receiver args
        mv.visitIntInsn(BIPUSH, numArgs);
        // [ ... receiver args n
        mv.visitTypeInsn(ANEWARRAY, "Core$BObject");
        // [ ... receiver args array

        for (int i = 0; i < numArgs; i++) {
            // [ ... receiver args array
            mv.visitInsn(DUP_X1);
            // [ ... receiver args array arg array
            mv.visitInsn(SWAP);
            // [ ... receiver args array array arg
            mv.visitIntInsn(BIPUSH, i);
            // [ ... receiver args array array arg n
            mv.visitInsn(SWAP);
            // [ ... receiver args array array n arg
            mv.visitInsn(AASTORE);
            // [ ... receiver args array
        }

        // [ ... receiver array
        mv.visitLdcInsn(selector);
        // [ ... receiver array selector
        mv.visitInsn(SWAP);
        // [ ... receiver selector array
        mv.visitMethodInsn(INVOKESTATIC, "Core", "invoke", "(LCore$BObject;Ljava/lang/String;[LCore$BObject;)LCore$BObject;", false);
        // [ ... result
    }

    private String mangle(Object... bits) {
        StringBuilder sb = new StringBuilder();

        Map<Character, String> rep = new HashMap<>();
        rep.put('+', "plus");
        rep.put('-', "minus");
        rep.put('/', "slash");
        rep.put('*', "star");
        rep.put('!', "bang");

        for (Object bit : bits) {
            sb.append('_');
            for (char c : bit.toString().toCharArray()) {
                sb.append(rep.getOrDefault(c, "" + c));
            }
        }

        return sb.toString();
    }
}
