package pe.skyairline.desktop.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Cliente HTTP generico usado por todos los paneles CRUD del sistema de escritorio.
 * Consume la misma API REST (modulo backend) que usa la web publica.
 */
public class ApiClient {

    // Cambiar esta URL si el backend corre en otra maquina/puerto
    public static final String BASE_URL = "http://localhost:8080/api";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static ObjectMapper mapper() {
        return mapper;
    }

    public static <T> List<T> getList(String path, com.fasterxml.jackson.databind.JavaType type) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + path)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkStatus(response);
        return mapper.readValue(response.body(), type);
    }

    public static <T> T post(String path, Object body, Class<T> responseType) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkStatus(response);
        return mapper.readValue(response.body(), responseType);
    }

    public static <T> T put(String path, Object body, Class<T> responseType) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkStatus(response);
        return mapper.readValue(response.body(), responseType);
    }

    public static void delete(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + path)).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkStatus(response);
    }

    private static void checkStatus(HttpResponse<String> response) throws IOException {
        int code = response.statusCode();
        if (code >= 400) {
            throw new IOException("Error del servidor (HTTP " + code + "): " + response.body());
        }
    }
}
