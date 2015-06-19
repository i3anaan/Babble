package org.twnc.irtree;

import java.util.ArrayList;
import java.util.List;

import org.twnc.BabbleBaseVisitor;
import org.twnc.BabbleParser.AssignmentContext;
import org.twnc.BabbleParser.BlockExprContext;
import org.twnc.BabbleParser.ClassMethodDefinitionContext;
import org.twnc.BabbleParser.DefsContext;
import org.twnc.BabbleParser.FalseExprContext;
import org.twnc.BabbleParser.GlobalMethodDefinitionContext;
import org.twnc.BabbleParser.InfixSendContext;
import org.twnc.BabbleParser.IntExprContext;
import org.twnc.BabbleParser.KeywordSendContext;
import org.twnc.BabbleParser.LocalMethodDefinitionContext;
import org.twnc.BabbleParser.LoneExprContext;
import org.twnc.BabbleParser.MethodDefinitionContext;
import org.twnc.BabbleParser.MthdContext;
import org.twnc.BabbleParser.NilExprContext;
import org.twnc.BabbleParser.ObjKeywordSendContext;
import org.twnc.BabbleParser.ParenExprContext;
import org.twnc.BabbleParser.ProgramContext;
import org.twnc.BabbleParser.SequenceContext;
import org.twnc.BabbleParser.StrExprContext;
import org.twnc.BabbleParser.SymbolExprContext;
import org.twnc.BabbleParser.TrueExprContext;
import org.twnc.BabbleParser.UnarySendContext;
import org.twnc.BabbleParser.VarExprContext;

public class ASTGenerator extends BabbleBaseVisitor<Node> {

    @Override
    public Node visitDefs(DefsContext ctx) {
        // TODO Auto-generated method stub
        return super.visitDefs(ctx);
    }

    @Override
    public Node visitTrueExpr(TrueExprContext ctx) {
        // TODO Auto-generated method stub
        return super.visitTrueExpr(ctx);
    }

    @Override
    public Node visitNilExpr(NilExprContext ctx) {
        // TODO Auto-generated method stub
        return super.visitNilExpr(ctx);
    }

    @Override
    public Node visitSymbolExpr(SymbolExprContext ctx) {
        // TODO Auto-generated method stub
        return super.visitSymbolExpr(ctx);
    }

    @Override
    public Node visitProgram(ProgramContext ctx) {
        List<MethodNode> methods = new ArrayList<MethodNode>();
        for (MthdContext m : ctx.mthd()) {
            methods.add((MethodNode) visit(m));
        }
        return new ProgramNode(methods);
    }

    @Override
    public Node visitBlockExpr(BlockExprContext ctx) {
        // TODO Auto-generated method stub
        return super.visitBlockExpr(ctx);
    }

    @Override
    public Node visitFalseExpr(FalseExprContext ctx) {
        // TODO Auto-generated method stub
        return super.visitFalseExpr(ctx);
    }

    @Override
    public Node visitAssignment(AssignmentContext ctx) {
        // TODO Auto-generated method stub
        return super.visitAssignment(ctx);
    }

    @Override
    public Node visitSequence(SequenceContext ctx) {
        // TODO Auto-generated method stub
        return super.visitSequence(ctx);
    }

    @Override
    public Node visitObjKeywordSend(ObjKeywordSendContext ctx) {
        // TODO Auto-generated method stub
        return super.visitObjKeywordSend(ctx);
    }

    @Override
    public Node visitKeywordSend(KeywordSendContext ctx) {
        // TODO Auto-generated method stub
        return super.visitKeywordSend(ctx);
    }

    @Override
    public Node visitGlobalMethodDefinition(GlobalMethodDefinitionContext ctx) {
        String selector = "";
        List<VarRefNode> arguments = new ArrayList<VarRefNode>();
        for (int i = 0; i < ctx.ID().size(); i += 2) {
            selector += ctx.ID(i) + ":";
            arguments.add(new VarRefNode(ctx.ID(i + 1).getText()));
        }
        return new MethodNode(selector, arguments);
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
        return new MethodNode(objectName, selector, arguments);
    }

    @Override
    public Node visitInfixSend(InfixSendContext ctx) {
        // TODO Auto-generated method stub
        return super.visitInfixSend(ctx);
    }

    @Override
    public Node visitUnarySend(UnarySendContext ctx) {
        // TODO Auto-generated method stub
        return super.visitUnarySend(ctx);
    }

    @Override
    public Node visitStrExpr(StrExprContext ctx) {
        // TODO Auto-generated method stub
        return super.visitStrExpr(ctx);
    }

    @Override
    public Node visitVarExpr(VarExprContext ctx) {
        // TODO Auto-generated method stub
        return super.visitVarExpr(ctx);
    }

    @Override
    public Node visitIntExpr(IntExprContext ctx) {
        // TODO Auto-generated method stub
        ctx.value.getText()
        return super.visitIntExpr(ctx);
    }

    @Override
    public Node visitParenExpr(ParenExprContext ctx) {
        // TODO Auto-generated method stub
        return super.visitParenExpr(ctx);
    }

    @Override
    public Node visitLoneExpr(LoneExprContext ctx) {
        // TODO Auto-generated method stub
        return super.visitLoneExpr(ctx);
    }

    
    
    
}
