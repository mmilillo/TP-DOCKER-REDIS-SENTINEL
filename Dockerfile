
FROM maven:3-jdk-8 AS build


COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip



























#COPY src /usr/src/app/src
#COPY pom.xml /usr/src/app
#COPY mvnw /usr/src/app
#COPY mvnw.cmd /usr/src/app
#COPY pom.xml /usr/src/app



