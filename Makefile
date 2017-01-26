IMAGE:="peacefulbit/radioteria-service"
NAME:="radioteria_service"
USER_ID:=$(shell id -u)
GROUP_ID:=$(shell id -g)

docker-build:
	docker build --build-arg USER_ID=$(USER_ID) --build-arg GROUP_ID=$(GROUP_ID) -t $(IMAGE) .

docker-run:
	docker run --rm -v $(CURDIR):/app -v /tmp/.m2:/home/app/.m2 -p 8080:8080 -ti --name $(NAME) -w /app $(IMAGE) $(COMMAND)

clean:
	mvn clean

install:
	mvn install

start:
	mvn install exec:java

test:
	mvn test

deploy:
	mvn heroku:deploy
