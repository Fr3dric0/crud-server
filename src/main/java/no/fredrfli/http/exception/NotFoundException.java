package no.fredrfli.http.exception;

import no.fredrfli.http.HttpStatus;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 */
public class NotFoundException extends HttpException {

    public NotFoundException(String msg) {
        super(msg);

        status = HttpStatus.NOT_FOUND;
    }
}
