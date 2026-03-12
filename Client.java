import java.net.ConnectException;

public class Client {

    public static void main(String[] args) {
        var server = new ServerInstance();
        try {
            byte[] fileBytes = server.getFile("big_json.json");
            System.out.println("File content as bytes: " + new String(fileBytes));
        } catch (ConnectException e) {
            System.out.println("Failed to connect to the server. Please ensure the web server is running and accessible at " + ServerInstance.ADDRESS);
            System.out.println("Run the server with the command: 'make webserver'");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}