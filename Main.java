public class Main {
    public static void main(String[] args) {
        ServerInstance server = new ServerInstance("Server1", "192.168.1.101");
        try {
            byte[] fileBytes = server.getFile("ex1.json");
            System.out.println("File content as bytes: " + new String(fileBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}