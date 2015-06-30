package org.twnc.util;

import org.twnc.irtree.BaseASTVisitor;
import org.twnc.irtree.nodes.*;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Optional;

public class Graphvizitor extends BaseASTVisitor<Void> {
    private final String outDir;
    private StringBuilder nodes;
    private StringBuilder edges;

    public Graphvizitor(String outputDirectory) {
        outDir = outputDirectory;
    }

    @Override
    public Void visit(ClazzNode node) {
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

        return null;
    }

    @Override
    public Void visit(MethodNode node) {
        makeNode(node);
        makeEdges(node, node.getArguments());
        makeEdge(node, node.getSequence());
        return super.visit(node);
    }

    @Override
    public Void visit(SendNode node) {
        makeNode(node);
        makeEdge(node, node.getExpression());
        makeEdges(node, node.getArguments());
        return super.visit(node);
    }

    @Override
    public Void visit(SequenceNode node) {
        makeNode(node);
        makeEdges(node, node.getExpressions());
        return super.visit(node);
    }

    @Override
    public Void visit(AssignNode node) {
        makeNode(node);
        makeEdge(node, node.getVariable());
        makeEdge(node, node.getExpression());
        return super.visit(node);
    }

    @Override
    public Void visit(BlockNode node) {
        makeNode(node);
        makeEdges(node, node.getArguments());
        makeEdge(node, node.getSequence());
        return super.visit(node);
    }

    @Override
    public Void visit(VarRefNode node) {
        return makeNode(node);
    }

    private Void makeNode(Node node) {
        nodes.append("  ");
        nodes.append(node.hashCode());
        nodes.append(" [");
        nodes.append("label=\"" + node.toString().replace("\"", "\\\"") + "\" ");
        nodes.append("color=\"" + describeColor(node.getColor()) + "\" ");;
        nodes.append("fillcolor=\"" + describeColor(node.getColor().brighter().brighter()) + "\"");
        nodes.append("];\n");
        return null;
    }

    private Void makeEdge(Node node, Node otherNode) {
        edges.append("  ").append(node.hashCode()).append(" -> ").append(otherNode.hashCode()).append(";\n");
        return null;
    }

    private Void makeEdge(Node node, Optional<? extends Node> otherNode) {
        otherNode.ifPresent(child -> makeEdge(node, child));
        return null;
    }

    private Void makeEdges(Node node, Collection<? extends Node> children) {
        children.forEach(child -> makeEdge(node, child));
        return null;
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
