package academy.certif.petpal.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Account already exists")
public class AccountExistsException extends Exception {
    
}
