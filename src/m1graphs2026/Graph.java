package m1graphs2026;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Graph {
  
  /**
   * Represents a directed graph using adjacency lists.
   * Each node maps to a list of its outgoing edges.
   */
  private Map<Node, List<Edge>> ael; // TreeMap<Node, List<Edge>>
  protected int NbNode;
  protected int NbEdge;



  /**
   * Empty Constructor for the class Graph, 
   * constructs an empty graph with an adjacent edge lists structure.
   */
  public Graph() {
    this.ael = new TreeMap<>(Comparator.comparingInt(Node::getId));
    this.NbEdge = 0;
  }




}