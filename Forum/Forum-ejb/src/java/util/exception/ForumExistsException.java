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
public class ForumExistsException extends Exception {

    /**
     * Creates a new instance of <code>ForumExistsException</code> without
     * detail message.
     */
    public ForumExistsException() {
    }

    /**
     * Constructs an instance of <code>ForumExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ForumExistsException(String msg) {
        super(msg);
    }
}
