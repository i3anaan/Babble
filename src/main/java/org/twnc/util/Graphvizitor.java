package org.twnc.util;

import org.twnc.irtree.ASTBaseVisitor;
import org.twnc.irtree.nodes.*;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Optional;

/**
 * ASTBaseVisitor that can be used to generate a visual representation of an AST.
 * 
 * Generates .dot files for each class, which can be read by the GraphViz library.
 * (http://www.graphviz.org/)
 */
public class Graphvizitor extends ASTBaseVisitor {
    /** Output directory where the .dot files get placed. */
    private final String outDir;
    /** Builds the String that labels and formats the nodes. */
    private StringBuilder nodes;
    /** Builds the String that contains the edges for the nodes. */
    private StringBuilder edges;

    /**
     * Constructs a new GraphVizitor.
     * @param outputDirectory Output directory where the .dot files get placed.
     */
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

    /**
     * Appends the label and format information of the given Node to the nodes StringBuilder.
     * @param node Node to append.
     */
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

    /**
     * Appends the edge information for the given Nodes
     * @param node  From
     * @param otherNode To
     */
    private void makeEdge(Node node, Node otherNode) {
        edges.append("  ").append(node.hashCode()).append(" -> ").append(otherNode.hashCode()).append(";\n");
    }

    /**
     * Appends the edge information for the given Nodes
     * @param node  From
     * @param otherNode To
     */
    private void makeEdges(Node node, Collection<? extends Node> children) {
        children.forEach(child -> makeEdge(node, child));
    }

    /**
     * Transforms the Color object into a format supported by the dot language.
     * @param color Color to format
     * @return Formatted String representing the color.
     */
    private static String describeColor(Color color) {
        return String.format(
                "#%02x%02x%02x",
                color.getRed(),
                color.getGreen(),
                color.getBlue()
        );
    }
}
