.PHONY: build
build:
	docker run --rm -v $$(pwd):/usr/bin/app:rw niaquinto/gradle clean fatjar

build_image:
	docker build -t dev.docker.points.com/newrelic-elasticsearch .

push: build_image
	docker push dev.docker.points.com/newrelic-elasticsearch
