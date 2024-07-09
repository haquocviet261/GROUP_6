FROM  jenkins/jenkins:lts
USER root
ARG JAR_FILE=target/*jar
COPY ./target/project-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

