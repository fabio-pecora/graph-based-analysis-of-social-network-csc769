import java.util.*;

class Graph {
    private int V; // Number of vertices
    private LinkedList<Edge>[] adjList; // Adjacency list representation

    // Edge class to store destination and weight
    static class Edge {
        int dest, weight;

        Edge(int d, int w) {
            this.dest = d;
            this.weight = w;
        }
    }

    // Constructor (Fixed Type Casting Issue)
    @SuppressWarnings("unchecked")
    public Graph(int vertices) {
        this.V = vertices;
        adjList = (LinkedList<Edge>[]) new LinkedList[vertices]; // Typecast to avoid warning
        for (int i = 0; i < vertices; i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    // Add an edge to the graph (undirected)
    public void addEdge(int src, int dest, int weight) {
        adjList[src].add(new Edge(dest, weight));
        adjList[dest].add(new Edge(src, weight)); // Since the graph is undirected
    }

    // Floyd-Warshall Algorithm
    public void floydWarshall() {
        double[][] lambda = new double[V][V]; // λ matrix (shortest paths)
        int[][] parent = new int[V][V]; // P matrix (predecessors)

        // Initialize λ with adjacency matrix values and ∞ where no direct edge exists
        for (int i = 0; i < V; i++) {
            Arrays.fill(lambda[i], Double.POSITIVE_INFINITY);
            Arrays.fill(parent[i], -1);
            lambda[i][i] = 0; // Distance to itself is 0
        }

        // Populate λ with given edges
        for (int i = 0; i < V; i++) {
            for (Edge edge : adjList[i]) {
                lambda[i][edge.dest] = edge.weight;
                parent[i][edge.dest] = i;
            }
        }

        // Floyd-Warshall Dynamic Programming Update
        for (int k = 0; k < V; k++) { // Intermediate vertex
            for (int i = 0; i < V; i++) { // Source
                for (int j = 0; j < V; j++) { // Destination
                    if (lambda[i][k] + lambda[k][j] < lambda[i][j]) {
                        lambda[i][j] = lambda[i][k] + lambda[k][j];
                        parent[i][j] = parent[k][j];
                    }
                }
            }
        }

        // Compute Closeness Centrality
        double[] closenessCentrality = new double[V];
        for (int i = 0; i < V; i++) {
            double sumShortestPaths = 0;
            for (int j = 0; j < V; j++) {
                if (lambda[i][j] != Double.POSITIVE_INFINITY) {
                    sumShortestPaths += lambda[i][j];
                }
            }
            closenessCentrality[i] = (V - 1) / sumShortestPaths;
        }

        // Print Results
        System.out.println("Closeness Centrality:");
        for (int i = 0; i < V; i++) {
            System.out.printf("Node %d: %.6f\n", i, closenessCentrality[i]);
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph(8); // 8 nodes in the graph

        // Add edges based on the adjacency matrix provided
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 1);
        graph.addEdge(0, 3, 2);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(1, 4, 4);
        graph.addEdge(2, 3, 1);
        graph.addEdge(2, 5, 2);
        graph.addEdge(3, 6, 2);
        graph.addEdge(4, 5, 4);
        graph.addEdge(4, 7, 1);
        graph.addEdge(5, 6, 4);
        graph.addEdge(5, 7, 5);
        graph.addEdge(6, 7, 3);

        graph.floydWarshall(); // Compute shortest paths and centrality
    }
}
