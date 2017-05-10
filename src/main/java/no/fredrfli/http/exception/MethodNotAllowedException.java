package no.fredrfli.http.exception;

import no.fredrfli.http.util.HttpStatus;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 */
public class MethodNotAllowedException extends HttpException {

    public MethodNotAllowedException() {
        super(null);
        this.status = HttpStatus.METHOD_NOT_ALLOWED;
    }

    public MethodNotAllowedException(String msg) {
        super(msg);
        this.status = HttpStatus.METHOD_NOT_ALLOWED;
    }
}
