package imann.graphvisualization.controller;

import imann.graphvisualization.graph.Graph;
import imann.graphvisualization.graph.NoSuchNodeException;
import imann.graphvisualization.gui.GraphGUI;
import imann.graphvisualization.gui.NodeGUI;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.event.MouseInputListener;

import java.awt.Cursor;
import java.awt.Point;
public class VisualGraphListener implements MouseInputListener {
	private int count = 0;
	private GraphGUI gui;
	private Graph graph;
	int prevX = 0;
	int prevY = 0;
	public VisualGraphListener(GraphGUI gui, Graph graph) {
		this.gui = gui;
		this.graph = graph;
	}

	@Override
	//creates a new node if not in delete mode
	public void mouseClicked(MouseEvent e) {
		if (gui.getNode(e.getX(), e.getY()) == null && gui.mode() != GraphGUI.Mode.DELETE) { //add a node
			String s = JOptionPane.showInputDialog(gui, "Please enter a value for the new node", null);
			if (s != null) {
				if (!gui.addNode(s, e.getX(), e.getY())) {
					JOptionPane.showMessageDialog(gui,
						    "Node already exists in graph.");
				}	
				gui.selectNode(null);
			}
		}
		gui.repaint();
	}

	@Override
	
	//different behavior depending on the GUI's current setting
	public void mousePressed(MouseEvent e) {
		gui.setDragLocation(e.getPoint());
		if (gui.mode() == GraphGUI.Mode.ADD_EDGE) {
			if (gui.getSelectedNode() == null) {
				gui.selectNode(gui.getNode(e.getX(), e.getY()));
			} else if (gui.getNode(e.getX(), e.getY()) != null) {
				gui.addEdge(gui.getNode(e.getX(), e.getY()));
			}
		} else if (gui.mode() == GraphGUI.Mode.DELETE_EDGE) {
			if (gui.getSelectedNode() == null) {
				gui.selectNode(gui.getNode(e.getX(), e.getY()));
			} else if (gui.getNode(e.getX(), e.getY()) != null) {
				gui.deleteEdge(gui.getNode(e.getX(), e.getY()));
			}
		} else if (gui.mode() == GraphGUI.Mode.DELETE) {
			graph.clear();
			if (gui.getNode(e.getX(), e.getY()) != null) {
				NodeGUI n = gui.getNode(e.getX(), e.getY());
				gui.delete(n);
			}
		} else gui.selectNode(gui.getNode(e.getX(), e.getY()));
		gui.repaint();
	}

	@Override
	
	//either functions to place edges or finish dragging nodes
	public void mouseReleased(MouseEvent e) {
		gui.drawingEdge(false);
		gui.deletingEdge(false);
		if (gui.getSelectedNode() != null) 
			gui.repaint();
		if (gui.mode() == GraphGUI.Mode.ADD_EDGE && gui.getSelectedNode() != null && gui.getNode(e.getX(), e.getY()) != null) {
			gui.addEdge(gui.getNode(e.getX(), e.getY()));
		} else if (gui.mode() == GraphGUI.Mode.DELETE_EDGE && gui.getSelectedNode() != null && gui.getNode(e.getX(), e.getY()) != null) {
			gui.deleteEdge(gui.getNode(e.getX(), e.getY()));
		}
		gui.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	
	//either used to move nodes or add/delete edges
	@Override
	public void mouseDragged(MouseEvent e) {
		if (gui.getSelectedNode() != null) {
			if (gui.mode() != GraphGUI.Mode.ADD_EDGE  && gui.mode() != GraphGUI.Mode.DELETE_EDGE)  {
				Point newCenter = new Point(gui.getSelectedNode().x() +  e.getX() - gui.getLastDragLocation().x, gui.getSelectedNode().y() +  e.getY()-gui.getLastDragLocation().y);
				gui.getSelectedNode().center(newCenter);
				gui.repaint();
			} else {
				if (gui.mode() == GraphGUI.Mode.DELETE_EDGE)
					gui.deletingEdge(true);
				gui.drawingEdge(true);
				gui.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				gui.repaint();
			}
			gui.setDragLocation(e.getPoint());
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

}
