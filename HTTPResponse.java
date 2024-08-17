import java.util.Hashtable;

public class HTTPResponse {
    private int statusCode;
    private Hashtable headers;
    private String body;

    public HTTPResponse(int statusCode, Hashtable headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Hashtable getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}