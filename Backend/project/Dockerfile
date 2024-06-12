FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*jar
COPY ./target/project-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

FROM jenkins/jenkins
USER root
RUN curl -L  "https://github.com/docker/compose/releases/download/1.25.3/docker-compose-$(uname -s)-$(uname -m)" \  -o /usr/local/bin/docker-compose  && chmod +x /usr/local/bin/docker-compose
USER jenkins