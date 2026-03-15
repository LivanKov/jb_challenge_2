- Dependencies
    - Java 11 or higher
    - Docker (for running the webserver instance)
    - `curl` (for downloading the JUnit console launcher)

- Commands
    - `make build` compiles the application classes into `bin/`
    - `make test` downloads JUnit into `lib/`, compiles `/tests`, and runs the test suite
