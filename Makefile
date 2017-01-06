clean:
	mvn clean

install:
	mvn install

run:
	java -cp web/target/dependency/*:web/target/web-1.0.jar com.radioteria.Main

test:
	mvn test

deploy:
	mvn heroku:deploy -pl web
