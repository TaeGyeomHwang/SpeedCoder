package exception;

public class EmptyTitleContentException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6933054053912090440L;

	public EmptyTitleContentException() {
        super("제목 또는 내용이 공백입니다.");
    }
}