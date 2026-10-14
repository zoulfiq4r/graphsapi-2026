import m1graphs2026.*;

import java.util.*;

import utils.GraphViz;

public class Main {
    public static void main(String[] args) throws Exception {
        Graph firstGraph = new Graph();
        Graph secondGraph = new Graph();

        Node node1 = new Node(1, firstGraph);
        Node node2 = new Node(2, "Node 2", firstGraph);
        Node sameNode = new Node(1, firstGraph);
        Node nodeInOtherGraph = new Node(1, secondGraph);

        // Getters
        assert node1.getId() == 1;
        assert node1.getName() == null;
        assert node1.getGraph() == firstGraph;
        assert node2.getName().equals("Node 2");

        // equals / hashCode
        assert node1.equals(sameNode);
        assert !node1.equals(nodeInOtherGraph);
        assert node1.hashCode() == sameNode.hashCode();

        // compareTo
        assert node1.compareTo(node2) < 0;
        assert node2.compareTo(node1) > 0;

        // Sorting
        List<Node> list = new ArrayList<>(List.of(new Node(3, firstGraph), node2, node1));
        Collections.sort(list);
        assert list.get(0).getId() == 1;
        assert list.get(1).getId() == 2;
        assert list.get(2).getId() == 3;

        // HashSet behaviour
        Set<Node> set = new HashSet<>();
        set.add(node1);
        set.add(sameNode);
        assert set.size() == 1;

        // Edge checks
        Edge e1 = new Edge(node1, node2);
        Edge e2 = new Edge(node1, node2, 1000);
        Edge e3 = new Edge(node1, node2, 1000);
        assert e2.equals(e3) && e2.hashCode() == e3.hashCode();
        assert !e1.equals(e2);
        assert e1.compareTo(e2) < 0;
        assert e1.toString().equals("1->2");
        assert new Edge(1, 1, firstGraph).isSelfLoop();
        assert e2.getSymmetric().from().equals(node2);

        System.out.println("All Node tests passed.");

        // Drawing only when asked: java -ea -cp out Main --show
        if (args.length > 0 && args[0].equals("--show")) {
            GraphViz.showNode(node2);
        }
    }
}