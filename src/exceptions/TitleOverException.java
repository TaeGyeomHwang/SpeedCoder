package exceptions;

public class TitleOverException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3611547217308233294L;
	
	public TitleOverException() {
        super("제목이 올바르지 않습니다.");
    }
}
