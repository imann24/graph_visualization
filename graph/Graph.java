package imann.graphvisualization.graph;
import java.util.ArrayList;
import java.util.List;

//original graph class with some overhauls

public class Graph {
	private boolean [][] edges; 
	private String [] nodes;
	private int index = 0;
	
	public Graph (int initialSize) {
		edges = new boolean [initialSize][initialSize];
		nodes = new String[initialSize];
	}
	public Graph (){
		this(10);
	}
	

	public java.util.List<String> getNeighbors(String string) throws NoSuchNodeException{
		java.util.List<String> list = new ArrayList<String>();
		int node = nodeToIndex(string);
		for (int i = 0; i < index; i++) {
			if (edges[node][i])
				list.add(nodes[i]);
		}
		return list;
	}

	public boolean nodeExists(String string) {
		try {
			nodeToIndex(string);
			return true;
		} catch (NoSuchNodeException e) {
			return false;
		}
	}

	public boolean edgeExists(String node, String node2) throws NoSuchNodeException {
		if (edges[nodeToIndex(node)][nodeToIndex(node2)])
			return true;
		return false;
	}
	
	public int edgeCount() {
		int count = 0;
		for (int i = 0; i < index; i++) {
			for (int j = 0; j < index; j++) {
				if (edges[i][j])
					count++;
			}
		}
		return count;
	}
	
	
	//remove node method has been rewritten to function more accurately 
	public void removeNode(String string) throws NoSuchNodeException {
		List<List<String>> neighbors = new ArrayList<List<String>>(index-1);
		int deleteIndex = -1;
		for (int i = 0; i < index; i++) {
			if (nodes[i].equals(string)) {
				deleteIndex = i;
			} else {
				ArrayList<String> temp = new ArrayList<String>(getNeighbors(nodes[i]));
				temp.remove(string);
				neighbors.add(temp);
			}
			for (int j = 0; j < nodes.length; j++) {
				edges[i][j] = false;
			}
		}
		for (int i = deleteIndex; i < index-1; i++) {
			nodes[i] = nodes[i+1];
		}
		nodes[index-1] = null;
		if (index > 0)
			index--;
		for (int i = 0; i < neighbors.size(); i++) {
			for (int j = 0; j < neighbors.get(i).size(); j++) {
				edges[i][nodeToIndex(neighbors.get(i).get(j))] = true;
			}
		}
	}

	public void removeEdge(String string, String string2) {
		try {
			edges[nodeToIndex(string)][nodeToIndex(string2)] = false;
		} catch (NoSuchNodeException e) {
			
		}
	}

	public int nodeCount() { 
		return index;
	}

	public void addEdge(String string, String string2) throws NoSuchNodeException {
		edges[nodeToIndex(string)][nodeToIndex(string2)] = true;
	}

	public void addNode(String string) {
		if (!nodeExists(string)) {
			maintainArrays();
			nodes[index++] = string;
		}
	}
	
	@Override 
	public String toString () {
		String s = "";
		for(int i = 0; i < nodeCount(); i++) {
			s += nodes[i] + ", ";
		}
		s = s.substring(0, s.length()-2);
		return s;
	}
	
	public List<String> getNodes () {
		List<String> nodes = new ArrayList<String>();
		for (int i = 0; i < nodeCount(); i++) {
			nodes.add(this.nodes[i]);
		}
		return nodes;
	}
	
	public boolean[][] getEdges () {
		return edges;
	}
	
	public int index() {
		return index;
	}
	
	public void clear () {
		for (int i = 0; i < index; i++) {
			try {
				removeNode(nodes[i]);
			} catch (NoSuchNodeException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int nodeToIndex (String key) throws NoSuchNodeException {
		for (int i = 0; i < index; i++) {
			if (key.equals(nodes[i]))
				return i;
		}
		throw new NoSuchNodeException(key);
	}
	
	//used to resize the arrays if the array reaches capacity
	private void maintainArrays() {
		if (index * 1.1 > nodes.length) {
			String[]newNodes = new String[nodes.length*2];
			System.arraycopy(nodes, 0, newNodes, 0, nodes.length);
			nodes = newNodes;
			for (int i = 0; i < edges.length; i++) {
				boolean[] newEdges = new boolean[edges[i].length*2];
				System.arraycopy(edges[i], 0, newEdges, 0, edges.length);
				edges[i] = newEdges;
			}
			boolean[][] allNewEdges = new boolean [edges.length*2][edges.length*2];
			System.arraycopy(edges, 0, allNewEdges, 0, edges.length);
			edges = allNewEdges;
		}
	}
	
	
	//prints the incidence matrix for debugging
	private void printMatrix () {
		for (int i = 0; i < index; i++) {
			System.out.print(nodes[i] + " ");
			for (int j = 0; j < index; j++) 
				System.out.print(edges[i][j] + " ");
			System.out.println();
		}
	}
}
