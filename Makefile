clean:
	mvn clean

install:
	mvn install

run:
	java -cp web/target/classes:web/target/dependency/* com.radioteria.Main

test:
	mvn test

deploy:
	mvn heroku:deploy -pl web
