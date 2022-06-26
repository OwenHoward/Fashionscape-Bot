FROM gradle:7.2.0-jdk16 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean --no-daemon
RUN gradle build --no-daemon

FROM openjdk:16

EXPOSE 8080

ENV ENVIRONMENT=PROD

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/fashionscape-bot.jar

ENTRYPOINT ["java","-jar","-Xmx800m","/app/fashionscape-bot.jar"]
