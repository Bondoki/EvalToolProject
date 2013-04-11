package EvalToolProject.tools;


import java.util.*;


public class Graph {
  // Kante
  static class Edge {
    Node dest; // Zielknoten
    int cost; // Kantengewicht
	
    public Edge(Node n, int c) { dest = n; cost = c; }
    public Node getDestNode() { return dest; }
    public int getCost() { return cost; }
  }

  // Knoten
  static class Node implements Comparable {
 
      String label;
      ArrayList<Edge> adjList = new ArrayList<Edge>();
      Node pred = null;

      // Kostenwerte f�r A*-Suche
      int[] costs = new int[3];
      public static final int F_COST = 0;
      public static final int G_COST = 1;
      public static final int H_COST = 2;

     public Node(String s) { 
    	label = s; 
        for (int i = 0; i < 3; i++) costs[i] = 0;  	
    }
    
     public String toString() {
         return label + ":g=" + costs[G_COST] + ",f=" + costs[F_COST];
 }
	
   public String getLabel() { return label; }
    public void addEdge(Edge e) { adjList.add(e); }
		
    public Iterator<Edge> getEdges() {
      return adjList.iterator();
    }
	
    public Edge getEdgeTo(Node n) {
      for (Edge e : adjList) {
        if (e.dest.equals(n))
          return e;
      }
      return null;
    }
    
    public void setCost(int idx, int c) {
        costs[idx] = c;
}

public int getCost(int idx) {
        return costs[idx];
}

public int compareTo(Object o) {
        Node other = (Node) o;
        System.out.println("compareTo: " + this + "." + other);
        if (costs[F_COST] < other.costs[F_COST])
                return -1;
        else if (costs[F_COST] > other.costs[F_COST])
                return 1;
        else
                return 0;
}

    public void setPredecessor(Node n) {
        pred = n;
}

public Node getPredecessor() {
        return pred;
}

  }
  
  // Verzeichnis aller Knoten des Graphen  
  private HashMap<String, Node> nodeSet = 
    new HashMap<String, Node>();
	
  public Graph() { }

  public Node addNode(String label) 
      throws NodeAlreadyDefinedException {
    if (nodeSet.containsKey(label))
      throw new NodeAlreadyDefinedException();
    Node n = new Node(label);
    nodeSet.put(label, n);
    return n;
  }
    
  public Node getNode(String label) 
      throws NoSuchElementException {
    Node n = nodeSet.get(label);
    if (n == null)
      throw new NoSuchElementException();
    return n;
  }
    
  public void addEdge(String src, String dest, int cost) {
    Node srcNode = getNode(src);
    Node destNode = getNode(dest);
    srcNode.addEdge(new Edge(destNode, cost));
  }
}
