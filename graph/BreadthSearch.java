package imann.graphvisualization.graph;

import java.util.List;

import imann.graphvisualization.components.Queue;
import imann.graphvisualization.gui.GraphGUI;

public class BreadthSearch extends Search {

	public BreadthSearch(Graph graph, GraphGUI gui) {
		super(graph, gui);
	}
	
	//calls search method with a queue
	public List<String> search (String start, String goal, Queue unvisited) {
		return super.search (start, goal, unvisited);
	}
}
