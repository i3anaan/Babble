package org.twnc.util;

import java.awt.Color;

import org.twnc.irtree.Node;

public class Graphvizivier {
    private Node tree;

    public Graphvizivier(Node irtree) {
        tree = irtree;
    }

    public String toGraph() {
        StringBuilder sb = new StringBuilder();

        sb.append("digraph G {\n");
        sb.append("\tnode [shape=\"box\" fontname=\"Helvetica\" style=\"filled\"];\n");
        appendNodes(sb, tree);
        sb.append("\n");
        appendEdges(sb, tree);
        sb.append("}\n");

        return sb.toString();
    }

    private void appendNodes(StringBuilder sb, Node node) {
        sb.append("\t" + node.hashCode() + " [");
        sb.append("label=\"" + node.toString().replace("\"", "\\\"") + "\" ");
        sb.append("color=\"" + describeColor(node.getColor()) + "\" ");
        sb.append("fillcolor=\"" + describeColor(node.getColor().brighter().brighter()) + "\"");
        sb.append("];\n");

        node.getChildren().forEach(child -> appendNodes(sb, child));
    }

    private void appendEdges(StringBuilder sb, Node node) {
        if (!node.getChildren().isEmpty()) {
            sb.append("\t" + node.hashCode() + " -> { ");
            node.getChildren().forEach(child -> sb.append(child.hashCode() + " "));
            sb.append("};\n");
        }

        node.getChildren().forEach(child -> appendEdges(sb, child));
    }

    private String describeColor(Color color) {
        return String.format(
                "#%02x%02x%02x",
                color.getRed(),
                color.getGreen(),
                color.getBlue()
        );
    }
}
