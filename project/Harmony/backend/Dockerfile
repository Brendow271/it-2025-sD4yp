# Multi-stage build для оптимизации размера образа
FROM maven:3.8-openjdk-17 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml для кэширования зависимостей
COPY pom.xml .

# Загружаем зависимости (этот слой будет кэшироваться)
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests -B

# Production stage
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Создаем пользователя для безопасности
RUN adduser --disabled-password --gecos '' --shell /bin/bash appuser && \
    chown -R appuser:appuser /app

# Копируем собранное приложение из build stage
COPY --from=build /app/target/harmony-backend.war ./app.war

# Устанавливаем права доступа
RUN chown appuser:appuser app.war

# Переключаемся на пользователя appuser
USER appuser

# Открываем порт
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1

# Точка входа
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.war"]
