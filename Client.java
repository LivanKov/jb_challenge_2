import java.net.ConnectException;
import java.util.Arrays;

public class Client {

    public static void main(String[] args) {
        var server = new ServerInstance();
        try {
            byte[] fileBytes = server.getFile("big_json.json", 0, 100);
            System.err.println("File content as bytes: " + new String(fileBytes));
        } catch (ConnectException e) {
            System.err.println("Failed to connect to the server. Please ensure the web server is running and accessible at " + ServerInstance.ADDRESS);
            System.err.println("Run the server with the command: 'make webserver'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}