	USERNAME=salmonllama
	IMAGE=fsbot
	TAG := $(shell git describe --tags)
	BUILD=${USERNAME}/${IMAGE}:${TAG}
	LATEST=${USERNAME}/${IMAGE}:latest

docker:
	echo ${TAG}
	docker build -t ${BUILD} .
	docker tag ${BUILD} ${LATEST}
	docker push ${BUILD}
	docker push ${LATEST}