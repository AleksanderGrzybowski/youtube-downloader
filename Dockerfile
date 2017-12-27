FROM openjdk:8-jdk

COPY . /app
WORKDIR /app

RUN ./gradlew clean bootRepackage

FROM openjdk:8-jre

RUN apt-get update && apt-get -y install libav-tools python-minimal
RUN wget https://yt-dl.org/downloads/latest/youtube-dl -O /usr/local/bin/youtube-dl && chmod a+rx /usr/local/bin/youtube-dl

COPY --from=0 /app/build/libs/app-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]
