# ARGUMENTS
ARG app_name=demo
ARG app_port=8080

# ARTIFACT BUILDER
FROM maven:3.5.2-jdk-8 AS buildArtifact
ARG app_name
ENV APP_NAME=$app_name
ARG app_version
ENV APP_VERSION=$app_version
LABEL version=${app_version}-BUILD
LABEL image=buildArtifact
COPY ./src /usr/src/${APP_NAME}/src
COPY pom.xml /usr/src/${APP_NAME}/pom.xml
RUN mvn -f /usr/src/${APP_NAME}/pom.xml \
        versions:set -DnewVersion=$APP_VERSION \
        clean package \
        surefire-report:report-only \
        site -DgenerateReports=false

# RUN-CONTAINER BUILDER
FROM docker.io/openjdk:8-jre-alpine AS buildRunContainer
ARG app_name
ARG app_port
ENV APP_NAME=$app_name
ENV APP_PORT=$app_port
LABEL image=buildRunContainer
COPY --from=buildArtifact /usr/src/${APP_NAME}/target/${APP_NAME}.jar /usr/${APP_NAME}/${APP_NAME}.jar
WORKDIR /usr/${APP_NAME}
EXPOSE ${APP_PORT}
RUN apk update && apk add bash curl
CMD java \
        -Dspring.profiles.active="${SPRING_ACTIVE_PROFILES}" \
        -DPORT=${APP_PORT}                                   \
        -jar ${APP_NAME}.jar