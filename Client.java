import java.net.ConnectException;

public class Client {


    public static void getFileHeaders(ServerInstance server, String fileName) {
        try {
            String[] headers = server.fetchHeaders(fileName);
            System.out.println("Content-Length: " + headers[0]);
            System.out.println("Accept-Ranges: " + headers[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getWholeFile(ServerInstance server, String fileName) {
        try {
            byte[] fileBytes = server.getFile(fileName);
            System.out.println("File content as bytes: " + new String(fileBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getFilePart(ServerInstance server, String fileName, int from, int to) {
        try {
            byte[] fileBytes = server.getFile(fileName, from, to);
            System.out.println("File content as bytes: " + new String(fileBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        var server = new ServerInstance();
        try {
            byte[] fileBytes = server.getFile("big_json.json", 0, 100);
            System.out.println("File content as bytes: " + new String(fileBytes));
        } catch (ConnectException e) {
            System.err.println("Failed to connect to the server. Please ensure the web server is running and accessible at " + ServerInstance.ADDRESS);
            System.err.println("Run the server with the command: 'make webserver'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}