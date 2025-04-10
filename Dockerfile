FROM openjdk:21-jdk-slim

WORKDIR /app

COPY . /app/

RUN apt-get update && apt-get install maven -y

RUN mvn clean compile -DskipTests

RUN mvn clean package -DskipTests

EXPOSE 8083

ENTRYPOINT ["java","-jar","/app/target/springbootproject1-0.0.1-SNAPSHOT.jar"]


















