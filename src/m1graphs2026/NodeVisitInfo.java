package m1graphs2026;

/**
 * DFS metadata for one node.
 */
public class NodeVisitInfo {
    private NodeColor color;
    private Node predecessor;
    private Integer discovery;
    private Integer finished;
    private static int time;

    public NodeVisitInfo(NodeColor color, Node predecessor, Integer discovery, Integer finished) {
        this.color = color;
        this.predecessor = predecessor;
        this.discovery = discovery;
        this.finished = finished;
    }

    public NodeColor getColor() {
        return color;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public Integer getDiscovery() {
        return discovery;
    }

    public Integer getFinished() {
        return finished;
    }

    public void setColor(NodeColor color) {
        this.color = color;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public void setDiscovery(Integer discovery) {
        this.discovery = discovery;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    public static void resetTime() {
        time = 0;
    }

    public static int getTime() {
        return time;
    }

    public static int incrementTime() {
        return ++time;
    }
}
