package imann.graphvisualization.gui;
import java.awt.Point;
public class NodeGUI { //an individual node in the graph, mostly used to stores the coordinates
	private Point center;
	private int x, y;
	private String value;
	private GraphGUI g;
	public NodeGUI(GraphGUI g, int x, int y, String v) {
		this.x = x;
		this.y = y;
		value = v;
		this.g = g;
	}
	
	public String getValue () {
		return value;
	}
	
	public int x () {
		return x;
	}
	
	public int y () {
		return y;
	}

	@Override
	public String toString () {
		return value + " at (" + x + ", " + y + ")";
	}
	
	public Point center () {
		return center;
	}
	
	public void center (Point c) {
		center = new Point (c.x, c.y);
		y = center.y;
		x = center.x;
	}
}
