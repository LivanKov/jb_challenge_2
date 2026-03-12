build:
	javac -d bin *.java

clean:
	rm -rd bin

run:
	java -cp bin Main

webserver:
	docker run --rm -p 8080:80 -d --name jb_challenge_2_container -v $(PWD)/dir/:/usr/local/apache2/htdocs/ httpd:latest

stop:
	docker stop jb_challenge_2_container