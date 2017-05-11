.PHONY: build
build:
	docker run --rm -v $$(pwd):/usr/bin/app:rw niaquinto/gradle clean fatjar
	docker run --rm -v $$(pwd):/usr/bin/app:rw --entrypoint=bash niaquinto/gradle -c "chown -R $$(id -u):$$(id -g) /usr/bin/app/build"

build_image:
	docker build -t dev.docker.points.com/newrelic-elasticsearch .

push: build_image
	docker push dev.docker.points.com/newrelic-elasticsearch
