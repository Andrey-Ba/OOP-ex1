package ex1.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ex1.src.*;


class GraphTest {
	static Random _rnd = new Random(42);

	public static int nextRnd(int min, int max) {
		double v = nextRnd(0.0+min, (double)max);
		int ans = (int)v;
		return ans;
	}
	public static double nextRnd(double min, double max) {
		double d = _rnd.nextDouble();
		double dx = max-min;
		double ans = d*dx+min;
		return ans;
	}

	@Test
	void Basic_Add_Remove_Nodes_and_edge_Tests()
	{
		weighted_graph g = new WGraph_DS();
		g.addNode(0);//1
		g.addNode(0);//1
		g.addNode(1);//2
		g.addNode(2);//3
		g.addNode(3);//4
		g.addNode(4);//5

		assertEquals(5, g.nodeSize());

		//Checks if none existing node is null
		assertNull(g.getNode(10));
		
		assertEquals(1, g.getNode(1).getKey());
		
		g.connect(1, 1, 0.3);//0
		g.connect(0, 1, 0.1);//1
		assertEquals(0.1, g.getEdge(0, 1));
		
		g.connect(1, 0, 0.3);//1
		assertEquals(0.3, g.getEdge(0, 1));//Check if the weight changed.
		
		g.connect(1, 0, 1.4);//1
		assertEquals(1.4, g.getEdge(1,0));
		
		g.connect(1, 0, 5.6);//1
		assertEquals(5.6, g.getEdge(1,0));
		g.connect(3, 2, 0.5);//2
		g.connect(-1, 1, 100);//2
		g.connect(-1, 5, 3.2);//2

		assertEquals(2, g.edgeSize());
		
		//Check for hasEdge
		assertTrue(g.hasEdge(1, 0));
		assertFalse(g.hasEdge(-1, 1));
		assertFalse(g.hasEdge(1, 1));
		assertFalse(g.hasEdge(1, 4));
		
		g.removeEdge(-1, 0);//2
		g.removeEdge(0, 1);//2-1=1 edges
		g.removeEdge(2, 3);//1-1=0 edges
		g.removeEdge(2, 3);//0

		assertEquals(0, g.edgeSize());
	}

	@Test
	void NodeRemovalTest()
	{
		weighted_graph g = new WGraph_DS();
		//10 nodes
		for(int i=0;i<10;i++)
			g.addNode(i);

		g.connect(1, 2, 1.2);//1 edges
		g.connect(2, 3, 1.3);//2 edges
		g.connect(2, 5, 1.2);//3 edges
		g.connect(2, 9, 3);//4 edges
		g.connect(1, 6, 3);//5 edges and 4 edges connected to 2
		g.connect(5, 3, 1.1);//6 edges and 4 edges connected to 2

		g.removeNode(2); //2 edges and 9 nodes
		g.removeNode(10);//Non existing edge -> 2 edges and 9 nodes

		assertEquals(9, g.nodeSize());
		assertEquals(2, g.edgeSize());
	}

	@Test
	void CopyTest()
	{
		weighted_graph g = new WGraph_DS();
		weighted_graph_algorithms ga = new WGraph_Algo(g);
		weighted_graph g2;
		for(int i=0;i<20;i++)
			g.addNode(i);
		while(g.edgeSize()<100)
		{
			int a = nextRnd(0,20);
			int b = nextRnd(0,20);
			double w = nextRnd(0,10.0);
			g.connect(a, b, w);
		}
		g2 = ga.copy();
		
		//Check that the 2 graphs are not the same one.
		assertNotSame(g, g2);
		
		//Check that the two graphs has the same number of nodes and edges.
		assertEquals(g.nodeSize(), g2.nodeSize());
		assertEquals(g.edgeSize(), g2.edgeSize());
		
		//Check if the changes to the node are the same as 
		//the amount of nodes addition and edges connections.
		//Which is n+e.
		assertEquals(g.edgeSize()+g.nodeSize(),g2.getMC());
		
		//Check that they have the same shape.
		assertEquals(g, g2);
	}

	@Test
	void ConnectedTest()
	{
		weighted_graph g = new WGraph_DS();
		weighted_graph_algorithms ga = new WGraph_Algo(g);
		for(int i=0;i<20;i++)
			g.addNode(i);
		//Checks if a graph with 20 nodes with 0 connections is connected.
		assertFalse(ga.isConnected());
		for(int i=1;i<20;i++)
			g.connect(0, i, 0.1);
		//Checks if a graph where all nodes connected to node 0 is connected.
		assertTrue(ga.isConnected());

		while(g.edgeSize()<100)
		{
			int a = nextRnd(0,20);
			int b = nextRnd(0,20);
			double w = nextRnd(0,10.0);
			g.connect(a, b, w);
		}
		//Checks if the graph is connceted.
		assertTrue(ga.isConnected());

		g.removeNode(0);
		g.addNode(0);
		//Check if after reseting a node will the graph remain connected.
		assertFalse(ga.isConnected());
	}

	@Test
	void ShortestPathdistandpathTest()
	{
		weighted_graph g = new WGraph_DS();
		weighted_graph_algorithms ga = new WGraph_Algo(g);
		for(int i=0;i<20;i++)
			g.addNode(i);
		//Check if the distance of two unconnected nodes is -1
		assertEquals(-1, ga.shortestPathDist(0, 14));
		
		g.connect(0, 14, 0.1);
		assertEquals(0.1, ga.shortestPathDist(0, 14));
		assertEquals(0.1, ga.shortestPathDist(14, 0));
		while(g.edgeSize()<100)
		{
			int a = nextRnd(0,20);
			int b = nextRnd(0,20);
			double w = nextRnd(0,10.0);
			if(!(a==14 && b==0) && !(b==14 && a==0) && !g.hasEdge(a, b) &&a!=b)
				g.connect(a, b, w);
		}
		assertEquals(0.1, ga.shortestPathDist(0, 14));
		assertEquals(0.1, ga.shortestPathDist(14, 0));

		//Check if two random nodes have the same distance.
		assertEquals(ga.shortestPathDist(2, 5), ga.shortestPathDist(5, 2));
	}

	@Test
	public void SaveandLoadTest()
	{
		weighted_graph g = new WGraph_DS();
		weighted_graph_algorithms ga = new WGraph_Algo(g);
		
		//Create a random graph with 100 nodes and 2000 edges.
		for(int i =0;i<100;i++)
			g.addNode(i);
		//Edge that will be used later.
		g.connect(1, 4, 2);
		
		while(g.edgeSize()<2000)
		{
			int a = nextRnd(0,101);
			int b = nextRnd(0,101);
			double w = nextRnd(0,10.0);
			g.connect(a, b, w);
		}
		//Save the file and load into different graph_algorithm.
		String file = "graph.obj";
		ga.save(file);
		weighted_graph g2 = new WGraph_DS();
		weighted_graph_algorithms ga2 = new WGraph_Algo(g);
		ga2.load(file);
		g2 = ga2.getGraph();
		
		//Check if the graph it got from the file is the same as the one it saved.
		assertEquals(g, g2);
		
		g2.removeEdge(1,4);
		//Check if after removing an edge the graphs become different.
		assertNotEquals(g, g2);
		
		//Loading the graph again.
		ga2.load(file);
		g2 = ga2.getGraph();
		
		assertEquals(g, g2);
		
		g2.removeNode(5);
		//Check if after removing a node it will be a different graph.
		assertNotEquals(g, g2);
	}
}
