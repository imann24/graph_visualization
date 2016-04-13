package imann.graphvisualization.graph;

import imann.graphvisualization.gui.GraphGUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//used to implement search for Depth and Breadth (using stack and queue respectively)
//Dijkstra's also extends, but it overrides the search method more thoroughly
public abstract class Search {
	Map<String, Boolean> visited = new HashMap<String, Boolean>();
	Graph graph;
	GraphGUI gui;
	public Search(Graph graph, GraphGUI gui) {
		this.graph = graph;
		this.gui = gui;
	}
	
	public List<String> search (String start, String goal, LinkedList<List<String>> unvisited) {
		List<String> nodes = graph.getNodes();
		for(String s: nodes) {
			visited.put(s, false);
		}
		List<String> pathToCurNode = new LinkedList<String>();
		pathToCurNode.add(start);
		String curNode;
		unvisited.add(pathToCurNode);
		while (!unvisited.isEmpty()) {
			pathToCurNode = unvisited.removeFirst();
			curNode = pathToCurNode.get(pathToCurNode.size()-1);
			if (!visited.get(curNode))  {
				visited.put(curNode, true);
				if (curNode.equals(goal))
					return pathToCurNode;
				List<String> neighbors = null; 
				try {
					neighbors = graph.getNeighbors(curNode);
				} catch (NoSuchNodeException e) {
					e.printStackTrace();
				}
				for (String neighbor: neighbors) {
					List<String> temp = new ArrayList<String>(pathToCurNode);
					temp.add(neighbor);
					unvisited.push(temp);
				}
			}
		}
		return null;	
								
	}

}
