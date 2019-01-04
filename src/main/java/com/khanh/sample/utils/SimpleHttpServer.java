package com.khanh.sample.utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
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
        server.createContext("/addtrade").setHandler(this::addTradeHandler);
        server.createContext("/dologin").setHandler(this::doLoginHandler);
        server.createContext("/doaddtrade").setHandler(this::doAddTradeHandler);
        server.start();

        users = new ArrayList<>() {{
            add(Map.entry("Khanh", "Khanh"));
        }};
    }

    private void loginHandler(HttpExchange exchange) throws IOException {
        staticHtmlHandler(exchange, "static/login.html");
    }

    private void addTradeHandler(HttpExchange exchange) throws IOException {
        staticHtmlHandler(exchange, "static/new-trade.html");
    }

    private void staticHtmlHandler(HttpExchange exchange, String resource) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        switch (requestMethod) {
            case "GET": {
                exchange.getResponseHeaders().add("Content-Type", "text/html");

                InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resource);
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
        String response = "Non existed user";

        if (!requestMethod.equals("GET")) {
            response = "Only support GET method";
            exchange.sendResponseHeaders(405, response.getBytes(StandardCharsets.UTF_8).length);
        } else {
            Map<String, String> queries = queryToMap(exchange.getRequestURI().getQuery());
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getKey().equals(queries.get("username"))) {
                    if (users.get(i).getValue().equals(queries.get("password"))) {
                        response = "Successful";
                    } else {
                        response = "Incorrect combination";
                    }
                }
            }

            response = String.format("<div id='message'>%s</div>", response);
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void doAddTradeHandler(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String response;

        if (!requestMethod.equals("POST")) {
            response = "Only support POST method";
            exchange.sendResponseHeaders(405, response.getBytes(StandardCharsets.UTF_8).length);
        } else {
            StringBuilder body = new StringBuilder();
            try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                char[] buffer = new char[256];
                int read;
                while ((read = reader.read(buffer)) != -1) {
                    body.append(buffer, 0, read);
                }
            }

            //System.out.println(body.toString());
            Map<String, String> queries = queryToMap(body.toString());
            response = processTradeAdding(queries);

            response = String.format("<div id='message'>%s</div>", response);
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String processTradeAdding(Map<String, String> queries) {
        String temp;

        temp = queries.get("trade");
        if(temp == null || temp.isEmpty()) {
            return "Invalid trade";
        }
        int trade = Integer.parseInt(temp);
        if (trade < 1 || trade > 3) {
            return "Invalid trade";
        }

        temp = queries.get("volume");
        if(temp == null || temp.isEmpty()) {
            return "Invalid volume";
        }
        float volume = Float.parseFloat(temp);
        if (volume <= 0) {
            return "Invalid volume";
        }

        temp = queries.get("price");
        if(temp == null || temp.isEmpty()) {
            return "Invalid price";
        }
        float price = Float.parseFloat(temp);
        if (price <= 0) {
            return "Invalid price";
        }

        temp = queries.get("type");
        if(temp == null || temp.isEmpty()) {
            return "Invalid type";
        }
        String type = temp;
        if (!type.equals("buy") && !type.equals("sell")) {
            return "Invalid type";
        }

        return "Successful";
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
