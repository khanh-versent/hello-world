package com.khanh.sample.utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleHttpServer {

    HttpServer server;
    List<Map.Entry<String, String>> users;

    public SimpleHttpServer(String hostname, int port) throws IOException {

        server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        server.createContext("/login").setHandler(this::loginHandler);
        server.createContext("/dologin").setHandler(this::doLoginHandler);
        server.start();

        users = new ArrayList<>() {{
            add(Map.entry("Khanh", "Khanh"));
        }};
    }

    private void loginHandler(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        switch (requestMethod) {
            case "GET": {
                exchange.getResponseHeaders().add("Content-Type", "text/html");

                InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("static/login.html");
                exchange.sendResponseHeaders(200, in.available());//response code and length
                OutputStream os = exchange.getResponseBody();
                byte[] buffer = new byte[256];
                int bytesRead = 0;
                while ((bytesRead = in.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                in.close();
                os.close();
            }
            break;
        }
    }

    private void doLoginHandler(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        Map<String, String> queries = queryToMap(exchange.getRequestURI().getQuery());
        String response = "Non existed user";
        switch (requestMethod) {
            case "GET": {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getKey().equals(queries.get("username"))) {
                        if( users.get(i).getValue().equals(queries.get("password"))) {
                            response = "Successful";
                        } else {
                            response = "Incorrect combination";
                        }
                    }
                }

                response = String.format("<div id='message'>%s</div>", response);
                exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            break;
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public void stop() {
        server.stop(0);
    }
}
