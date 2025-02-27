import java.util.*;

class Graph {
    private int V;
    private LinkedList<Edge>[] adjList;
    private String[] nodeNames;

    static class Edge {
        int dest, weight;

        Edge(int d, int w) {
            this.dest = d;
            this.weight = w;
        }
    }

    public Graph(int vertices, String[] names) {
        this.V = vertices;
        this.nodeNames = names;
        adjList = (LinkedList<Edge>[]) new LinkedList[vertices];
        for (int i = 0; i < vertices; i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    public void addEdge(int src, int dest, int weight) {
        adjList[src].add(new Edge(dest, weight));
        adjList[dest].add(new Edge(src, weight));
    }

    public void floydWarshall() {
        double[][] lambda = new double[V][V];
        int[][] parent = new int[V][V];

        for (int i = 0; i < V; i++) {
            Arrays.fill(lambda[i], Double.POSITIVE_INFINITY);
            Arrays.fill(parent[i], -1);
            lambda[i][i] = 0;
        }

        for (int i = 0; i < V; i++) {
            for (Edge edge : adjList[i]) {
                lambda[i][edge.dest] = edge.weight;
                parent[i][edge.dest] = i;
            }
        }

        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (lambda[i][k] + lambda[k][j] < lambda[i][j]) {
                        lambda[i][j] = lambda[i][k] + lambda[k][j];
                        parent[i][j] = parent[k][j];
                    }
                }
            }
        }

        System.out.println("Final Shortest Path Matrix");
        System.out.print("\t");
        for (int i = 0; i < V; i++) {
            System.out.print(nodeNames[i] + "\t");
        }
        System.out.println();

        for (int i = 0; i < V; i++) {
            System.out.print(nodeNames[i] + "\t");
            for (int j = 0; j < V; j++) {
                if (lambda[i][j] == Double.POSITIVE_INFINITY) {
                    System.out.print("INF\t");
                } else {
                    System.out.printf("%.0f\t", lambda[i][j]);
                }
            }
            System.out.println();
        }

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

        System.out.println("\nCloseness Centrality:");
        for (int i = 0; i < V; i++) {
            System.out.printf("Node %s: %.6f\n", nodeNames[i], closenessCentrality[i]);
        }
    }

    public static void main(String[] args) {

        String[] nodeNames = { "s", "1", "2", "3", "4", "5", "6", "t" };
        Graph graph = new Graph(8, nodeNames);

        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 1);
        graph.addEdge(0, 3, 2);
        graph.addEdge(1, 4, 4);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 5, 2);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 6, 2);
        graph.addEdge(4, 5, 4);
        graph.addEdge(4, 7, 1);
        graph.addEdge(5, 6, 4);
        graph.addEdge(5, 7, 5);
        graph.addEdge(6, 7, 3);

        graph.floydWarshall();
    }
}
