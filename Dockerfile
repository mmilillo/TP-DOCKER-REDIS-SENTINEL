
#
# Build stage
#

FROM maven:3-jdk-8 AS build
#no anda

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip

#
# Package stage
#

#FROM maven:3-jdk-8
#COPY --from=build /home/app/target/noescalapp-1.0.jar /usr/local/lib/noescalapp.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/usr/local/lib/noescalapp.jar"]

# esto no va
#COPY src .
#COPY .mvn .
#COPY mvnw .
#COPY mvnw.cmd .
#COPY pom.xml .



# si copio asi anda
#COPY . .




























#COPY src /usr/src/app/src
#COPY pom.xml /usr/src/app
#COPY mvnw /usr/src/app
#COPY mvnw.cmd /usr/src/app
#COPY pom.xml /usr/src/app



