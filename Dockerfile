FROM eclipse-temurin:21
EXPOSE 8010
ARG JAR_FILE=target/*.jar
WORKDIR /opt/app
COPY ${JAR_FILE} notivent.jar
ENTRYPOINT ["java","-jar","notivent.jar"]