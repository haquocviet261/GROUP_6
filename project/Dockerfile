# Sử dụng JDK phù hợp để chạy ứng dụng Spring Boot
FROM jenkins/jenkins:lts
USER root
# Khai báo biến ARG để chỉ định file JAR
ARG JAR_FILE=target/*.jar

# Sao chép file JAR từ thư mục build vào container
COPY ${JAR_FILE} app.jar

# Khởi chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/app.jar"]


