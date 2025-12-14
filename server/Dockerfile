# Stage 1: Build React klienta
FROM node:18-alpine as frontend-build

WORKDIR /app

# Kopíruj do kontejneru složku s Reactem (client)
COPY ../client/package*.json ./
RUN npm install

COPY ../client/ ./
RUN npm run build

# Stage 2: Build Spring Boot backend
FROM maven:3.9.1-eclipse-temurin-17 as backend-build

WORKDIR /app

# Kopíruj backendovy pom.xml a zdrojaky
COPY ./pom.xml ./
COPY ./src ./src

RUN mvn clean package -DskipTests

# Stage 3: Final image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Z backend buildu zkopíruj jar
COPY --from=backend-build /app/target/*.jar app.jar

# Z frontend buildu zkopíruj sestavený React do static resources backendu
COPY --from=frontend-build /app/build /app/build

# Pokud tvůj Spring Boot server servíruje React build např. z /app/build, tak to bude dostupné

# Nastav port (podle konfigurace Spring Boot, např. 8080)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]