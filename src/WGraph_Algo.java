package ex1.src;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


public class WGraph_Algo implements weighted_graph_algorithms{

	private weighted_graph g;

	public WGraph_Algo() {
		g = new WGraph_DS();
	}

	public WGraph_Algo(weighted_graph g)
	{
		this.g = g;
	}
	@Override
	public void init(weighted_graph g) 
	{
		this.g = g;
	}

	@Override
	public weighted_graph getGraph() 
	{
		return g;
	}

	@Override
	public weighted_graph copy() 
	{
		return new WGraph_DS(g);
	}

	@Override
	public boolean isConnected() 
	{
		if(g.nodeSize()<=1)
			return true;
		if(g.edgeSize()<g.nodeSize()-1)
			return false;
		LinkedList<node_info> lst = new LinkedList<node_info>();
		Queue<Integer> q =new LinkedList<Integer>();
		q.add(g.getV().iterator().next().getKey());
		int count = 1;
		g.getNode(q.peek()).setInfo("V");
		lst.add(g.getNode(q.peek()));
		while(!q.isEmpty())
		{
			Iterator<node_info> it = g.getV(q.remove()).iterator();
			while(it.hasNext())
			{
				node_info node = it.next();
				if(!node.getInfo().equals("V"))
				{
					lst.add(node);
					int k = node.getKey();
					q.add(k);
					g.getNode(k).setInfo("V");
					count++;
					if(count == g.nodeSize())
					{
						reset(lst);
						return count==g.nodeSize();
					}
				}
			}
		}
		reset(lst);
		return count==g.nodeSize();
	}

	@Override
	public double shortestPathDist(int src, int dest) 
	{
		PriorityQueue<node_info> pq = new PriorityQueue<node_info>();
		//List to reset the used node's Tag and Info
		LinkedList<node_info> lst = new LinkedList<node_info>();
		lst.add(g.getNode(src));
		lst.add(g.getNode(dest));
		pq.add(g.getNode(src));
		g.getNode(src).setTag(0);
		//Mark dest with "E" 
		g.getNode(dest).setInfo("E");
		while(!pq.isEmpty())
		{
			node_info node = pq.remove();
			node.setInfo("B");
			Iterator<node_info> it = g.getV(node.getKey()).iterator();
			while(it.hasNext())
			{
				node_info n = it.next();
				if(n.getInfo().equals(""))
				{
					n.setInfo("V");
					n.setTag(node.getTag()+g.getEdge(n.getKey(), node.getKey()));
					pq.add(n);
					lst.add(n);
				}
				else if(n.getInfo().equals("V"))
				{
					double tag = node.getTag()+g.getEdge(n.getKey(), node.getKey());
					//Check if the new path is shorter than the older one.
					if(tag<n.getTag() || n.getTag()==-1.0)
					{
						n.setTag(tag);
						//Update the pq to the new value of tag
						//Takes O(n) to find the value and logn to remove it.
						pq.remove(n);
						//Add it with the new value at it's place.
						pq.add(n);
					}
				}
				else if(n.getInfo().equals("E"))
				{
					double tag = node.getTag()+g.getEdge(n.getKey(), node.getKey());
					//Check if the new path is shorter than the older one or haven't been set yet.
					if(tag<n.getTag() || n.getTag()==-1.0)
					{
						n.setTag(tag);
					}
				}
			}
		}
		double t = g.getNode(dest).getTag();
		reset(lst);
		return t;
	}

	@Override
	public List<node_info> shortestPath(int src, int dest) 
	{
		LinkedList<node_info> l = new LinkedList<node_info>();
		LinkedList<node_info> lst = new LinkedList<node_info>();
		PriorityQueue<node_info> pq = new PriorityQueue<node_info>();
		HashMap<Integer, Integer> source = new HashMap<Integer, Integer>();
		g.getNode(dest).setInfo("E");
		pq.add(g.getNode(src));
		pq.peek().setTag(0);
		source.put(src, src);
		lst.add(pq.peek());
		lst.add(g.getNode(dest));
		while(!pq.isEmpty())
		{
			node_info node = pq.remove();
			node.setInfo("B");
			Iterator<node_info> it = g.getV(node.getKey()).iterator();
			while(it.hasNext())
			{
				node_info n = it.next();
				if(n.getInfo().equals(""))
				{
					n.setInfo("V");
					n.setTag(node.getTag()+g.getEdge(n.getKey(), node.getKey()));
					pq.add(n);
					lst.add(n);
					source.put(n.getKey(), node.getKey());
				}
				else if(n.getInfo().equals("V"))
				{
					double t = node.getTag()+g.getEdge(n.getKey(), node.getKey());
					if(t<n.getTag()|| n.getTag()==-1.0)
					{
						n.setTag(t);
						pq.remove(n);
						pq.add(n);
						source.remove(n.getKey());
						source.put(n.getKey(),node.getKey());
					}
				}
				else if(n.getInfo().equals("E"))
				{
					double tag = node.getTag()+g.getEdge(n.getKey(), node.getKey());
					if(tag<n.getTag() || n.getTag()==-1)
					{
						source.remove(n.getKey());
						source.put(n.getKey(),node.getKey());
						n.setTag(tag);
					}
				}
			}
		}
		if(g.getNode(dest).getTag()==-1)
		{
			reset(lst);
			return null;
		}
		int i = dest;
		while(source.get(i)!=i)
		{
			l.addFirst(g.getNode(i));
			i=source.get(i);
		}
		l.addFirst(g.getNode(src));
		reset(lst);
		return l;
	}

	@Override
	public boolean save(String file) 
	{
		try {
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(g);
			f.close();
			o.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean load(String file) 
	{
		try {
			FileInputStream f = new FileInputStream(file);
			ObjectInputStream o = new ObjectInputStream(f);
			init((weighted_graph)o.readObject());
			f.close();
			o.close();
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}


	public void reset(LinkedList<node_info> lst)
	{
		Iterator<node_info> it = lst.iterator();
		while(it.hasNext())
		{
			node_info n = it.next();
			n.setInfo("");
			n.setTag(-1);
		}
	}
}
