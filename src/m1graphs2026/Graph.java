package m1graphs2026;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Directed graph represented by outgoing adjacency lists.
 */
public class Graph {
    protected Map<Node, List<Edge>> adjEdList = new HashMap<>();

    public Graph() {
    }

    public Graph(int... successorArray) {
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
        if (adjEdList.containsKey(new Node(node, this))
                && adjEdList.get(new Node(node, this)).isEmpty()) {
            removeNode(node);
        }
    }

    public int nbNodes() {
        return adjEdList.size();
    }

    public int nbEdges() {
        int count = 0;
        for (List<Edge> edges : adjEdList.values()) {
            count += edges.size();
        }
        return count;
    }

    public boolean usesNode(int id) {
        return getNode(id) != null;
    }

    public boolean usesNode(Node node) {
        return holdsNode(node);
    }

    public boolean holdsNode(Node node) {
        return node != null && node.getGraph() == this && adjEdList.containsKey(node);
    }

    public Node getNode(int id) {
        for (Node node : adjEdList.keySet()) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null;
    }

    public boolean addNode(int id) {
        if (usesNode(id)) {
            return false;
        }
        adjEdList.put(new Node(id, this), new ArrayList<>());
        return true;
    }

    public boolean addNode(Node node) {
        return node != null && node.getGraph() == this && addNode(node.getId());
    }

    public boolean removeNode(int id) {
        Node node = getNode(id);
        return node != null && removeNode(node);
    }

    public boolean removeNode(Node node) {
        if (!holdsNode(node)) {
            return false;
        }
        adjEdList.remove(node);
        for (List<Edge> edges : adjEdList.values()) {
            edges.removeIf(edge -> edge.to().equals(node));
        }
        return true;
    }

    public List<Node> getAllNodes() {
        return new ArrayList<>(adjEdList.keySet());
    }

    public int largestNodeId() {
        return adjEdList.keySet().stream().mapToInt(Node::getId).max().orElse(0);
    }

    public int smallestNodeId() {
        return adjEdList.keySet().stream().mapToInt(Node::getId).min().orElse(0);
    }

    public boolean addEdge(int from, int to) {
        return addEdge(from, to, null);
    }

    public boolean addEdge(int from, int to, int weight) {
        return addEdge(from, to, Integer.valueOf(weight));
    }

    public boolean addEdge(int from, int to, Integer weight) {
        if (!usesNode(from)) {
            addNode(from);
        }
        if (!usesNode(to)) {
            addNode(to);
        }
        return addEdge(new Edge(getNode(from), getNode(to), weight));
    }

    public boolean addEdge(Node from, Node to) {
        return addEdge(from, to, null);
    }

    public boolean addEdge(Node from, Node to, Integer weight) {
        if (from == null || to == null || from.getGraph() != this || to.getGraph() != this) {
            return false;
        }
        if (!holdsNode(from)) {
            addNode(from.getId());
        }
        if (!holdsNode(to)) {
            addNode(to.getId());
        }
        return addEdge(new Edge(getNode(from.getId()), getNode(to.getId()), weight));
    }

    public boolean addEdge(Edge edge) {
        if (edge == null || edge.from().getGraph() != this || edge.to().getGraph() != this) {
            return false;
        }
        if (!holdsNode(edge.from())) {
            addNode(edge.from().getId());
        }
        if (!holdsNode(edge.to())) {
            addNode(edge.to().getId());
        }
        adjEdList.get(getNode(edge.from().getId())).add(
                new Edge(getNode(edge.from().getId()), getNode(edge.to().getId()), edge.getWeight()));
        return true;
    }

    public List<Edge> getOutEdges(Node node) {
        if (!holdsNode(node)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(adjEdList.get(node));
    }

    public List<Edge> getOutEdges(int id) {
        return getOutEdges(getNode(id));
    }

    public List<Edge> getInEdges(Node node) {
        if (!holdsNode(node)) {
            return new ArrayList<>();
        }
        List<Edge> result = new ArrayList<>();
        for (List<Edge> edges : adjEdList.values()) {
            for (Edge edge : edges) {
                if (edge.to().equals(node)) {
                    result.add(edge);
                }
            }
        }
        return result;
    }

    public List<Edge> getInEdges(int id) {
        return getInEdges(getNode(id));
    }

    public List<Edge> getIncidentEdges(Node node) {
        List<Edge> result = getOutEdges(node);
        for (Edge edge : getInEdges(node)) {
            if (!(edge.from().equals(node) && edge.to().equals(node))) {
                result.add(edge);
            }
        }
        return result;
    }

    public List<Edge> getIncidentEdges(int id) {
        return getIncidentEdges(getNode(id));
    }

    public List<Edge> getEdges(Node from, Node to) {
        if (!holdsNode(from) || !holdsNode(to)) {
            return new ArrayList<>();
        }
        List<Edge> result = new ArrayList<>();
        for (Edge edge : adjEdList.get(from)) {
            if (edge.to().equals(to)) {
                result.add(edge);
            }
        }
        return result;
    }

    public List<Edge> getEdges(int from, int to) {
        return getEdges(getNode(from), getNode(to));
    }

    public boolean existsEdge(int from, int to) {
        return !getEdges(from, to).isEmpty();
    }

    public boolean existsEdge(Node from, Node to) {
        return !getEdges(from, to).isEmpty();
    }

    public boolean existsEdge(Edge edge) {
        return edge != null && getEdges(edge.from(), edge.to()).contains(edge);
    }

    public boolean isMultiEdge(Node from, Node to) {
        return getEdges(from, to).size() > 1;
    }

    public boolean isMultiEdge(int from, int to) {
        return isMultiEdge(getNode(from), getNode(to));
    }

    public boolean isMultiEdge(Edge edge) {
        return edge != null && isMultiEdge(edge.from(), edge.to());
    }

    public boolean removeEdge(int from, int to) {
        return removeEdge(getNode(from), getNode(to));
    }

    public boolean removeEdge(Node from, Node to) {
        List<Edge> edges = getEdges(from, to);
        return !edges.isEmpty() && adjEdList.get(from).remove(edges.get(0));
    }

    public boolean removeEdge(int from, int to, Integer weight) {
        return removeEdge(getNode(from), getNode(to), weight);
    }

    public boolean removeEdge(Node from, Node to, Integer weight) {
        if (!holdsNode(from) || !holdsNode(to)) {
            return false;
        }
        for (Edge edge : new ArrayList<>(adjEdList.get(from))) {
            if (edge.to().equals(to)
                    && java.util.Objects.equals(edge.getWeight(), weight)) {
                return adjEdList.get(from).remove(edge);
            }
        }
        return false;
    }

    public boolean removeEdge(Edge edge) {
        return edge != null && removeEdge(edge.from(), edge.to(), edge.getWeight());
    }

    public List<Edge> getAllEdges() {
        List<Edge> result = new ArrayList<>();
        for (List<Edge> edges : adjEdList.values()) {
            result.addAll(edges);
        }
        return result;
    }

    public List<Node> getSuccessors(Node node) {
        Set<Node> result = new LinkedHashSet<>();
        for (Edge edge : getOutEdges(node)) {
            result.add(edge.to());
        }
        return new ArrayList<>(result);
    }

    public List<Node> getSuccessors(int id) {
        return getSuccessors(getNode(id));
    }

    public List<Node> getSuccessorsMulti(Node node) {
        List<Node> result = new ArrayList<>();
        for (Edge edge : getOutEdges(node)) {
            result.add(edge.to());
        }
        return result;
    }

    public List<Node> getSuccessorsMulti(int id) {
        return getSuccessorsMulti(getNode(id));
    }

    public boolean adjacent(Node from, Node to) {
        return existsEdge(from, to) || existsEdge(to, from);
    }

    public boolean adjacent(int from, int to) {
        return adjacent(getNode(from), getNode(to));
    }

    public int inDegree(Node node) {
        return getInEdges(node).size();
    }

    public int inDegree(int id) {
        return inDegree(getNode(id));
    }

    public int outDegree(Node node) {
        return getOutEdges(node).size();
    }

    public int outDegree(int id) {
        return outDegree(getNode(id));
    }

    public int degree(Node node) {
        return inDegree(node) + outDegree(node);
    }

    public int degree(int id) {
        return degree(getNode(id));
    }

    public boolean hasSelfLoops() {
        return getAllEdges().stream().anyMatch(Edge::isSelfLoop);
    }

    public boolean isMultiGraph() {
        for (Node node : getAllNodes()) {
            for (Node successor : getSuccessors(node)) {
                if (isMultiEdge(node, successor)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSimpleGraph() {
        return !isMultiGraph() && !hasSelfLoops();
    }

    public Graph toSimpleGraph() {
        Graph result = newEmptyGraph();
        for (Node node : getAllNodes()) {
            result.addNode(node.getId());
        }
        for (Edge edge : getAllEdges()) {
            if (!edge.isSelfLoop() && !result.existsEdge(edge.from().getId(), edge.to().getId())) {
                result.addEdge(edge.from().getId(), edge.to().getId(), edge.getWeight());
            }
        }
        return result;
    }

    public int[][] toAdjMatrix() {
        List<Node> nodes = sortedNodes();
        int[][] matrix = new int[nodes.size()][nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            for (Edge edge : getOutEdges(nodes.get(i))) {
                int j = nodes.indexOf(edge.to());
                matrix[i][j]++;
            }
        }
        return matrix;
    }

    public Graph getReverse() {
        Graph result = newEmptyGraph();
        for (Node node : getAllNodes()) {
            result.addNode(node.getId());
        }
        for (Edge edge : getAllEdges()) {
            result.addEdge(edge.to().getId(), edge.from().getId(), edge.getWeight());
        }
        return result;
    }

    public Graph getTransitiveClosure() {
        Graph result = newEmptyGraph();
        for (Node node : getAllNodes()) {
            result.addNode(node.getId());
            Set<Node> seen = new HashSet<>();
            Queue<Node> queue = new ArrayDeque<>(getSuccessors(node));
            while (!queue.isEmpty()) {
                Node current = queue.remove();
                if (current.equals(node) || !seen.add(current)) {
                    continue;
                }
                if (!result.existsEdge(node.getId(), current.getId())) {
                    result.addEdge(node.getId(), current.getId());
                }
                queue.addAll(getSuccessors(current));
            }
        }
        return result;
    }

    public Graph copy() {
        Graph result = newEmptyGraph();
        for (Node node : getAllNodes()) {
            result.addNode(node.getId());
        }
        for (Edge edge : getAllEdges()) {
            result.addEdge(edge.from().getId(), edge.to().getId(), edge.getWeight());
        }
        return result;
    }

    protected Graph newEmptyGraph() {
        return new Graph();
    }

    public int[] toSuccessorArray() {
        List<Integer> values = new ArrayList<>();
        for (Node node : sortedNodes()) {
            List<Node> successors = getSuccessorsMulti(node);
            successors.sort(Comparator.naturalOrder());
            for (Node successor : successors) {
                values.add(successor.getId());
            }
            values.add(0);
        }
        return values.stream().mapToInt(Integer::intValue).toArray();
    }

    public List<Node> getDFS() {
        return traverse(false, null);
    }

    public List<Node> getBFS() {
        return traverse(true, null);
    }

    public List<Node> getDFS(Node start) {
        return traverse(false, start);
    }

    public List<Node> getBFS(Node start) {
        return traverse(true, start);
    }

    public List<Node> getDFS(int start) {
        return getDFS(getNode(start));
    }

    public List<Node> getBFS(int start) {
        return getBFS(getNode(start));
    }

    public List<Node> getDFSWithVisitInfo(Map<Node, NodeVisitInfo> nodeVisit,
            Map<Edge, EdgeVisitType> edgeVisit) {
        return getDFSWithVisitInfo(smallestNodeId(), nodeVisit, edgeVisit);
    }

    public List<Node> getDFSWithVisitInfo(int start, Map<Node, NodeVisitInfo> nodeVisit,
            Map<Edge, EdgeVisitType> edgeVisit) {
        return getDFSWithVisitInfo(getNode(start), nodeVisit, edgeVisit);
    }

    public List<Node> getDFSWithVisitInfo(Node start, Map<Node, NodeVisitInfo> nodeVisit,
            Map<Edge, EdgeVisitType> edgeVisit) {
        List<Node> order = new ArrayList<>();
        if (nodeVisit == null || edgeVisit == null) {
            return order;
        }
        nodeVisit.clear();
        edgeVisit.clear();
        for (Node node : sortedNodes()) {
            nodeVisit.put(node, new NodeVisitInfo(NodeColor.WHITE, null, null, null));
        }
        NodeVisitInfo.resetTime();
        if (start != null && nodeVisit.containsKey(start)) {
            visitDepthFirst(start, nodeVisit, edgeVisit, order);
        }
        for (Node node : sortedNodes()) {
            if (nodeVisit.get(node).getColor() == NodeColor.WHITE) {
                visitDepthFirst(node, nodeVisit, edgeVisit, order);
            }
        }
        return order;
    }

    private void visitDepthFirst(Node node, Map<Node, NodeVisitInfo> nodeVisit,
            Map<Edge, EdgeVisitType> edgeVisit, List<Node> order) {
        NodeVisitInfo info = nodeVisit.get(node);
        info.setColor(NodeColor.GRAY);
        info.setDiscovery(NodeVisitInfo.incrementTime());
        order.add(node);
        List<Edge> edges = getOutEdges(node);
        edges.sort(Comparator.naturalOrder());
        for (Edge edge : edges) {
            NodeVisitInfo target = nodeVisit.get(edge.to());
            if (target.getColor() == NodeColor.WHITE) {
                edgeVisit.put(edge, EdgeVisitType.TREE);
                target.setPredecessor(node);
                visitDepthFirst(edge.to(), nodeVisit, edgeVisit, order);
            } else if (target.getColor() == NodeColor.GRAY) {
                edgeVisit.put(edge, EdgeVisitType.BACKWARD);
            } else if (target.getDiscovery() > info.getDiscovery()) {
                edgeVisit.put(edge, EdgeVisitType.FORWARD);
            } else {
                edgeVisit.put(edge, EdgeVisitType.CROSS);
            }
        }
        info.setColor(NodeColor.BLACK);
        info.setFinished(NodeVisitInfo.incrementTime());
    }

    private List<Node> traverse(boolean breadthFirst, Node start) {
        List<Node> result = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        List<Node> roots = sortedNodes();
        if (start != null && holdsNode(start)) {
            roots.remove(start);
            roots.add(0, start);
        }
        for (Node root : roots) {
            if (visited.contains(root)) {
                continue;
            }
            ArrayDeque<Node> pending = new ArrayDeque<>();
            pending.add(root);
            while (!pending.isEmpty()) {
                Node current = breadthFirst ? pending.remove() : pending.removeLast();
                if (!visited.add(current)) {
                    continue;
                }
                result.add(current);
                List<Node> successors = getSuccessors(current);
                successors.sort(Comparator.naturalOrder());
                if (breadthFirst) {
                    pending.addAll(successors);
                } else {
                    for (int i = successors.size() - 1; i >= 0; i--) {
                        pending.addLast(successors.get(i));
                    }
                }
            }
        }
        return result;
    }

    public String toDotString() {
        StringBuilder result = new StringBuilder("digraph {\n\trankdir=LR\n");
        for (Node node : sortedNodes()) {
            List<Edge> edges = getOutEdges(node);
            edges.sort(Comparator.comparingInt(e -> e.to().getId()));
            if (edges.isEmpty() && getInEdges(node).isEmpty()) {
                result.append("\t").append(node.getId()).append("\n");
            }
            for (Edge edge : edges) {
                result.append("\t").append(edge.from().getId()).append(" -> ")
                        .append(edge.to().getId());
                if (edge.isWeighted()) {
                    result.append(" [label=").append(edge.getWeight())
                            .append(", len=").append(edge.getWeight()).append("]");
                }
                result.append("\n");
            }
        }
        return result.append("}\n").toString();
    }

    public void toDotFile(String name) {
        toDotFile(name, ".gv");
    }

    public void toDotFile(String name, String extension) {
        try {
            Files.writeString(Path.of(name + extension), toDotString());
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to write DOT file", exception);
        }
    }

    public static Graph fromDotFile(String name) {
        return fromDotFile(name, ".gv");
    }

    public static Graph fromDotFile(String name, String extension) {
        Path path = Path.of(name.endsWith(extension) ? name : name + extension);
        try {
            List<String> lines = Files.readAllLines(path);
            boolean undirected = lines.stream().map(String::trim).anyMatch(line -> line.startsWith("graph "));
            Graph graph = undirected ? new UndirectedGraph() : new Graph();
            Pattern edgePattern = Pattern.compile("(-?\\d+)\\s*(->|--)\\s*(-?\\d+)(?:\\s*\\[label=(-?\\d+).*\\])?.*");
            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty() || line.startsWith("#") || line.equals("}")
                        || line.startsWith("digraph") || line.startsWith("graph")
                        || line.startsWith("rankdir")) {
                    continue;
                }
                line = line.replaceAll(";$", "").trim();
                Matcher matcher = edgePattern.matcher(line);
                if (matcher.matches()) {
                    Integer weight = matcher.group(4) == null ? null : Integer.valueOf(matcher.group(4));
                    graph.addEdge(Integer.parseInt(matcher.group(1)),
                            Integer.parseInt(matcher.group(3)), weight);
                } else if (line.matches("\\d+")) {
                    graph.addNode(Integer.parseInt(line));
                }
            }
            return graph;
        } catch (IOException | RuntimeException exception) {
            return null;
        }
    }

    private List<Node> sortedNodes() {
        List<Node> result = getAllNodes();
        result.sort(Comparator.naturalOrder());
        return result;
    }
}
