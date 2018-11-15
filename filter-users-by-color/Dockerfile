FROM openjdk:11

ARG DEPENDENCY_DIR=target/dependency
ARG CLASSES_DIR=target/classes

COPY ${DEPENDENCY_DIR} /app/dependency
COPY ${CLASSES_DIR} /app/classes

ENTRYPOINT ["java", "-cp", "/app/dependency/*:/app/classes", "com.github.wojciechfiszer.kafkastreamsjava11.filterusersbycolor.Main"]