package imann.graphvisualization.components;

import java.util.LinkedList;
import java.util.List;

//slight modification to linkedlist to ensure queue functionality
public class Queue extends LinkedList<List<String>> {
	public void push (List<String> s) {
		addLast(s);
	}
}
