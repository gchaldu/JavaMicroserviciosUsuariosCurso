FROM openjdk:18-jdk-alpine as builder
WORKDIR /app/msvc-usuarios

COPY ./pom.xml /app
COPY ./msvc-usuarios/.mvn ./.mvn
COPY ./msvc-usuarios/mvnw .
COPY ./msvc-usuarios/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline ES PARA LO MISMO QUE LA INSTRUCCION DE ARRIBA
COPY ./msvc-usuarios/src ./src

RUN ./mvnw clean package -DskipTests


#a partir de una imagen llamada "builder" creamos otra mas liviana
FROM openjdk:18-jdk-alpine
WORKDIR /app
COPY --from=builder /app/msvc-usuarios/target/msvc-usuarios-0.0.1-SNAPSHOT.jar .
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]