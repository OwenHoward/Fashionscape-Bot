FROM gradle:6.6.1-jdk11 AS build

ENV ENVIRONMENT=PROD

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:11-jre-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/fsbot*.jar /app/

ENTRYPOINT ["java","-jar","-Xmx800m","/app/fsbot*.jar"]
