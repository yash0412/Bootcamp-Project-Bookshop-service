FROM gcr.io/distroless/java17-debian12

COPY ./build/libs/bookshop-service-1.0-SNAPSHOT.jar ./service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "service.jar"]