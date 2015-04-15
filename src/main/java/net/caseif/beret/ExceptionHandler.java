package net.caseif.beret;

/**
 * Represents an exception handler within a method.
 *
 * @author Max Roncacé
 */
public class ExceptionHandler {

	private MethodInfo parent;
	private int startIndex;
	private int endIndex;
	private int handlerStart;
	private String catchType;

	/**
	 * Instantiates a new {@link ExceptionHandler} with the given information.
	 *
	 * @param parent The parent {@link MethodInfo}
	 * @param start The index within the method's code array at which this
	 *              {@link ExceptionHandler} becomes active
	 * @param end The index within the method's code array at which this
	 *              {@link ExceptionHandler} ends
	 * @param handlerStart The index within the method's code array at which
	 *                     this {@link ExceptionHandler} begins
	 * @param catchType The class representing the exception this
	 *                  {@link ExceptionHandler} is designated to catch
	 */
	public ExceptionHandler(MethodInfo parent, int start, int end, int handlerStart, String catchType) {
		if (end <= start) {
			throw new IllegalArgumentException("Exception handler end index must be greater than start index");
		}
		this.parent = parent;
		this.startIndex = start;
		this.endIndex = end;
		this.handlerStart = handlerStart;
		this.catchType = catchType;
	}

	public MethodInfo getParent() {
		return this.parent;
	}

	public int getStartIndex() {
		return this.startIndex;
	}

	public int getEndIndex() {
		return this.endIndex;
	}

	public int getHandlerStartIndex() {
		return this.handlerStart;
	}

	public String getCatchType() {
		return this.catchType;
	}

}
