package EvalToolProject_bcc.tools;

public class NodeAlreadyDefinedException extends RuntimeException {

	public NodeAlreadyDefinedException() {
	}

	public NodeAlreadyDefinedException(String message) {
		super(message);
	}
}
