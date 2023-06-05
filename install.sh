#!/usr/bin/env sh
mvn clean
mvn install -DskipTests
docker rmi eu.gcr.io/brave-drive-388410/chatbot-java-image
docker build -t eu.gcr.io/brave-drive-388410/chatbot-java-image .