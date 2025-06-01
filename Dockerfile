FROM maven:3.9.3-eclipse-temurin-17 AS build

ARG WORK_DIR=app
ARG SOURCE_DIR=src

WORKDIR /${WORK_DIR}

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./${SOURCE_DIR}
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine AS run

ARG WORK_DIR=app
ARG SOURCE_DIR=src
ARG VERSION=0.0.1
ARG JAR_FILE=target/account-${VERSION}.jar

WORKDIR /${WORK_DIR}

COPY --from=build /${WORK_DIR}/${JAR_FILE} account.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "account.jar"]