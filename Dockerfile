FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/weather.jar /weather/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/weather/app.jar"]
