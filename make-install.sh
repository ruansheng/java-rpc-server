#! /bin/bash

mvn clean package

cp -r ./src/main/resources ./target/
cd ./target
java -jar java-rpc-1.0.0-SNAPSHOT.jar
