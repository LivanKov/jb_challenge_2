import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class ServerInstance {
    private HttpClient client;
    private String address;

    public static final String ADDRESS = "http://localhost:8080/";

    public ServerInstance() {
        this(HttpClient.newHttpClient(), ADDRESS);
    }

    public ServerInstance(HttpClient client, String address) {
        this.client = client;
        this.address = address;
    }

    public byte[] getFile(String fileName) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(this.address + fileName))
            .GET()
            .build();
        HttpResponse<byte[]> response = this.client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        return response.body();
    }

    public byte[] getFile(String fileName, int from, int to) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(this.address + fileName))
            .header("Range", "bytes=" + from + "-" + to)
            .GET()
            .build();
        HttpResponse<byte[]> response = this.client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        return response.body();
    }

    public String[] fetchHeaders(String fileName) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(this.address + fileName))
            .HEAD()
            .build();
        HttpResponse<Void> response = this.client.send(request, HttpResponse.BodyHandlers.discarding());
        String contentLength = response.headers().firstValue("Content-Length").get();
        String acceptRanges = response.headers().firstValue("Accept-Ranges").get();
        return new String[]{contentLength, acceptRanges};
    }
}
