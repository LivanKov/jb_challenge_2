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
            .GET()
            .build();
        HttpResponse<byte[]> response = this.client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        long contentLength = Long.valueOf(response.headers().firstValue("Content-Length").get());
        System.out.println("Content-Length: " + contentLength);
        System.out.println("Accept ranges: " + response.headers().firstValue("Accept-Ranges").get());   
        return response.body();
    }

    public byte[] getFile(String fileName, int from, int to) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(ServerInstance.ADDRESS + fileName))
            .header("Range", "bytes=" + from + "-" + to)
            .GET()
            .build();
        HttpResponse<byte[]> response = this.client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        long contentLength = Long.valueOf(response.headers().firstValue("Content-Length").get());
        System.out.println("Content-Length: " + contentLength);
        System.out.println("Accept ranges: " + response.headers().firstValue("Accept-Ranges").get());   
        return response.body();
    }

    public String[] fetchHeaders(String fileName) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(ServerInstance.ADDRESS + fileName))
            .HEAD()
            .build();
        HttpResponse<Void> response = this.client.send(request, HttpResponse.BodyHandlers.discarding());
        String contentLength = response.headers().firstValue("Content-Length").get();
        String acceptRanges = response.headers().firstValue("Accept-Ranges").get();
        return new String[]{contentLength, acceptRanges};
    }
}