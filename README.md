- Dependencies
    - Java 11 or higher
    - Docker (for running the webserver instance)
    - `curl` (for downloading the JUnit console launcher)

- Commands
    - `make build` compiles the application classes into `bin/`
    - `make test` downloads JUnit into `lib/`, compiles `/tests`, and runs the test suite


- Project Description
 - This project implements a very simple concurrent downloader of a singular file mounted to a docker container. The functions in the Client class allow the user to fetch the headers as well as the entirety of the class in normal and concurrent mode. Try to play around with the number of threads in order to get the optimal performance improvement, which may be different on different systems. 
 **Important** - ensure that the server is running using the make webserver command before running the tests and the main function. 