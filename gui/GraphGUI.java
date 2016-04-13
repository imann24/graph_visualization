package imann.graphvisualization.gui;

import imann.graphvisualization.controller.VisualGraphListener;
import imann.graphvisualization.graph.Graph;
import imann.graphvisualization.graph.NoSuchNodeException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
//the inner shell of the GUI, handles drawing the nodes
public class GraphGUI extends JComponent {
	private int nodeSize = 50, x = 50, y = 50;
	private Graph g;
	private List<NodeGUI> nodes;
	private NodeGUI selected = null;
	private boolean deleteActive = false;
	private boolean addEdgesActive = true;
	public static enum Mode {DELETE, ADD, ADD_EDGE, DELETE_EDGE};
	public static enum Search {DEPTH, BREADTH, DIJKSTRA};
	private Map <String, Search> searches = new HashMap<String, Search>();
	private Mode mode = Mode.ADD;
	private Point lastDragLocation;
	private Search searchType = Search.BREADTH;
	private List<String> searchPath = new LinkedList<String>();
	private boolean drawingEdge = false;
	private boolean deletingEdge = false;
	public GraphGUI (Graph g) {
		nodes = new ArrayList<NodeGUI>();
		this.g = g;
		this.addMouseListener(new VisualGraphListener(this, g));
		this.addMouseMotionListener(new VisualGraphListener(this, g));
		for (String s:this.g.getNodes()) {
			NodeGUI l;
			nodes.add(l = new NodeGUI(this, x, y, s));
			x+=nodeSize;
			y+=nodeSize;
		}
		searches.put("Breadth First", Search.BREADTH);
		searches.put("Depth First", Search.DEPTH);
		searches.put("Dijkstra's Shortest Path", Search.DIJKSTRA);
	}
	
	public Dimension getMinimumSize () {
		return new Dimension(500, 500);
	}
	
	//draws nodes as circles
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);	
		Font f = new Font("sans-serif", Font.PLAIN, 10);
		g.setFont(f);
		for (NodeGUI a: nodes) {
			for (NodeGUI b: nodes) {
				try {
					if (this.g.edgeExists(a.getValue(), b.getValue())) {
						if (searchPath.contains(a.getValue()) && searchPath.contains(b.getValue()) &&
							Math.abs(searchPath.indexOf(a.getValue()) - searchPath.indexOf(b.getValue())) <= 1){
							g.setColor(Color.RED);
						} else g.setColor(Color.BLACK);
						g.drawLine(a.x(), a.y(), b.x(), b.y());
					}
				} catch (NoSuchNodeException e) {
					System.out.println("One or more node doesn't exist");
				}
			}
		}
		if (drawingEdge)  {
			drawEdge(g, lastDragLocation);
		}
		for (NodeGUI n: nodes) {
			if (n == selected)
				drawNode(g, n, Color.RED);
			else drawNode(g, n, Color.BLACK);
		}
		//draws a text box detailing information about the selected node
		if (selected != null) {
			int x = 510;
			x -= selected.getValue().length() * 5;
			g.setColor(Color.BLACK);
			g.drawString("Value: " + selected.getValue(), x, 100);
			g.drawString("Position: (" + selected.x() +", " + selected.y() + ")"  , x, 125);
			String neighbors = "";
			List<String> adjacent = null;
			try {
				adjacent = this.g.getNeighbors(selected.getValue());
			} catch (NoSuchNodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			if (adjacent.size() > 0)
				g.drawString("Neighbors:", x, 150);
			for (int i = 0; i < adjacent.size(); i++) {
				g.drawString(adjacent.get(i), x, 165 + i * 15);
			}
			g.drawRect(x-10, 75, 95 + selected.getValue().length()*4, 75 + adjacent.size() * 20);
			
		}
	}
	
	public Graph getGraph() {
		return g;
	}
	
	public int getNodeSize () {
		return nodeSize;
	}
	
	public NodeGUI getNode (int x, int y) {
		for (NodeGUI node:nodes) {
			if (nodeSize/2 > Math.abs(x - node.x()) &&
				nodeSize/2 > Math.abs(y - node.y())) {
				return node;
			}
		}
		return null;
	}
	
	public NodeGUI getNode (String s) {
		for (NodeGUI g : nodes) {
			if (g.getValue().equals(s))
				return g;
		}
		return null;
	}
	
	public boolean addNode (String s, int x, int y) {
		if (!g.nodeExists(s)) {
			g.addNode(s);
			nodes.add(new NodeGUI(this, x, y, s));
			return true;
		} else return false;
	}
	
	public boolean addNode(NodeGUI node) {
		if (!g.nodeExists(node.getValue())) {
			g.addNode(node.getValue());
			nodes.add(node);
			return true;
		} else return false;
	}
	
	//searches through the x,y pos of nodes and returns one if the click was within one of the positions
	public NodeGUI getClickedNode () {
		return null;
	}
	
	public void selectNode (NodeGUI g) {
		selected = g;
	}
	
	public NodeGUI getSelectedNode () {
		return selected;
	}
	
	public boolean deleteActive () {
		return deleteActive;
	}
	
	public void deleteActive (boolean d) {
		deleteActive = d;
	}
	
	public boolean addEdgesActive () {
		return addEdgesActive;
	}
	
	public void addEdgesActive (boolean b) {
		addEdgesActive = b;
	}
	
	public void delete (NodeGUI g) {
		nodes.remove(g);
		try {
			this.g.removeNode(g.getValue());
		} catch (NoSuchNodeException e) {
			e.printStackTrace();
		}
	}
	
	public void addEdge (NodeGUI g) {
		try {
			this.g.addEdge(selected.getValue(), g.getValue());
		} catch (NoSuchNodeException e) {
			e.printStackTrace();
		}
		selected = null;
	}
	
	public void deleteEdge (NodeGUI g) {
		this.g.removeEdge(selected.getValue(), g.getValue());
		selected = null;
	}
	
	public void drawNode(Graphics g, NodeGUI node, Color c) {
		g.setColor(c);
		g.fillOval(node.x()-nodeSize/2, node.y()-nodeSize/2, nodeSize, nodeSize);
		g.setColor(Color.WHITE);
		if (node.getValue().length() <= 5) {
			g.drawString(node.getValue(), node.x()-nodeSize/4, node.y());
		} else {
			g.drawString(node.getValue().substring(0, 4) + "...", node.x()-nodeSize/4, node.y());
		}
	}
	
	public Mode mode () {
		return mode;
	}
	
	public void mode (Mode m) {
		mode = m;
	}
	
	public void setDragLocation (Point p) {
		lastDragLocation = p;
	}
	
	public Point getLastDragLocation () {
		return lastDragLocation;
	}
	
	public Search searchType () {
		return searchType;
	}
	
	public void searchType (Search s) {
		searchType = s;
	}
	
	public void searchType (String s) {
		searchType = searches.get(s);
	}

	public int length(String s, String u) {
		NodeGUI n = getNode(s);
		NodeGUI n2 = getNode(u);
		return length(n, n2);
	}
	
	public int length(NodeGUI n, NodeGUI n2) {
		return (int) Math.sqrt(Math.abs(n.x() - n2.x()) + Math.abs(n.y() - n2.y()));
	}
	
	public void drawPath (List<String> undrawn) {
		searchPath.clear();
		while (!undrawn.isEmpty()) {
			searchPath.add(undrawn.remove(undrawn.size()-1));
			repaint();
		}
	}
	
	public List<String> searchPath () {
		return searchPath;
	}
	
	public void searchPath(List<String> s) {
		searchPath = s;
	}
	
	public void drawEdge (Graphics g, Point mouse) {
		if (deletingEdge)
			g.setColor(Color.RED);
		else g.setColor(Color.GREEN);
		g.drawLine(selected.x(), selected.y(), mouse.x, mouse.y);
	}
	
	public boolean drawingEdge () {
		return drawingEdge;
	}
	
	public void drawingEdge (boolean b) {
		drawingEdge = b;
	}
	
	public boolean deletingEdge () {
		return deletingEdge;
	}
	
	public void deletingEdge (boolean b) {
		deletingEdge = b;
	}
	
	public List<NodeGUI> getNodes () {
		return nodes;
	}
	
	public void clearNodes () {
		nodes.clear();
	}
}
