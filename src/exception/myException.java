package exception;

public class myException extends Exception {
	public myException(String message){
	super(message);
	}
	public myException(Exception e){
		super(e);
	}
	
}
