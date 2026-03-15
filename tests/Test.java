import static org.junit.jupiter.api.Assertions.*;

// Ensure that the webserver is running prior to executing the tests

class Test {

    // Validate that the correct file length is fetched via the HEAD call
    @org.junit.jupiter.api.Test
    void validateCorrectFileLength() {

        int jsonFileLen = 340474;
        var server = new ServerInstance();
        String[] headers = null;
        
        try {
            headers = server.fetchHeaders("big_json.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(jsonFileLen, Integer.parseInt(headers[0]));
    }

    // Validate that the file is fetched properly via concurrent download
    @org.junit.jupiter.api.Test
    void validateConcurrentDownload() {
        var server = new ServerInstance();
        byte[] fileContent = null;
        try {
            fileContent = Client.getFileConcurrent(server, "big_json.json", 8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(fileContent);
        assertTrue(Client.validateFileContent("big_json.json", fileContent));
    }
}
