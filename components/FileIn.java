package imann.graphvisualization.components;

import imann.graphvisualization.gui.GraphGUI;
import imann.graphvisualization.gui.NodeGUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;


//reads a file in an parses nodes and edges
public class FileIn {
	private GraphGUI gui;
	private Scanner s;
	private LinkedList <NodeGUI> nodes = new LinkedList<NodeGUI>();
	private Map <String, LinkedList<String>> edges = new HashMap<String, LinkedList<String>>();
	public FileIn(GraphGUI gui, String fileName) {
		this.gui = gui;
		try {
			s = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<String> temp = new LinkedList<String>();
		while (s.hasNextLine()) {
			temp.add(s.nextLine());
		}
		while (!temp.isEmpty()) {
			String s = temp.removeFirst();
			if (s.charAt(0)=='n')
				addNode(s);
			else if (s.charAt(0)=='e')
				addEdge(s);
		}
			
	}
	
	public LinkedList<NodeGUI> getNodes () {
		return nodes;
	}
	
	public Map<String, LinkedList<String>> getEdges () {
		return edges;
	}
	
	private void addNode (String s) {
		String[]parts = s.split(",");
		nodes.add(new NodeGUI(gui, Integer.parseInt(parts[1].substring(1)), Integer.parseInt(parts[2].substring(1)), parts[3].substring(1)));
	}
	
	private void addEdge (String s) {
		String[]parts = s.split(",");
		LinkedList<String> temp = null;
		if (edges.get(parts[1]) != null)
			temp = edges.get(parts[1]);
		else temp = new LinkedList<String>();
		temp.add(parts[2].substring(1, parts[2].length()));
		edges.put(parts[1].substring(1, parts[1].length()), temp);
	}
}
