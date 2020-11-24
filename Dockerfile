FROM gradle:6.6.1-jdk11 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean --no-daemon
RUN gradle build --no-daemon

FROM openjdk:11-jre-slim

EXPOSE 8080

ENV ENVIRONMENT=PROD

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/fashionscape-bot.jar

ENTRYPOINT ["java","-jar","-Xmx800m","/app/fashionscape-bot.jar"]
