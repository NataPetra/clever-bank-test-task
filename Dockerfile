FROM gradle:8.3.0-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon
# If copy nothing in Run stage this command will help find where .war was created
RUN pwd && find . -name "*.war"
RUN pwd && find . -name "config.yml"

# Run stage
FROM tomcat:9.0
ARG WAR_FILE=/home/gradle/src/build/libs/*.war
COPY --from=build $WAR_FILE $CATALINA_HOME/webapps/app.war