package no.fredrfli.http;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 07.05.2017
 */
public class RequestTest {

    private String validRequestTemplate =
            "POST / HTTP/1.1\r\n" +
            "Host: localhost:8080\r\n" +
            "Connection: keep-alive\r\n" +
            "Content-Length: 57\r\n" +
            "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36\r\n" +
            "Cache-Control: no-cache\r\n" +
            "Origin: chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop\r\n" +
            "Content-Type: application/json\r\n" +
            "Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0OTM1MDAwMTYsInN1YiI6IjU5MDQ3ZDQ1ZjdkNTQzMGQ0YTg1ZTkwNyIsImlhdCI6MTQ5MzQ4OTIxNn0.olnpsjyk8gdu63gEMZOowtU49PNhwCLIB2qNfXaGn10\r\n" +
            "Postman-Token: ec04cbed-8764-a742-6282-2f5cf65c0f67\r\n" +
            "Accept: */*\r\n" +
            "Accept-Encoding: gzip, deflate, br\r\n" +
            "Accept-Language: en-US,en;q=0.8,da;q=0.6,nb;q=0.4,sv;q=0.2,nn;q=0.2\r\n" +
            "\r\n";

    // Missing protocol
    private String invalidRequestTemplate =
            "POST / /1.1\r\n" +
                    "Host: localhost:8080\r\n" +
                    "Connection: keep-alive\r\n" +
                    "Content-Length: 57\r\n" +
                    "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36\r\n" +
                    "Cache-Control: no-cache\r\n" +
                    "Origin: chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop\r\n" +
                    "Content-Type: application/json\r\n" +
                    "Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0OTM1MDAwMTYsInN1YiI6IjU5MDQ3ZDQ1ZjdkNTQzMGQ0YTg1ZTkwNyIsImlhdCI6MTQ5MzQ4OTIxNn0.olnpsjyk8gdu63gEMZOowtU49PNhwCLIB2qNfXaGn10\r\n" +
                    "Postman-Token: ec04cbed-8764-a742-6282-2f5cf65c0f67\r\n" +
                    "Accept: */*\r\n" +
                    "Accept-Encoding: gzip, deflate, br\r\n" +
                    "Accept-Language: en-US,en;q=0.8,da;q=0.6,nb;q=0.4,sv;q=0.2,nn;q=0.2\r\n" +
                    "\r\n";

    private String reqTemplateWithQueryParams =
            "POST /blablabla?type=json&name=jonSnow HTTP/1.1\r\n" +
                    "Host: localhost:8080\r\n" +
                    "Connection: keep-alive\r\n" +
                    "\r\n";


    @Test
    public void ensureHttpRequestIsCorrectlyParsed() {
        Request req = new Request(validRequestTemplate);

        assertTrue(!req.getHeaders().containsKey("Host"));
        assertEquals(req.getHeaders().get("host"), "localhost:8080");
        assertEquals(req.getMethod(), "POST");
        assertEquals(req.getVersion(), "HTTP/1.1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureExceptionOnBadHttpRequest() {
        Request req = new Request(invalidRequestTemplate);
    }

    @Test
    public void expectQueryParameters() {
        Request req = new Request(reqTemplateWithQueryParams);

        Map<String, String> query = req.getQuery();

        assertEquals(query.size(), 2);
        assertEquals(query.get("type"), "json");
        assertEquals(query.get("name"), "jonSnow");
    }

}