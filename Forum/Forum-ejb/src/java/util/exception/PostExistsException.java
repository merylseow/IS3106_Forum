/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author merylseow
 */
public class PostExistsException extends Exception {

    /**
     * Creates a new instance of <code>PostExistsException</code> without detail
     * message.
     */
    public PostExistsException() {
    }

    /**
     * Constructs an instance of <code>PostExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PostExistsException(String msg) {
        super(msg);
    }
}
