#!/usr/bin/env sh
mvn clean
mvn install -DskipTests
docker rmi eu.gcr.io/neural-sol-391812/chatbot-java-image
docker build -t eu.gcr.io/neural-sol-391812/chatbot-java-image .