FROM ubuntu:22.04 AS build

RUN apt-get update \
  && apt-get install openjdk-17-jdk maven -y \
  && rm -rf /var/lib/apt/lists/* /var/cache/apt/archives/*

COPY . .
 
RUN mvn clean install

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/todolist-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]