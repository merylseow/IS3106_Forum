package util.exception;

public class UsernameExistsException extends Exception {
    
    public UsernameExistsException() {
    }
    
    public UsernameExistsException(String msg) {
        super(msg);
    }
}
