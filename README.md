In this project we were asked to make a class that implements weighted graphs and number of algorithms on it. Algorithms including: finding the shortest path of two given nodes in the graph, finding the shortest distance between them, checking if a given weighted graph is connected and saving it to a file as well as loading a weighted graph from a file.

Implementaion of the weighted graph:
Main data structure used for implementation is hashmap. For it's unique ability to return a value of a given key at time complexity of O(1).

The graph has an inner class, NodeInfo which implements the weighted graph's nodes. Each edge is saved in the two nodes connected by it, using a hashmap for each node which saves for it what nodes it is connected to. The weights of the connections are stored in a hashmap in the weighted graph itself. Inorder to create a unique key for each edge to store it's weight I decided to make the key as a string, which is built in the following way, taking the smaller node's key as the first part of the string followed by the string " | ", ends with the bigger node's key 

In this project we were asked to make a class that implements weighted graphs and number of algorithms on it. Algorithms including: finding the shortest path of two given nodes in the graph, finding the shortest distance between them, checking if a given weighted graph is connected and saving it to a file as well as loading a weighted graph from a file.

Implementaion of the weighted graph: Main data structure used for implementation is hashmap. For it's unique ability to return a value of a given key at time complexity of O(1).

The graph has an inner class, NodeInfo which implements the weighted graph's nodes. Each edge is saved in the two nodes connected by it, using a hashmap for each node which saves for it what nodes it is connected to. The weights of the connections are stored in a hashmap in the weighted graph itself. Inorder to create a unique key for each edge to store it's weight I decided to make the key as a string, which is built in the following way, taking the smaller node's key as the first part of the string followed by the string " | ", ends with the bigger node's key (The nodes with keys 2 and 1 will get the key "1 | 2") and edge's weight as value. The nodes themselves are stored in a hashmap with their unique key as key.

The weighted graph supports the following functions:

getNode - Returns a node with given key.

hasEdge - Returns true if two nodes, given their keys have an edge.

getEdge - Returns the edge's weight of 2 given nodes by their keys.

addNode - Creates a new node with a given key and saves it in the graph.

connect - Connects an edge between two nodes, given their keys and weight of the edge.

getV - Returns a collection containing the nodes of the graph, or with a given key, will return a collection with the node's neighbors.

removeNode - Removes a node with given key.

removeEdge - Removes an edge between two nodes with given keys.

nodesize - Returns how many nodes are in the graph.

edgesize - Return how many edges have been connected.

getMC - Returns how many changes have been made to the graph.

equals - Returns true if a given graph has exactly the same properties as the graph.

Impelementation of the graph algorithms: Main algorithms that were used are: BFS and Dijkstra's Algorithms. BFS - An algorithm that finds the shortest path from 2 given nodes in a graph. It is used in my project for checking if a graph is connected. Basic idea: Given starting node key, src and destantion key, dest, using a queue and a marking of if a node was visited. For the first node adding it's neighbors to the queue and marking them as visited. For any other nodes until the queue is empty, removing them from the queue, adding their unvisited neighbours to the queue and marking them as visited until it gets to the dest node. Used in isconnected by counting how many nodes could be reached from the first node of the graph. The running time of BFS is O(n+e), n = number of nodes e = number of edges. O(n+e) because the worst case is when the graph is connceted. It will go through the whole graph -> n operations. it goes through each node and check it's neighbours that means 2e neighbours (1 check from each side). Over all O(n+e).

Dijkstra's Algorithm - An algorithm that finds the shortest path from 2 given nodes in a weighted graph. Was used in my project for finding the shortest path and distance between 2 nodes. First given starting node key, src and destantion key, dest. Using a min heap, a marking for each node if it was visited or got out of the min heap, marking for the distance and Adding the first node to min heap setting it's distance as 0. For the first node, going through all of it's neighbors marking them as visited setting their distance to be the weight of their edge with the first node, adding them to the min heap which places them by their distance and marking them as visited, "V" marking the first node as done "B". For any other node removed from the min heap, until the min heap is empty, marking it as done, going through it's neighbors, If the neighbor is done (marked by "B" in their Info) skips it. If the neighbor is marked as visited "V" checking if the distance from the src through the node is shorter than the neighbor's existing distance, if so it updates the distance, Updating the min heap to reset it's new position. If the neighbor is the destination node, it checks the same as a node marked by "V" but without adding it to the min heap. At the end dest's Info will contain the minimal distance from the starting node.

The running time of the algorithm is O(n+e), when n = number of node and e = number of edges. O(n+e) because if the graph is connceted it will go through the whole graph -> n operations. It goes through each node and check it's neighbours that means 2e neighbours (1 check from each side). Over all O(n+e).

The algorithms that are implemented in my project are: Containig weighted graph g that the algorithms will work on.

init - Given a graph, updates g to the new graph.

getgraph - Returns the graph g.

copy - Return a deep copy of g.

isconnected - Return true if g is connected using a version of BFS algorithm that counts how many nodes are connected to the first node in the graph. Returns true if reaches the number of nodes in the graph.

shortestPathDist - Using Dijkstra's Algorithm, returns the shortest distance of two nodes, given their keys.

shortestPath - Using Dijkstra's Algorithm, returns list containing the shortest path of two nodes, given their keys. Using a hashmap to save what is the origin of each node to create that path.

save - Given a path, saves g in a file on that path. Returns true if saving was successful, false otherwise.

load - Given a path to a file containing a weighted graph, loads it and initiats it on the algorithm calss. Returns true if the loading was successful false otherwise.
