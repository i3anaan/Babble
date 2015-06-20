package org.twnc.irtree;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.twnc.BabbleBaseVisitor;
import org.twnc.BabbleParser.*;

public class ASTGenerator extends BabbleBaseVisitor<Node> {

    @Override
    public Node visitTrueExpr(TrueExprContext ctx) {
        return VarRefNode.TRUE;
    }

    @Override
    public Node visitNilExpr(NilExprContext ctx) {
        return VarRefNode.NIL;
    }

    @Override
    public Node visitSymbolExpr(SymbolExprContext ctx) {
        return new SymbolNode(ctx.ID().getText());
    }

    @Override
    public Node visitProgram(ProgramContext ctx) {
        List<MethodNode> methods = new ArrayList<MethodNode>();
        for (MthdContext context : ctx.mthd()) {
            methods.add((MethodNode) visit(context));
        }
        
        return new ProgramNode(methods);
    }

    @Override
    public Node visitBlockExpr(BlockExprContext ctx) {
        List<VarRefNode> arguments = new ArrayList<>();
        for (TerminalNode node : ctx.ID()) {
            arguments.add(new VarRefNode(node.getText()));
        }
        SequenceNode sequence = (SequenceNode) visit(ctx.sequence());
        return new BlockExprNode(sequence, arguments);
    }

    @Override
    public Node visitFalseExpr(FalseExprContext ctx) {
        return VarRefNode.FALSE;
    }

    @Override
    public Node visitAssignment(AssignmentContext ctx) {
        VarRefNode variable = new VarRefNode(ctx.getText());
        ExprNode expression = (ExprNode) visit(ctx.expr());
        return new AssignNode(variable, expression);
    }

    @Override
    public Node visitSequence(SequenceContext ctx) {
        List<StatNode> statements = new ArrayList<>();

        for (StmtContext context : ctx.stmt()) {
            statements.add((StatNode) visit(context));
        }

        return new SequenceNode(statements);
    }

    @Override
    public Node visitObjKeywordSend(ObjKeywordSendContext ctx) {
        // TODO Auto-generated method stub
        return super.visitObjKeywordSend(ctx);
    }

    @Override
    public Node visitKeywordSend(KeywordSendContext ctx) {
        StatNode statement = (StatNode) visit(ctx.stmt());
        String selector = "";
        List<ExprNode> arguments = new ArrayList<>();

        for (int i = 0; i < ctx.ID().size(); i += 1) {
            selector += ctx.ID(i) + ":";
            arguments.add((ExprNode) visit(ctx.expr(i)));
        }

        return new SendNode(statement, selector, arguments);
    }

    @Override
    public Node visitGlobalMethodDefinition(GlobalMethodDefinitionContext ctx) {
        String selector = "";
        List<VarRefNode> arguments = new ArrayList<VarRefNode>();

        for (int i = 0; i < ctx.ID().size(); i += 2) {
            selector += ctx.ID(i) + ":";
            arguments.add(new VarRefNode(ctx.ID(i + 1).getText()));
        }
        
        SequenceNode sequence = (SequenceNode) visit(ctx.sequence());

        return new MethodNode(selector, arguments, sequence);
    }

    @Override
    public Node visitClassMethodDefinition(ClassMethodDefinitionContext ctx) {
        VarRefNode objectName = new VarRefNode(ctx.object.getText());
        String selector = "";
        List<VarRefNode> arguments = new ArrayList<VarRefNode>();

        for (int i = 1; i < ctx.ID().size(); i += 2) {
            selector += ctx.ID(i) + ":";
            arguments.add(new VarRefNode(ctx.ID(i + 1).getText()));
        }
        
        SequenceNode sequence = (SequenceNode) visit(ctx.sequence());

        return new MethodNode(objectName, selector, arguments, sequence);
    }

    @Override
    public Node visitInfixSend(InfixSendContext ctx) {
        StatNode statement = (StatNode) visit(ctx.stmt());
        String selector = ctx.method.getText();
        List<ExprNode> arguments = new ArrayList<>();
        arguments.add((ExprNode) visit(ctx.expr()));

        return new SendNode(statement, selector, arguments);
    }

    @Override
    public Node visitUnarySend(UnarySendContext ctx) {
        StatNode statement = (StatNode) visit(ctx.stmt());
        String selector = ctx.method.getText();
        List<ExprNode> arguments = new ArrayList<>();

        return new SendNode(statement, selector, arguments);
    }

    @Override
    public Node visitStrExpr(StrExprContext ctx) {
        String quoted = ctx.string.getText();
        return new StringLitNode(quoted.substring(1, quoted.length()-1));
    }

    @Override
    public Node visitVarExpr(VarExprContext ctx) {
        return new VarRefNode(ctx.getText());
    }

    @Override
    public Node visitIntExpr(IntExprContext ctx) {
        return new IntLitNode(ctx.getText());
    }

    @Override
    public Node visitParenExpr(ParenExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Node visitLoneExpr(LoneExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Node visit(ParseTree tree) {
        Node n = super.visit(tree);

        if (tree instanceof ParserRuleContext) {
            Token tok = ((ParserRuleContext)tree).start;
            n.setLineOffset(tok.getLine(), tok.getCharPositionInLine());
        }

        return n;
    }
}
