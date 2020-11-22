package ex1.src;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


public class WGraph_DS implements weighted_graph, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<Integer, node_info> V;
	private HashMap<String, Double> w;
	private int MC;


	public WGraph_DS() {
		V = new HashMap<Integer, node_info>();
		w = new HashMap<String, Double>();
		MC = 0;
	}

	public WGraph_DS(weighted_graph g )
	{
		this();
		Iterator<node_info> it = g.getV().iterator();
		while(it.hasNext() && it!=null)
		{
			//Builds a new node in the graph with the same key.
			node_info n = it.next();
			addNode(n.getKey());
		}
		it = g.getV().iterator();
		while(it.hasNext())
		{
			node_info n = it.next();
			Iterator<node_info> i = g.getV(n.getKey()).iterator();
			while(i.hasNext())
			{
				int key = i.next().getKey();
				connect(n.getKey(), key, g.getEdge(n.getKey(), key));
			}
		}
	}

	@Override
	public node_info getNode(int key) {
		return V.get(key);
	}

	@Override
	public boolean hasEdge(int node1, int node2) {
		if(!V.containsKey(node1) || !V.containsKey(node2))
			return false;
		return ((NodeInfo)V.get(node1)).HasNi(node2);
	}

	@Override
	public double getEdge(int node1, int node2) {
		if(!hasEdge(node1, node2))
			return -1;
		int mi = Math.min(node1, node2);
		int ma = Math.max(node1, node2);
		String s = mi+"|"+ma;
		return w.get(s);
	}

	@Override
	public void addNode(int key) {
		if(V.containsKey(key))
			return;
		V.put(key, new NodeInfo(key));
		MC++;
	}

	@Override
	public void connect(int node1, int node2, double w) {
		if(node1 == node2 || !V.containsKey(node1) || !V.containsKey(node2))
			return;
		int mi = Math.min(node1, node2);
		int ma = Math.max(node1, node2);
		String s = mi+"|"+ma;
		if(hasEdge(node1, node2))
		{
			if(w==this.w.get(s))
				return;
			this.w.remove(s);
			this.w.put(s, w);
			MC++;
			return;
		}
		else
		{
			node_info n1 = V.get(node1);
			node_info n2 = V.get(node2);
			this.w.put(s, w);
			((NodeInfo)n1).addNi(n2);
			((NodeInfo)n2).addNi(n1);
			MC++;
		}
	}

	@Override
	public Collection<node_info> getV() {
		return V.values();
	}

	@Override
	public Collection<node_info> getV(int node_id) {
		if(!V.containsKey(node_id))
			return (new HashMap<Integer, node_info>()).values();
		NodeInfo n = (NodeInfo)V.get(node_id);
		return n.GetNi().values();
	}

	@Override
	public node_info removeNode(int key) {
		//Return an null if it is not in the graph.
		if(!V.containsKey(key))
			return null;
		Iterator<node_info> it = getV(key).iterator();
		while(it.hasNext() && it!=null)
		{
			NodeInfo n = (NodeInfo)it.next();
			n.RemoveNi(key);
			int mi = Math.min(key, n.getKey());
			int ma = Math.max(key, n.getKey());
			String s = mi+"|"+ma;
			w.remove(s);
		}
		((NodeInfo)V.get(key)).ResetNi();
		V.remove(key);
		MC++;
		return V.get(key);
	}

	@Override
	public void removeEdge(int node1, int node2) {
		if(node1 == node2 || !V.containsKey(node1) || !V.containsKey(node2) || !hasEdge(node1, node2))
			return;
		((NodeInfo)V.get(node1)).RemoveNi(node2);
		((NodeInfo)V.get(node2)).RemoveNi(node1);
		int mi = Math.min(node1, node2);
		int ma = Math.max(node1, node2);
		String s = mi+"|"+ma;
		w.remove(s);
		MC++;
	}

	@Override
	public int nodeSize() {
		return V.size();
	}

	@Override
	public int edgeSize() {
		return w.size();
	}

	@Override
	public int getMC() {
		return MC;
	}

	public String toString()
	{
		return "Number of nodes: "+ V.size()+" | Number of edges: "+w.size()+"| MC: " + MC;
	}

	//To check if a given graph is equal to this graph.
	@Override
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof weighted_graph))
			return false;
		weighted_graph g = (weighted_graph)o;
		if(V.size() != g.nodeSize())
			return false;
		if(edgeSize() != g.edgeSize())
			return false;
		Iterator<Integer> it = V.keySet().iterator();
		while(it.hasNext())
		{
			int k = it.next();
			if(getNode(k).equals(null))
				return false;

			//Checks that the node has the same amount of neighbors.
			if(getV(k).size() != g.getV(k).size())
				return false;

			//Checks that the neighbors are the same (that the node in g2 has the neighbors of the node in g).
			Iterator<node_info> it1 = getV(k).iterator();
			while(it1.hasNext())
			{
				int ke = it1.next().getKey();
				//Check that it has the neighbor.
				if(!g.hasEdge(k, ke) || getEdge(k, ke)!=g.getEdge(k, ke))
					return false;
			}
		}
		return true;
	}

	////////////////////////////////////////////////////////



	class NodeInfo implements node_info,Comparable<node_info>, Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int key;
		private String Info;
		private double Tag;
		private HashMap<Integer, node_info> Ni;

		public NodeInfo() {
			key=0;
			Info = "";
			Tag = -1;
			Ni = new HashMap<Integer, node_info>();
		}
		public NodeInfo(int k)
		{
			this();
			key = k;
		}
		//Get node's neighbors
		public HashMap<Integer, node_info> GetNi()
		{
			return Ni;
		}

		public void addNi(node_info n)
		{
			Ni.put(n.getKey(), n);
		}

		public void ResetNi()
		{
			Ni = new HashMap<Integer, node_info>();
		}
		
		public boolean HasNi(int k)
		{
			return Ni.containsKey(k);
		}

		public void RemoveNi(int k)
		{
			Ni.remove(k);
		}
		@Override
		public int getKey() {
			return key;
		}

		@Override
		public String getInfo() {
			return Info;
		}

		@Override
		public void setInfo(String s) {
			Info = s;

		}

		@Override
		public double getTag() {
			return Tag;
		}

		@Override
		public void setTag(double t) {
			Tag = t;

		}

		@Override
		public String toString() {
			if(Ni==null)
				return "Key: "+key+" | Info: " + Info+" | Tag: "+Tag;
			return "Key: "+key+" | Info: " + Info+" | Tag: "+Tag +" | Neighbors: "+Ni.size();
		}

		//CompareTo so I can make a priority queue
		@Override
		public int compareTo(node_info o) {
			double d = this.Tag-o.getTag();
			if(d<0)
				return -1;
			if(d==0)
				return 0;
			return 1;
		}

	}
	//////////////////////////////////////////////////
}
