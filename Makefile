	USERNAME=salmonllama
	IMAGE=fsbot
	TAG := $(shell git describe --tags)

docker:
	echo ${TAG}
	docker build -t ${USERNAME}/${IMAGE}:${TAG} .