import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ServerRequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) {

        Class<ExampleRoutes> obj = ExampleRoutes.class;

        for (Method method : obj.getDeclaredMethods()) {

            if (method.isAnnotationPresent(WebRoute.class)) {

                WebRoute annotation = method.getAnnotation(WebRoute.class);

                // this can be made more complex
                if (annotation.route().equals(httpExchange.getRequestURI().getPath())) {
                    try {
                        method.invoke(obj.newInstance(), httpExchange);
                    } catch (IllegalAccessException | IllegalArgumentException |
                            InvocationTargetException | NullPointerException |
                            ExceptionInInitializerError | InstantiationException e) {
                        e.printStackTrace();
                        onError(httpExchange, "annotation error! " + e.toString() + " ");
                    }
                    return;
                }
            }
        }
        onError(httpExchange, "route not found ");
    }

    private void onError(HttpExchange httpExchange, String msg) {
        try {
            String response = msg + httpExchange.getRequestURI().getPath();
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
