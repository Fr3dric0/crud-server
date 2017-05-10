import no.fredrfli.http.Request;
import no.fredrfli.http.Response;
import no.fredrfli.http.controller.Controller;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 09.05.2017
 */
public class IndexController extends Controller {

    public String get(Request req, Response res) {
        res.addHeader("Content-Type", "application/json");

        return "{\"title\":\"Hello World\"}";
    }
}
