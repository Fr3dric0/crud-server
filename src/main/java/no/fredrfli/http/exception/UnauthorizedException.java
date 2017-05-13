package no.fredrfli.http.exception;

import no.fredrfli.http.util.HttpStatus;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 13.05.2017
 */
public class UnauthorizedException extends HttpException {

    public UnauthorizedException() {
        this("401 UNAUTHORIZED");
    }

    public UnauthorizedException(String msg) {
        super(msg);
        status = HttpStatus.UNAUTHORIZED;
    }

}
