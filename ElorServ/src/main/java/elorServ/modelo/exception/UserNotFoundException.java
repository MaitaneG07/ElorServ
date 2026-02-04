package elorServ.modelo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1877969201439820684L;
	
	public UserNotFoundException(String message) {
		super(message);
	}
	

}
