package elorServ.modelo.exception;

public class ElorException extends Exception{

	private static final long serialVersionUID = -2862464085406826788L;
	
    public ElorException(String message) {
        super(message);
    }

    public ElorException(String message, Throwable cause) {
        super(message, cause);
    }

}
