package m1graphs2026;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Undirected graph stored as symmetric adjacency entries.
 */
public class UndirectedGraph extends Graph {
    public UndirectedGraph() {
        super();
    }

    public UndirectedGraph(int... successorArray) {
        int node = 1;
        addNode(node);
        for (int value : successorArray) {
            if (value == 0) {
                node++;
                addNode(node);
            } else {
                addEdge(node, value);
            }
        }
        if (getNode(node) != null && getOutEdges(node).isEmpty()) {
            removeNode(node);
        }
    }

    @Override
    public boolean addEdge(Edge edge) {
        if (edge == null) {
            return false;
        }
        boolean added = super.addEdge(edge);
        if (!edge.isSelfLoop()) {
            super.addEdge(edge.getSymmetric());
        }
        return added;
    }

    @Override
    public boolean removeEdge(Node from, Node to) {
        return removeEdge(from, to, null, false);
    }

    @Override
    public boolean removeEdge(Node from, Node to, Integer weight) {
        return removeEdge(from, to, weight, true);
    }

    private boolean removeEdge(Node from, Node to, Integer weight, boolean matchWeight) {
        if (!holdsNode(from) || !holdsNode(to)) {
            return false;
        }
        Edge selected = null;
        for (Edge edge : getOutEdges(from)) {
            if (edge.to().equals(to)
                    && (!matchWeight || java.util.Objects.equals(edge.getWeight(), weight))) {
                selected = edge;
                break;
            }
        }
        if (selected == null) {
            return false;
        }
        adjEdList.get(from).remove(selected);
        if (!from.equals(to)) {
            for (Edge reverse : getOutEdges(to)) {
                if (reverse.to().equals(from)
                        && java.util.Objects.equals(reverse.getWeight(), selected.getWeight())) {
                    adjEdList.get(to).remove(reverse);
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public int nbEdges() {
        return getAllEdges().size();
    }

    @Override
    public List<Edge> getInEdges(Node node) {
        return getOutEdges(node);
    }

    @Override
    public List<Edge> getAllEdges() {
        List<Edge> result = new ArrayList<>();
        for (Edge edge : super.getAllEdges()) {
            if (edge.from().getId() < edge.to().getId()
                    || edge.from().getId() == edge.to().getId()) {
                result.add(edge);
            }
        }
        return result;
    }

    @Override
    public List<Edge> getIncidentEdges(Node node) {
        return getOutEdges(node);
    }

    @Override
    public int inDegree(Node node) {
        return undirectedDegree(node);
    }

    @Override
    public int outDegree(Node node) {
        return undirectedDegree(node);
    }

    @Override
    public int degree(Node node) {
        return undirectedDegree(node);
    }

    private int undirectedDegree(Node node) {
        int degree = 0;
        for (Edge edge : getOutEdges(node)) {
            degree += edge.isSelfLoop() ? 2 : 1;
        }
        return degree;
    }

    @Override
    protected Graph newEmptyGraph() {
        return new UndirectedGraph();
    }

    @Override
    public UndirectedGraph getTransitiveClosure() {
        return (UndirectedGraph) super.getTransitiveClosure();
    }

    @Override
    public UndirectedGraph getReverse() {
        return (UndirectedGraph) super.getReverse();
    }

    @Override
    public UndirectedGraph copy() {
        return (UndirectedGraph) super.copy();
    }

    @Override
    public int[] toSuccessorArray() {
        List<Integer> values = new ArrayList<>();
        List<Node> nodes = getAllNodes();
        nodes.sort(Comparator.naturalOrder());
        for (Node node : nodes) {
            List<Integer> successors = new ArrayList<>();
            for (Edge edge : getOutEdges(node)) {
                if (edge.to().getId() >= node.getId()) {
                    successors.add(edge.to().getId());
                }
            }
            successors.sort(Integer::compareTo);
            values.addAll(successors);
            values.add(0);
        }
        return values.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public String toDotString() {
        StringBuilder result = new StringBuilder("graph {\n\trankdir=LR\n");
        List<Node> nodes = getAllNodes();
        nodes.sort(Comparator.naturalOrder());
        for (Node node : nodes) {
            List<Edge> edges = getOutEdges(node);
            edges.sort(Comparator.comparingInt(e -> e.to().getId()));
            boolean printed = false;
            for (Edge edge : edges) {
                if (edge.from().getId() > edge.to().getId()) {
                    continue;
                }
                result.append("\t").append(edge.from().getId()).append(" -- ")
                        .append(edge.to().getId());
                if (edge.isWeighted()) {
                    result.append(" [label=").append(edge.getWeight())
                            .append(", len=").append(edge.getWeight()).append("]");
                }
                result.append("\n");
                printed = true;
            }
            if (!printed && getInEdges(node).isEmpty()) {
                result.append("\t").append(node.getId()).append("\n");
            }
        }
        return result.append("}\n").toString();
    }

    public static UndirectedGraph fromDotFile(String name) {
        return fromDotFile(name, ".gv");
    }

    public static UndirectedGraph fromDotFile(String name, String extension) {
        Graph graph = Graph.fromDotFile(name, extension);
        if (graph == null) {
            return null;
        }
        UndirectedGraph result = new UndirectedGraph();
        for (Node node : graph.getAllNodes()) {
            result.addNode(node.getId());
        }
        for (Edge edge : graph.getAllEdges()) {
            if (edge.from().getId() <= edge.to().getId()) {
                result.addEdge(edge.from().getId(), edge.to().getId(), edge.getWeight());
            }
        }
        return result;
    }
}
