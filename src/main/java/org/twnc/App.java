package org.twnc;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.twnc.BabbleParser.ProgramContext;
import org.twnc.irtree.ASTGenerator;
import org.twnc.irtree.Node;
import org.twnc.util.Graphvizivier;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends BabbleBaseListener implements Opcodes {
    private ClassWriter cw;
    private MethodVisitor mv;

    private Map<String, Integer> symbols;

    private String name;

    public App(String name) {
        this.name = name;
        symbols = new HashMap<>();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: babble <file.bla>");
        } else {
            String path = args[0];
            File file = new File(path);

            CharStream chars = new ANTLRInputStream(new FileInputStream(file));
            Lexer lexer = new BabbleLexer(chars);
            TokenStream tokens = new CommonTokenStream(lexer);
            BabbleParser parser = new BabbleParser(tokens);

            String target = "target/classes/" + file.getName().replace(".bla", ".class");

            ParseTreeWalker walker = new ParseTreeWalker();
            ParseTree tree = parser.program();
            
            ASTGenerator generator = new ASTGenerator();
            System.out.println(tree);
            Node irtree = generator.visitProgram((ProgramContext) tree);
            Graphvizivier graphvizivier = new Graphvizivier();
            System.out.println(irtree);
            System.out.println(graphvizivier.nodeToGraph(irtree));
            
            App app = new App(file.getName().split("\\.")[0]);
            walker.walk(app, tree);
            app.writeBytecode(target);

            System.out.println(String.format("[ OK ] Compiled %s", target));
        }
    }

    public void writeBytecode(String path) throws IOException {
        OutputStream out = new FileOutputStream(path);
        out.write(cw.toByteArray());
        out.close();
    }

    @Override
    public void enterProgram(BabbleParser.ProgramContext ctx) {
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS);

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, name, null, "java/lang/Object", null);

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
    public void exitSymbolExpr(BabbleParser.SymbolExprContext ctx) {
        String sym = ctx.ID().getText();

        int num;
        if (symbols.containsKey(sym)) {
            num = symbols.get(sym);
        } else {
            num = symbols.size();
            symbols.put(sym, num);
        }

        mv.visitTypeInsn(NEW, "Core$BSymbol");
        mv.visitInsn(DUP);
        mv.visitLdcInsn(sym);
        mv.visitIntInsn(BIPUSH, num);
        mv.visitMethodInsn(INVOKESPECIAL, "Core$BSymbol", "<init>", "(Ljava/lang/String;I)V", false);
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

        for (int i = numArgs - 1; i >= 0; i--) {
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
        rep.put(',', "comma");

        for (Object bit : bits) {
            sb.append('_');
            for (char c : bit.toString().toCharArray()) {
                sb.append(rep.getOrDefault(c, "" + c));
            }
        }

        return sb.toString();
    }
}
