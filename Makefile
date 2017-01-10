IMAGE:="peacefulbit/radioteria-service"
NAME:="radioteria_service"
USER_ID:=$(shell id -u)
GROUP_ID:=$(shell id -g)

docker-build:
	docker build --build-arg USER_ID=$(USER_ID) --build-arg GROUP_ID=$(GROUP_ID) -t $(IMAGE) .

start-env:
	docker run -ephemeral -v $(CURDIR):/app -v /tmp/.m2:/home/app/.m2 -p 8080:8080 -ti -w /app $(IMAGE)

clean:
	mvn clean

install:
	mvn install

start:
	mvn install exec:java -pl web

test:
	mvn test

deploy:
	mvn heroku:deploy -pl web
