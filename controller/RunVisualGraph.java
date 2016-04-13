package imann.graphvisualization.controller;

import imann.graphvisualization.components.FileIn;
import imann.graphvisualization.components.FileOut;
import imann.graphvisualization.graph.Graph;
import imann.graphvisualization.graph.NoSuchNodeException;
import imann.graphvisualization.gui.GraphGUI;
import imann.graphvisualization.gui.NodeGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


//main class used to run and assemble the GUI
public class RunVisualGraph {
	private Container pane;
	private JComponent v;
	private Graph g;
	private GraphGUI gui;
	private JButton [] modes = new JButton[4];
	private JMenuBar topMenu = new JMenuBar();
	private JPanel top = new JPanel();
	private JFrame frame;
	private String filename = "file.dat";
	public RunVisualGraph (String filename) {
		this.filename = filename;
		g = new Graph();
		v = new GraphGUI(g);
		gui = (GraphGUI) v;
		createGraphFromFile(filename);
	
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			// Schedule a job for the event-dispatching thread:
			// creating and showing this application's GUI.
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	// constructor with no arguments creates a blank graph (though this is never called)
	public RunVisualGraph() {
		g = new Graph();
		v = new GraphGUI(g);
		gui = (GraphGUI) v;
		//createGraphFromFile(filename);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			// Schedule a job for the event-dispatching thread:
			// creating and showing this application's GUI.
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void main(String[] args) {
		new RunVisualGraph();
	}
	
	
	//creates the parts of the GUI (though GraphGUI handles the actual graph visualization)
	public void createComponents(Container pane) {
		pane.setLayout(new BorderLayout());
		pane.add(v, BorderLayout.CENTER);	
		pane.setFocusable( true );
		pane.requestFocus();
		pane.addNotify();
		pane.addKeyListener(new NumberControls(this));
		//adds the bottom buttons
		JPanel buttons = new JPanel();
		JPanel search = new JPanel();
		search.setBorder(new EmptyBorder(10, 10, 10, 10) );

		JComboBox select;
		JButton goSearch;
		JTextField start = new JTextField();
		JTextField goal = new JTextField();
		search.setLayout(new GridLayout(3, 1));
		search.add(new JLabel("Start:", SwingConstants.RIGHT));
		search.add(start);
		search.add(new JLabel("Goal:", SwingConstants.RIGHT));
		search.add(goal);
		search.add(select = new JComboBox());
		select.addItem("Breadth First");
		select.addItem("Depth First");
		select.addItem("Dijkstra's Shortest Path");
		search.add(goSearch = new JButton("Search"));
		goSearch.addActionListener(new SearchListener(g, (GraphGUI)v, start, goal, select, pane));
		buttons.add(search);
		buttons.setOpaque(true);
		buttons.setBackground(Color.BLACK);
		pane.add(buttons, BorderLayout.PAGE_END);
		
		//add the top buttons
		JPanel menu = new JPanel();
		JButton add = (modes[0] = new JButton ("Add Node"));
		JButton delete = (modes[1] = new JButton("Delete Node"));
		JButton addEdge = (modes[2] = new JButton("Add Edge"));
		JButton deleteEdge = (modes[3] = new JButton ("Delete Edge"));
		for (JButton button: modes)
			button.setBackground(Color.BLUE);
		menu.add(add);
		menu.add(delete);
		menu.add(addEdge);
		menu.add(deleteEdge);
		add.addActionListener(new AddListener((GraphGUI)v));
		delete.addActionListener(new DeleteListener((GraphGUI)v));
		addEdge.addActionListener(new AddEdgeListener((GraphGUI)v));
		deleteEdge.addActionListener(new DeleteEdgeListener((GraphGUI)v));
		menu.setOpaque(true);
		menu.setBackground(Color.BLACK);
		pane.add(top, BorderLayout.PAGE_START);
		
		//add top menu
		top.setLayout(new GridLayout(2,1));
		top.add(topMenu);
		top.add(menu);
		JTextField text;
		topMenu.add(new JLabel("File Name:"));
		topMenu.add(text = new JTextField(filename));
		JButton temp;
		topMenu.add(temp = new JButton("Save File"));
		temp.addActionListener(new SaveListener(gui, text));
		topMenu.add(temp = new JButton("Open File"));
		temp.addActionListener(new OpenListener(gui, text));
	}

	
	//constructs the window
	public void createAndShowGUI() {
		// Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);
		// Create and set up the window.
		frame = new JFrame("Graph Visualization");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(600, 600));
		// Add components
		pane = frame.getContentPane();
		createComponents(pane);
		// Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setSize(v.getMinimumSize());
	}
	
	public Graph getGraph () {
		return g;
	}
	
	public JButton[] getButtons () {
		return modes;
	}
	
	//parses the data from the file reader and creates the nodes and gui components
	private void createGraphFromFile (String filename) {
		g.clear();
		gui.clearNodes();
		FileIn f = new FileIn(gui, filename);
		LinkedList<NodeGUI> nodes = f.getNodes();
		LinkedList<String> nodeValues = new LinkedList<String>();
		while(!nodes.isEmpty()) {
			NodeGUI temp;
			gui.addNode(temp = nodes.removeFirst());
			nodeValues.add(temp.getValue());
		}
		Map <String, LinkedList<String>> edges = f.getEdges();
		while (!edges.isEmpty() && !nodeValues.isEmpty()) {
			if (edges.containsKey(nodeValues.peek())) {
				String s;
				LinkedList<String> temp = edges.remove(s = nodeValues.removeFirst());
				while (!temp.isEmpty()) {
					try {
						g.addEdge(s, temp.remove());
					} catch (NoSuchNodeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else nodeValues.removeFirst();
		}
		v.repaint();
	}
	
	//listeners to toggle the state of the gui
	class AddListener implements ActionListener{
		GraphGUI g;
		public AddListener(GraphGUI g) {
			this.g = g;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			g.mode(GraphGUI.Mode.ADD);
			for (JButton button: modes)
				button.setOpaque(false);
			modes[0].setOpaque(true);
			pane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
			pane.repaint();
			pane.requestFocus();
		}
	}
	
	class DeleteListener implements ActionListener{
		GraphGUI g;
		public DeleteListener(GraphGUI g) {
			this.g = g;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			g.mode(GraphGUI.Mode.DELETE);
			for (JButton button: modes)
				button.setOpaque(false);
			modes[1].setOpaque(true);
			pane.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			pane.repaint();
			pane.requestFocus();

		}
	}
	
	class AddEdgeListener implements ActionListener{
		GraphGUI g;
		public AddEdgeListener(GraphGUI g) {
			this.g = g;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			g.mode(GraphGUI.Mode.ADD_EDGE);
			for (JButton button: modes)
				button.setOpaque(false);
			modes[2].setOpaque(true);
			pane.repaint();
			pane.setCursor(new Cursor(Cursor.HAND_CURSOR));
			pane.requestFocus();

		}
	}
	
	class DeleteEdgeListener implements ActionListener{
		GraphGUI g;
		public DeleteEdgeListener(GraphGUI g) {
			this.g = g;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			g.mode(GraphGUI.Mode.DELETE_EDGE);
			for (JButton button: modes)
				button.setOpaque(false);
			modes[3].setOpaque(true);
			pane.setCursor(new Cursor(Cursor.HAND_CURSOR));
			pane.repaint();
			pane.requestFocus();

		}
	}
	
//	YES_OPTION
//	NO_OPTION
//	CANCEL_OPTION
//	OK_OPTION
//	CLOSED_OPTION
	//listeners to open and save files
	class SaveListener implements ActionListener {
		GraphGUI g;
		JTextField f;
		public SaveListener (GraphGUI g, JTextField f) {
			this.g = g;
			this.f = f;
		}
		public void actionPerformed (ActionEvent e) {
			int confirm = JOptionPane.showConfirmDialog(gui,
					 "Are you sure you want to save? \n This will overwrite any file in the directory with the same name.",
                    "Saving...",
                    JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION)
				new FileOut(RunVisualGraph.this.g, gui, f.getText());
			
		}
	}
	
	class OpenListener implements ActionListener {
		GraphGUI g;
		JTextField f;
		public OpenListener (GraphGUI g, JTextField f) {
			this.g = g;
			this.f = f;
		}
		public void actionPerformed (ActionEvent e) {
			int confirm = JOptionPane.showConfirmDialog(gui,
					"Do you want to save the current file before you open a new one?",
                   "Opening...",
                   JOptionPane.YES_NO_CANCEL_OPTION);
			boolean open = false;
			if (confirm == JOptionPane.YES_OPTION) {
				String s = JOptionPane.showInputDialog(gui, "Please enter a filename for the current graph", "Saving");
				new FileOut(RunVisualGraph.this.g, gui, s);
				open = true;
			}
			if (open || confirm == JOptionPane.NO_OPTION) {
				frame.dispose();
				new RunVisualGraph(f.getText());
			}
		}
	}
	
	
}
