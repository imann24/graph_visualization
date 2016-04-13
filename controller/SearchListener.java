package imann.graphvisualization.controller;

import imann.graphvisualization.components.Queue;
import imann.graphvisualization.components.Stack;
import imann.graphvisualization.graph.BreadthSearch;
import imann.graphvisualization.graph.DepthSearch;
import imann.graphvisualization.graph.DijkstraSearch;
import imann.graphvisualization.graph.Graph;
import imann.graphvisualization.graph.Search;
import imann.graphvisualization.gui.GraphGUI;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class SearchListener implements ActionListener {
	private Graph graph;
	private GraphGUI gui;
	private JTextField start;
	private JTextField goal;
	private JComboBox type;
	private Container pane;
	private Search search;
	private List<String> path;
	public SearchListener(Graph graph, GraphGUI gui, JTextField start, JTextField goal, JComboBox type, Container pane) {
		this.graph = graph;
		this.gui = gui;
		this.start = start;
		this.goal = goal;
		this.type = type;
		this.pane = pane;
	}

	@Override
	//sets program to the proper search algorithm and passes this to the gui
	public void actionPerformed(ActionEvent e) {
		if (!graph.nodeExists(start.getText()) || !graph.nodeExists(goal.getText()))
			return;
		gui.searchType((String)type.getSelectedItem());
		if (gui.searchType() == GraphGUI.Search.BREADTH) {
			search = new BreadthSearch(graph, gui);
			path = search.search(start.getText(), goal.getText(), new Queue());
		} else if (gui.searchType() == GraphGUI.Search.DEPTH) {
			search = new DepthSearch(graph, gui);
			path = search.search(start.getText(), goal.getText(), new Stack());
		} else if (gui.searchType() == GraphGUI.Search.DIJKSTRA) {
			search = new DijkstraSearch(graph, gui);
			path = search.search(start.getText(), goal.getText(), null);
		}
		pane.requestFocus();
		if (path == null || path.isEmpty())
			return;
		gui.drawPath(path);	
	}
}
