#!/bin/bash

mvn clean 

mvn compile

mvn package

cd target/

reset

java -jar proyecto2.jar ~/Documents/EDD/Proyecto2/ejemplo.txt


