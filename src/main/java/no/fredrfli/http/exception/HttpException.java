package no.fredrfli.http.exception;

import no.fredrfli.http.util.HttpStatus;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 */
public class HttpException extends RuntimeException {
    protected HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public HttpException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
