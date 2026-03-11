import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class ServerInstance {
    private String name;
    private String address;
    private HttpClient client;

    public ServerInstance(String name, String address) {
        this.name = name;
        this.address = address;
        this.client = HttpClient.newHttpClient();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public byte[] getFile(String fileName) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/" + fileName))
            .header("Accept", "application/json")
            .GET()
            .build();
        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body().getBytes();  
    }

}