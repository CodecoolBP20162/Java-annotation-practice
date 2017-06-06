import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ExampleRoutes {

    @WebRoute(route = "/")
    public void root(HttpExchange requestData) throws IOException {
        System.out.println("on route: root " + requestData.getRequestURI().getPath());
        sendResponse(requestData, "root route called!");
    }

    @WebRoute(route = "/test")
    public void test(HttpExchange requestData) throws IOException {
        System.out.println("on route: test " + requestData.getRequestURI().getPath());
        sendResponse(requestData, "test route called!");
    }

    @WebRoute(route = "/aaa")
    public void aaab(HttpExchange requestData, String bena) throws IOException {
        System.out.println("test " + requestData);
        sendResponse(requestData, "aaa called!");
    }

    private void sendResponse(HttpExchange requestData, String response) throws IOException {
        requestData.sendResponseHeaders(200, response.length());
        OutputStream os = requestData.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
