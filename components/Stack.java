package imann.graphvisualization.components;

import java.util.LinkedList;
import java.util.List;
//slight tweak to linked list to make give it stack functionality 
public class Stack extends LinkedList<List<String>> {
	public void push (List<String> s) {
		addFirst(s);
	}
}
