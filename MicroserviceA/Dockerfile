FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /code
ADD target/microservice-a-1.0-SNAPSHOT-spring-boot.jar app.jar
VOLUME /code/src/main/webapp/WEB-INF
COPY src/main/webapp/WEB-INF/web.xml /code/src/main/webapp/WEB-INF/web.xml
WORKDIR /code
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]