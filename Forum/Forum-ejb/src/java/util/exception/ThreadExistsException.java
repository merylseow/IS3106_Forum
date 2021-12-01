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
public class ThreadExistsException extends Exception {

    /**
     * Creates a new instance of <code>ThreadExistsException</code> without
     * detail message.
     */
    public ThreadExistsException() {
    }

    /**
     * Constructs an instance of <code>ThreadExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ThreadExistsException(String msg) {
        super(msg);
    }
}
