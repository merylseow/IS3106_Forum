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
public class UpdateAdminException extends Exception {

    /**
     * Creates a new instance of <code>UpdateAdminException</code> without
     * detail message.
     */
    public UpdateAdminException() {
    }

    /**
     * Constructs an instance of <code>UpdateAdminException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateAdminException(String msg) {
        super(msg);
    }
}
