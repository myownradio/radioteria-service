clean:
	mvn clean

install:
	mvn install

run:
	mvn tomcat7:run

test:
	mvn test -pl data
