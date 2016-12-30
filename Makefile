clean:
	mvn clean

install:
	mvn install

run:
	java -cp web/target/classes/ com.radioteria.Main

test:
	mvn test
