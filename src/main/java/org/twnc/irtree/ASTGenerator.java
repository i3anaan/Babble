package org.twnc.irtree;

import java.beans.MethodDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.twnc.BabbleBaseVisitor;
import org.twnc.BabbleParser.*;

public class ASTGenerator extends BabbleBaseVisitor<Node> {

    @Override
    public Node visitTrueLit(TrueLitContext ctx) {
        return VarRefNode.TRUE;
    }

    @Override
    public Node visitNilLit(NilLitContext ctx) {
        return VarRefNode.NIL;
    }

    @Override
    public Node visitSymbolLit(SymbolLitContext ctx) {
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
    public Node visitBlock(BlockContext ctx) {
        List<VarRefNode> arguments = new ArrayList<>();
        for (TerminalNode node : ctx.ID()) {
            arguments.add(new VarRefNode(node.getText()));
        }
        SequenceNode sequence = (SequenceNode) visit(ctx.sequence());
        return new BlockNode(sequence, arguments);
    }

    @Override
    public Node visitFalseLit(FalseLitContext ctx) {
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
        return new SequenceNode(visitExprArguments(ctx.expr()));
    }

    @Override
    public Node visitGlobalKeywordSend(GlobalKeywordSendContext ctx) {
        String selector = buildSelector(ctx.ID());
        List<ExprNode> arguments = visitExprArguments(ctx.subexpr());
        return new SendNode(selector, arguments);
    }

    @Override
    public Node visitKeywordSend(KeywordSendContext ctx) {
        ExprNode expression = (ExprNode) visit(ctx.expr());
        String selector = buildSelector(ctx.ID());
        List<ExprNode> arguments = visitExprArguments(ctx.subexpr());
        return new SendNode(expression, selector, arguments);
    }

    @Override
    public Node visitMthd(MthdContext ctx) {
        VarRefNode objectName;
        if (ctx.object != null) {
            objectName = new VarRefNode(ctx.object.getText()); //ObjectMethodDefinition
        } else {
            objectName = null; //GlobalMethodDefinition
        }

        String selector = "";
        List<VarRefNode> arguments = new ArrayList<VarRefNode>();
        for (int i = 0; i < ctx.ID().size(); i += 2) {
            selector += ctx.ID(i) + ":";
            arguments.add(new VarRefNode(ctx.ID(i + 1).getText()));
        }
        SequenceNode sequence = (SequenceNode) visit(ctx.sequence());

        return new MethodNode(objectName, selector, arguments, sequence);
    }

    @Override
    public Node visitInfixSend(InfixSendContext ctx) {
        ExprNode expression = (ExprNode) visit(ctx.expr());
        String selector = ctx.method.getText();
        List<ExprNode> arguments = visitExprArguments(ctx.subexpr());
        return new SendNode(expression, selector, arguments);
    }

    @Override
    public Node visitUnarySend(UnarySendContext ctx) {
        ExprNode expression = (ExprNode) visit(ctx.expr());
        String selector = buildSelector(ctx.ID());
        List<ExprNode> arguments = new ArrayList<>();
        return new SendNode(expression, selector, arguments);
    }

    @Override
    public Node visitStrLit(StrLitContext ctx) {
        String quoted = ctx.string.getText();
        return new StringLitNode(quoted.substring(1, quoted.length()-1));
    }

    @Override
    public Node visitVarRef(VarRefContext ctx) {
        return new VarRefNode(ctx.getText());
    }

    @Override
    public Node visitIntLit(IntLitContext ctx) {
        return new IntLitNode(ctx.getText());
    }

    @Override
    public Node visitParenExpr(ParenExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Node visitLoneExpr(LoneExprContext ctx) {
        return visit(ctx.subexpr());
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

    public String buildSelector(TerminalNode item) {
        return buildSelector(Arrays.asList(item));
    }

    public String buildSelector(List<TerminalNode> list) {
        String selector = "";
        for (int i = 0; i < list.size(); i += 1) {
            selector += list.get(i) + ":";
        }
        return selector;
    }
}
