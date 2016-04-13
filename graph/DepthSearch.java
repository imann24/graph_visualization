package imann.graphvisualization.graph;

import java.util.List;

import imann.graphvisualization.components.Stack;
import imann.graphvisualization.gui.GraphGUI;

public class DepthSearch extends Search {

	public DepthSearch(Graph graph, GraphGUI gui) {
		super(graph, gui);
	}
	//calls search method with a queue
	public List<String> search (String start, String goal, Stack unvisited) {
		return super.search (start, goal, unvisited);
	}
}
