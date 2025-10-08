FROM ${CI_DEPENDENCY_PROXY_GROUP_IMAGE_PREFIX_SLASH}openjdk:21-slim

WORKDIR /simurbs

EXPOSE 8080

# copy the jar from the build/libs dir into /simurbs
COPY build/libs/simurbs-0.0.1-SNAPSHOT.jar /simurbs/simurbs-mamis.jar

ENTRYPOINT ["java", "-jar", "simurbs-mamis.jar"]

