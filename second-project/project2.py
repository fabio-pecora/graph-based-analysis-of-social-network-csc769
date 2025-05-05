import numpy as np
import networkx as nx
from sklearn.cluster import KMeans
import matplotlib.pyplot as plt

# Step 1: Define the input graph

edges = [
    (0, 1), (0, 3), (0, 5), (1, 2), (1, 4), (2, 3), (3, 4), (4, 5),
    (4, 6), (5, 6), (6, 7), (6, 8), (7, 8), (8, 10), (8, 12),
    (9, 10), (9, 11), (9, 12), (10, 11), (11, 12), (10, 13), (13, 14), (13, 15)
]

G = nx.Graph()
G.add_edges_from(edges)
nodes = list(G.nodes)

# Step 2: Compute matrices
A = nx.to_numpy_array(G)            # Adjacency matrix
D = np.diag(np.sum(A, axis=1))      # Degree matrix
L = D - A                           # Laplacian matrix

print("Adjacency Matrix (A):\n", A)
print("\nLaplacian Matrix (L):\n", L)

# Step 3: Eigen decomposition
eigenvalues, eigenvectors = np.linalg.eigh(L)
print("\nFirst 5 Eigenvalues:\n", eigenvalues[:5])

# Use second and third smallest eigenvectors (skip eigenvector 0)
X = eigenvectors[:, 1:3]

# Step 4: KMeans Clustering
def spectral_kmeans(X, k):
    kmeans = KMeans(n_clusters=k, random_state=0)
    labels = kmeans.fit_predict(X)
    clusters = {i: [] for i in range(k)}
    for idx, label in enumerate(labels):
        clusters[label].append(idx)
    return labels, clusters

# Step 5: Run and Visualize Results
def draw_clusters(G, labels, k, title):
    plt.figure(figsize=(8, 5))
    pos = nx.spring_layout(G, seed=42)
    colors = plt.cm.tab10(labels)  # auto-coloring
    nx.draw(G, pos, node_color=colors, with_labels=True, node_size=600, font_weight='bold')
    plt.title(f"{title} (k = {k})")
    plt.show()

for k in [2, 3, 4]:
    print(f"\n--- Spectral Clustering (k={k}) ---")
    labels, clusters = spectral_kmeans(X, k)
    for cluster_id, nodes in clusters.items():
        print(f"Cluster {cluster_id + 1}: {sorted(nodes)}")
    draw_clusters(G, labels, k, "Spectral Clustering Result")
