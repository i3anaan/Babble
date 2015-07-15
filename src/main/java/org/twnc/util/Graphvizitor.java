package org.twnc.util;

import org.twnc.irtree.ASTBaseVisitor;
import org.twnc.irtree.nodes.*;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Optional;

public class Graphvizitor extends ASTBaseVisitor {
    private final String outDir;
    private StringBuilder nodes;
    private StringBuilder edges;

    public Graphvizitor(String outputDirectory) {
        outDir = outputDirectory;
    }

    @Override
    public void visit(ClazzNode node) {
        nodes = new StringBuilder();
        edges = new StringBuilder();

        makeNode(node);
        makeEdges(node, node.getMethods());
        super.visit(node);

        try (PrintWriter pw = new PrintWriter(outDir + node.getName() + ".dot")) {
            pw.write("digraph " + node.getName() + " {\n");
            pw.write("  node [shape=\"box\" fontname=\"Helvetica\" style=\"filled\"];\n");
            pw.write("\n");
            pw.write(nodes.toString());
            pw.write("\n");
            pw.write(edges.toString());
            pw.write("}\n");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(MethodNode node) {
        makeNode(node);
        makeEdges(node, node.getArguments());
        makeEdge(node, node.getSequence());
        super.visit(node);
    }

    @Override
    public void visit(SendNode node) {
        makeNode(node);
        makeEdge(node, node.getExpression());
        makeEdges(node, node.getArguments());
        super.visit(node);
    }

    @Override
    public void visit(SequenceNode node) {
        makeNode(node);
        makeEdges(node, node.getExpressions());
        super.visit(node);
    }

    @Override
    public void visit(AssignNode node) {
        makeNode(node);
        makeEdge(node, node.getVariable());
        makeEdge(node, node.getExpression());
        super.visit(node);
    }

    @Override
    public void visit(BlockNode node) {
        makeNode(node);
        makeEdge(node, node.getSequence());
        super.visit(node);
    }

    @Override
    public void visit(VarRefNode node) {
        makeNode(node);
    }

    @Override
    public void visit(VarDeclNode node) {
        makeNode(node);
        super.visit(node);
    }

    @Override
    public void visit(DeclsNode node) {
        makeNode(node);
        makeEdges(node, node.getDeclarations());
        super.visit(node);
    }

    @Override
    public void visit(LiteralNode node) {
        makeNode(node);
        super.visit(node);
    }

    @Override
    public void visit(ArrayNode node) {
        makeNode(node);
        makeEdges(node, node.getExpressions());
        super.visit(node);
    }

    private void makeNode(Node node) {
        nodes.append("  ");
        nodes.append(node.hashCode());
        nodes.append(" [");
        String label = node.toString() + "\n" + String.valueOf(node.getLocation());
        nodes.append("label=\"" + label.replace("\"", "\\\"").replace("\\", "\\\\") + "\" ");
        nodes.append("color=\"" + describeColor(node.getColor()) + "\" ");;
        nodes.append("fillcolor=\"" + describeColor(node.getColor().brighter().brighter()) + "\"");
        nodes.append("];\n");
    }

    private void makeEdge(Node node, Node otherNode) {
        edges.append("  ").append(node.hashCode()).append(" -> ").append(otherNode.hashCode()).append(";\n");
    }

    private void makeEdge(Node node, Optional<? extends Node> otherNode) {
        otherNode.ifPresent(child -> makeEdge(node, child));
    }

    private void makeEdges(Node node, Collection<? extends Node> children) {
        children.forEach(child -> makeEdge(node, child));
    }

    private static String describeColor(Color color) {
        return String.format(
                "#%02x%02x%02x",
                color.getRed(),
                color.getGreen(),
                color.getBlue()
        );
    }
}
