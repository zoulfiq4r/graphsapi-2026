package utils;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import m1graphs2026.Node;

public class GraphViz {

    /** Renders a DOT string to a PNG with GraphViz and opens it. */
    public static void show(String dot) throws IOException, InterruptedException {
        Path gv = Files.createTempFile("graph", ".gv");
        Path png = Path.of(gv.toString().replace(".gv", ".png"));
        Files.writeString(gv, dot);
        Process p = new ProcessBuilder("dot", "-Tpng", gv.toString(), "-o", png.toString())
                .inheritIO().start();
        if (p.waitFor() != 0) {
            throw new IOException("dot failed, is GraphViz installed?");
        }
        Desktop.getDesktop().open(png.toFile());
    }

    /** Draws a single node (id as identifier, name as label if any). */
    public static void showNode(Node n) throws IOException, InterruptedException {
        String label = (n.getName() == null) ? String.valueOf(n.getId()) : n.getName();
        show("digraph G {\n" + n.getId() + " [label=\"" + label + "\"]\n}");
    }
}
