FROM openjdk:11.0.3

RUN mkdir /app
COPY server/target/server.jar /app

WORKDIR /app

ENV PORT 8080
ENV JAVA_OPTS "-Xmx300m -Xss512k"

CMD ["sh", "-c", "java -XX:+UseContainerSupport $JAVA_OPTS -XX:TieredStopAtLevel=1 -Dserver.port=$PORT -jar server.jar"]