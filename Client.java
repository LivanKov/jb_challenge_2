public class Client {


    public static String[] getFileHeaders(ServerInstance server, String fileName) {
        try {
            return server.fetchHeaders(fileName);
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
            return server.getFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getFile(ServerInstance server, String fileName, int from, int to) {
        try {
            return server.getFile(fileName, from, to);
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
        String fileName = "big_json.json";
        int threadCount = 4;

        long sequentialStart = System.nanoTime();
        byte[] sequentialBytes = Client.getFile(server, fileName);
        long sequentialDuration = System.nanoTime() - sequentialStart;

        long concurrentStart = System.nanoTime();
        byte[] concurrentBytes = Client.getFileConcurrent(server, fileName, threadCount);
        long concurrentDuration = System.nanoTime() - concurrentStart;

        boolean isValid = java.util.Arrays.equals(sequentialBytes, concurrentBytes);
        double sequentialMillis = sequentialDuration / 1_000_000.0;
        double concurrentMillis = concurrentDuration / 1_000_000.0;
        double speedup = (double) sequentialDuration / concurrentDuration;

        System.out.println("Sequential fetch time: " + sequentialMillis + " ms");
        System.out.println("Concurrent fetch time (" + threadCount + " threads): " + concurrentMillis + " ms");
        System.out.println("Concurrent result matches sequential result: " + isValid);
        System.out.println("Speedup: " + speedup + "x");
    }
}
