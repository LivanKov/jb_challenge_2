BIN_DIR := bin
TEST_BIN_DIR := bin-tests
LIB_DIR := lib
JUNIT_VERSION := 1.10.2
JUNIT_JAR := $(LIB_DIR)/junit-platform-console-standalone-$(JUNIT_VERSION).jar
MAIN_SOURCES := $(wildcard *.java)
TEST_SOURCES := $(shell find tests -name '*.java' 2>/dev/null)

.PHONY: build clean run webserver stop junit test

build:
	mkdir -p $(BIN_DIR)
	javac -d $(BIN_DIR) $(MAIN_SOURCES)

junit: $(JUNIT_JAR)

$(JUNIT_JAR):
	mkdir -p $(LIB_DIR)
	curl -L -o $(JUNIT_JAR) https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/$(JUNIT_VERSION)/junit-platform-console-standalone-$(JUNIT_VERSION).jar

test: build junit
	mkdir -p $(TEST_BIN_DIR)
	javac -cp $(BIN_DIR):$(JUNIT_JAR) -d $(TEST_BIN_DIR) $(TEST_SOURCES)
	java -jar $(JUNIT_JAR) --class-path $(BIN_DIR):$(TEST_BIN_DIR) --scan-class-path

clean:
	rm -rf $(BIN_DIR) $(TEST_BIN_DIR)

run: build
	java -cp $(BIN_DIR) Client

webserver:
	docker run --rm -p 8080:80 -d --name jb_challenge_2_container -v $(PWD)/dir/:/usr/local/apache2/htdocs/ httpd:latest

stop:
	docker stop jb_challenge_2_container
