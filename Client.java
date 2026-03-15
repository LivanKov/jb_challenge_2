public class Client {


    public static String[] getFileHeaders(ServerInstance server, String fileName) {
        try {
            String[] headers = server.fetchHeaders(fileName);
            System.out.println("Content-Length: " + headers[0]);
            System.out.println("Accept-Ranges: " + headers[1]);
            return headers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getFileConcurrent(ServerInstance server, String fileName, int threadCount) {
        int fileSize = Integer.parseInt(getFileHeaders(server, fileName)[0]);
        
        int activeThreadCount = Math.min(threadCount, fileSize);
        byte[][] chunks = new byte[activeThreadCount][];

        Thread[] threads = new Thread[activeThreadCount];
        int baseChunkSize = fileSize / activeThreadCount;
        int remainder = fileSize % activeThreadCount;
        int from = 0;

        for (int i = 0; i < activeThreadCount; i++) {
            int chunkSize = baseChunkSize + (i < remainder ? 1 : 0);
            int start = from;
            int end = start + chunkSize - 1;
            int chunkIndex = i;
            threads[i] = new Thread(() -> {
                chunks[chunkIndex] = getFile(server, fileName, start, end);
            });
            threads[i].start();
            from = end + 1;
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Concurrent download interrupted", e);
            }
        }

        byte[] reconstructedFile = new byte[fileSize];
        int offset = 0;
        for (byte[] chunk : chunks) {
            System.arraycopy(chunk, 0, reconstructedFile, offset, chunk.length);
            offset += chunk.length;
        }

        return reconstructedFile;
    }

    public static byte[] getFile(ServerInstance server, String fileName) {
        try {
            byte[] fileBytes = server.getFile(fileName);
            System.out.println("File content as bytes: " + new String(fileBytes));
            return fileBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getFile(ServerInstance server, String fileName, int from, int to) {
        try {
            byte[] fileBytes = server.getFile(fileName, from, to);
            System.out.println("File content as bytes: " + new String(fileBytes));
            return fileBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean validateFileContent(String fileName, byte[] fetchedContent) {
        byte[] expectedBytes = getFile(new ServerInstance(), fileName);
        return java.util.Arrays.equals(fetchedContent, expectedBytes);
    }


    public static void main(String[] args) {
        var server = new ServerInstance();
        byte[] fileBytes = Client.getFileConcurrent(server, "big_json.json", 4);
        boolean isValid = validateFileContent("big_json.json", fileBytes);
        System.out.println("File content validation result: " + isValid);
    }
}
