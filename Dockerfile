FROM openjdk:17-jdk-slim
RUN addgroup spring-boot-group && adduser --ingroup spring-boot-group spring-boot
USER spring-boot:spring-boot-group
VOLUME /tmp
ARG JAR_FILE=target/crud-0.0.1-SNAPSHOT.jar
WORKDIR /app
EXPOSE 8080
COPY ${JAR_FILE} app.jar
COPY target/dependency ./lib
ENTRYPOINT ["java","-jar","/app/app.jar"]
