import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class ServerInstance {
    private HttpClient client;

    public static final String ADDRESS = "http://localhost:8080/";

    public ServerInstance() {
        this.client = HttpClient.newHttpClient();
    }

    public byte[] getFile(String fileName) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(ServerInstance.ADDRESS + fileName))
            .HEAD()
            .build();
        HttpResponse<byte[]> response = this.client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        System.out.println("Content-Length: " + Long.valueOf(response.headers().firstValue("Content-Length").get()));
        return response.body();
    }

}