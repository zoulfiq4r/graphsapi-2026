   package m1graphs2026;

   /**
    * Represents one node belonging to a graph.
    *
    * <p>The node id is unique only inside its graph. Therefore, two nodes with
    * the same id are equal only when they also belong to the same graph object.</p>
    */
   public class Node implements Comparable<Node> {

      /** The identifier of this node. It cannot change after construction. */
      private final int id;

      /** The optional name of this node. */
      private final String name;

      /** The graph to which this node belongs. */
      private final Graph graph;

      /**
       * Creates an unnamed node with the given id and graph holder.
       *
       * @param id the identifier of the node
       * @param graph the graph that holds the node
       */
      public Node(int id, Graph graph) {
         // Reuse the main constructor and leave the name empty.
         this(id, null, graph);
      }
      //——————

      /**
       * Creates a node with the given id, name, and graph holder.
       *
       * @param id the identifier of the node
       * @param name the name of the node, or null when it has no name
       * @param graph the graph that holds the node
       */
      public Node(int id, String name, Graph graph) {
         this.id = id;
         this.name = name;
         this.graph = graph;
      }
      //——————

      /**
       * Returns the identifier of this node.
       *
       * @return this node's identifier
       */
      public int getId() {
         return id;
      }
      //——————

      /**
       * Returns the optional name of this node.
       *
       * @return this node's name, or null when no name was given
       */
      public String getName() {
         return name;
      }
      //——————

      /**
       * Returns the graph that holds this node.
       *
       * @return this node's graph holder
       */
      public Graph getGraph() {
         return graph;
      }
      //——————

      /**
       * Tests whether another object represents the same node.
       *
       * <p>The graph is compared with {@code ==} because the specification
       * requires the same graph object, not merely an equal graph.</p>
       *
       * @param other the object to compare with this node
       * @return true when the object is a Node with the same id and graph holder
       */
      @Override
      public boolean equals(Object other) {
         // An object is always equal to itself.
         if (this == other) {
            return true;
         }
         // A null object or a different class cannot represent this node.
         if (other == null || getClass() != other.getClass()) {
            return false;
         }
         Node node = (Node) other;

         // The graph comparison uses == because graph identity matters here.
         return id == node.id && graph == node.graph;
      }
      //——————

      /**
       * Returns a hash code consistent with {@link #equals(Object)}.
       *
       * <p>The id alone is safe because equal nodes always have the same id.
       * Nodes from different graphs may have the same hash code, which is
       * allowed because hash codes do not have to be unique.</p>
       *
       * @return a hash code for this node
       */
      @Override
      public int hashCode() {
         // Equal nodes have the same id, so the id is enough for this hash.
         return Integer.hashCode(id);
      }
      //——————

      /**
       * Compares this node with another node by increasing id.
       *
       * @param other the node to compare with this node
       * @return a negative value, zero, or a positive value when this node's id
       *         is smaller than, equal to, or greater than the other id
       */
      @Override
      public int compareTo(Node other) {
         // Integer.compare avoids overflow when comparing integer ids.
         return Integer.compare(id, other.id);
      }
      //——————
   }