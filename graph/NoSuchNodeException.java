package imann.graphvisualization.graph;
public class NoSuchNodeException extends Exception {

	/**
	 * the serialVersionUID is required to silence a java warning
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchNodeException(String label) {
		super("Node " + label + " does not exist");
	}

}