FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/moneymanager-0.0.1-SNAPSHOT.jar momeymanager-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","momeymanager-v1.0"]
