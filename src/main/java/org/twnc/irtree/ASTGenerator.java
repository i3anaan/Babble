package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.twnc.BabbleBaseVisitor;
import org.twnc.BabbleParser;
import org.twnc.BabbleParser.DeclExprContext;
import org.twnc.BabbleParser.*;
import org.twnc.irtree.nodes.*;
import org.twnc.irtree.nodes.LiteralNode.Type;

public class ASTGenerator extends BabbleBaseVisitor<Node> {
    private String filename;
    
    public ASTGenerator(String filename) {
        this.filename = filename;
    }
    
    @Override
    public Node visitTrueLit(TrueLitContext ctx) {
        return VarRefNode.newTrue();
    }

    @Override
    public Node visitNilLit(NilLitContext ctx) {
        return VarRefNode.newNil();
    }

    @Override
    public Node visitSymbolLit(SymbolLitContext ctx) {
        return new LiteralNode(Type.SYMBOL, ctx.ID().getText());
    }

    @Override
    public Node visitProgram(ProgramContext ctx) {
        List<ClazzNode> classes = new ArrayList<ClazzNode>();
        for (ClazzContext context : ctx.clazz()) {
            classes.add((ClazzNode) visit(context));
        }

        return new ProgramNode(classes);
    }

    @Override
    public Node visitClazz(ClazzContext ctx) {
        String superclass;
        String clazzName = ctx.classname.getText();

        if (ctx.superclass != null) {
            superclass = ctx.superclass.getText().replace('\\', '/');
        } else {
            superclass = "java/lang/Object";
        }

        DeclsNode decls;
        if (ctx.decls() != null) {
            decls = (DeclsNode) visit(ctx.decls());
        } else {
            decls = new DeclsNode();
        }
        
        Set<MethodNode> methods = new HashSet<>();
        for (MthdContext m : ctx.mthd()) {
            methods.add((MethodNode) visit(m));
        }
        
        return new ClazzNode(clazzName, superclass, decls, methods);
    }

    @Override
    public Node visitBlock(BlockContext ctx) {
        SequenceNode sequence = (SequenceNode) visit(ctx.sequence());
        return new BlockNode(sequence);
    }

    @Override
    public Node visitFalseLit(FalseLitContext ctx) {
        return VarRefNode.newFalse();
    }

    @Override
    public Node visitAssignment(AssignmentContext ctx) {
        VarRefNode variable = new VarRefNode(ctx.ID().getText());
        setLocation(variable, ctx.ID().getSymbol());
        ExprNode expression = (ExprNode) visit(ctx.expr());
        return new AssignNode(variable, expression);
    }

    @Override
    public Node visitSequence(SequenceContext ctx) {
        return new SequenceNode(visitExprArguments(ctx.expr()));
    }

    @Override
    public Node visitArrayLit(ArrayLitContext ctx) {
        return new ArrayNode(visitExprArguments(ctx.expr()));
    }

    @Override
    public Node visitKeywordSend(KeywordSendContext ctx) {
        ExprNode expression = (ExprNode) visit(ctx.expr());
        String selector = buildSelector(ctx.ID());
        List<ExprNode> arguments = visitExprArguments(ctx.subexpr());
        return new SendNode(expression, selector, arguments);
    }

    @Override
    public Node visitKeywordMethod(KeywordMethodContext ctx) {
        StringBuilder selector = new StringBuilder();
        List<VarDeclNode> arguments = new ArrayList<>();

        for (int i = 0; i < ctx.ID().size(); i++) {
            selector.append(ctx.ID(i)).append(":");
            VarDeclNode var = new VarDeclNode(ctx.decl(i).getText());
            setLocation(var, ctx.decl(i).ID().getSymbol());
            arguments.add(var);
        }

        SequenceNode sequence = (SequenceNode) visit(ctx.sequence());

        return new MethodNode(selector.toString(), arguments, sequence);
    }
    
    @Override
    public Node visitUnaryMethod(UnaryMethodContext ctx) {
        String selector = ctx.method.getText();
        SequenceNode sequence = (SequenceNode) visit(ctx.sequence());

        return new MethodNode(selector, sequence);
    }

    @Override
    public Node visitInfixMethod(InfixMethodContext ctx) {
        String selector = ctx.method.getText() + ':';
        SequenceNode sequence = (SequenceNode) visit(ctx.sequence());

        VarDeclNode arg = new VarDeclNode(ctx.arg.getText());
        setLocation(arg, ctx.arg.ID().getSymbol());

        return new MethodNode(selector, Arrays.asList(arg), sequence);
    }

    @Override
    public Node visitInfixSend(InfixSendContext ctx) {
        ExprNode expression = (ExprNode) visit(ctx.rcv);
        String selector = ctx.method.getText() + ':';
        List<ExprNode> arguments = visitExprArguments(ctx.arg);
        return new SendNode(expression, selector, arguments);
    }

    @Override
    public Node visitUnarySend(UnarySendContext ctx) {
        ExprNode expression = (ExprNode) visit(ctx.expr());
        String selector = ctx.ID().getText();
        return new SendNode(expression, selector);
    }

    @Override
    public Node visitStrLit(StrLitContext ctx) {
        String quoted = ctx.string.getText();
        String unquoted = quoted.substring(1, quoted.length() - 1);
        return new LiteralNode(Type.STRING, unquoted);
    }

    @Override
    public Node visitVarRef(VarRefContext ctx) {
        return new VarRefNode(ctx.getText());
    }

    @Override
    public Node visitIntLit(IntLitContext ctx) {
        return new LiteralNode(Type.INTEGER, ctx.getText());
    }

    @Override
    public Node visitParenExpr(ParenExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Node visitLoneExpr(LoneExprContext ctx) {
        return visit(ctx.subexpr());
    }

    @Override
    public Node visitDecl(DeclContext ctx) {
        return new VarDeclNode(ctx.getText());
    }
    
    @Override
    public Node visitDecls(DeclsContext ctx) {
        return new DeclsNode(visitDecls(ctx.decl()));
        
    }

    @Override
    public Node visit(ParseTree tree) {
        Node n = super.visit(tree);
        if (tree instanceof ParserRuleContext) {
            setLocation(n, ((ParserRuleContext)tree).getStart());
        }
        return n;
    }
    
    private void setLocation(Node node, Token tok) {
        Location loc = new Location(filename, tok.getLine(), tok.getCharPositionInLine());
        node.setLocation(loc);
    }

    public List<ExprNode> visitExprArguments(ParserRuleContext argument) {
        return visitExprArguments(Arrays.asList(argument));
    }

    public List<ExprNode> visitExprArguments(List<? extends ParserRuleContext> arguments) {
        List<ExprNode> output = new ArrayList<>();
        for (int i = 0; i < arguments.size(); i += 1) {
            output.add((ExprNode) visit(arguments.get(i)));
        }
        return output;
    }
    
    public Set<VarDeclNode> visitDecls(List<? extends ParserRuleContext> decls) {
        Set<VarDeclNode> output = new HashSet<>();
        for (int i = 0; i < decls.size(); i += 1) {
            output.add((VarDeclNode) visit(decls.get(i)));
        }
        return output;
    }
    
    public String buildSelector(List<TerminalNode> list) {
        String selector = "";
        for (int i = 0; i < list.size(); i += 1) {
            selector += list.get(i).toString() + ":";
        }
        return selector;
    }
}
