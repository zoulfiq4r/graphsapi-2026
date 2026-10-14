package m1graphs2026;

import java.util.Objects;

public class Edge implements Comparable<Edge> {
    private final Node from;
    private final Node to;
    private final Integer weight; // null means unweighted

    public Edge(Node from, Node to) {
        this(from, to, null);
    }

    public Edge(Node from, Node to, Integer weight) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("from and to must both be non-null");
        }
        if (from.getGraph() == null || to.getGraph() == null || from.getGraph() != to.getGraph()) {
            throw new IllegalArgumentException("both nodes must belong to the same graph");
        }
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Edge(int fromId, int toId, Graph graph) {
        this(new Node(fromId, graph), new Node(toId, graph), null);
    }

    public Edge(int fromId, int toId, Graph graph, Integer weight) {
        this(new Node(fromId, graph), new Node(toId, graph), weight);
    }

    public Edge(int fromId, int toId, Graph graph, int weight) {
        this(new Node(fromId, graph), new Node(toId, graph), Integer.valueOf(weight));
    }

    public Node from() {
        return from;
    }

    public Node to() {
        return to;
    }

    public Integer getWeight() {
        return weight;
    }

    public boolean isWeighted() {
        return weight != null;
    }

    public boolean isSelfLoop() {
        return from.equals(to);
    }

    public Edge getSymmetric() {
        return new Edge(to, from, weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Edge)) {
            return false;
        }
        Edge other = (Edge) obj;
        return Objects.equals(from, other.from)
                && Objects.equals(to, other.to)
                && Objects.equals(weight, other.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, weight);
    }

    @Override
    public int compareTo(Edge other) {
        int cmp = Integer.compare(this.from.getId(), other.from.getId());
        if (cmp != 0) {
            return cmp;
        }

        cmp = Integer.compare(this.to.getId(), other.to.getId());
        if (cmp != 0) {
            return cmp;
        }

        return compareWeights(this.weight, other.weight);
    }

    private int compareWeights(Integer w1, Integer w2) {
        if (w1 == null && w2 == null) {
            return 0;
        }
        if (w1 == null) {
            return -1;
        }
        if (w2 == null) {
            return 1;
        }
        return Integer.compare(w1, w2);
    }

    @Override
    public String toString() {
        return from.getId() + "->" + to.getId();
    }
}
