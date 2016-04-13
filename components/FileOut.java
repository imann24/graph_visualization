package imann.graphvisualization.components;

import imann.graphvisualization.graph.Graph;
import imann.graphvisualization.graph.NoSuchNodeException;
import imann.graphvisualization.gui.GraphGUI;
import imann.graphvisualization.gui.NodeGUI;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FileOut {

	//writes a file out with nodes and edges
	//write out is comptable with read in
	public FileOut(Graph graph, GraphGUI gui, String filename) {
		if (!filename.contains("dat") && !filename.contains("txt"))
			filename+= ".dat";
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(filename, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<NodeGUI> nodeGUIs = gui.getNodes();
		for (NodeGUI s : nodeGUIs)
			writer.println("n, " + s.x() +", " + s.y() +", " + s.getValue());
		List<String> nodes = graph.getNodes();
		List<List<String>> neighbors = new ArrayList<List<String>>(graph.index());
		for (int i = 0; i < graph.index(); i++) {			
			ArrayList<String> temp = null;
			try {
				temp = new ArrayList<String>(graph.getNeighbors(nodes.get(i)));
			} catch (NoSuchNodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (String s : temp)
				writer.println("e, " + nodes.get(i) + ", " + s);

		}
		
		writer.close();
	}

}
