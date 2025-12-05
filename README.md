# Notes API - Sistema de Gestión de Notas

API REST desarrollada en Spring Boot para gestionar notas con
autenticación JWT y control de acceso basado en roles.

## 📌 Características

-   **Autenticación JWT** con login/registro (Access + Refresh tokens)
-   **Control de Acceso por Roles:** USER y ADMIN
-   **Gestión de Notas:** CRUD con ownership
-   **Categorías:** Organización de notas por categoría
-   **Seguridad:** Spring Security + BCrypt
-   **CORS:** Configuración lista para React/Angular

## 🛠️ Tecnologías

-   Java 17+
-   Spring Boot 3.x
-   Spring Security
-   Spring Data JPA
-   JWT (jjwt 0.12.x)
-   MySQL / PostgreSQL
-   Lombok
-   Maven

## 📂 Estructura del Proyecto

    src/main/java/
     ├── controller/
     │   ├── AdminController.java
     │   ├── AuthController.java
     │   ├── CategoryController.java
     │   └── NoteController.java
     ├── dto/
     ├── entity/
     ├── exception/
     ├── repository/
     ├── service/
     └── security/

## ⚙️ Configuración

Archivo `application.yaml`:

spring:
  application:

    name: categorized_notes_app

  datasource:
    
    url: jdbc:mysql://localhost:3306/categorized_notes_app?useSSL=false&serverTimezone=UTC&
    allowPublicKeyRetrieval=true
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:}
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:

    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQLDialect

  jwt:

    secret: ${JWT_SECRET:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}
    expiration: ${JWT_EXPIRATION:86400000} # 24 horas en milisegundos

## 🚀 Instalación

``` bash
git clone <repo>
cd notes-api
mvn clean install
mvn spring-boot:run
```

API disponible en: `http://localhost:8080`

## 📚 Endpoints Principales

### Autenticación

-   POST `/api/auth/register`
-   POST `/api/auth/login`
-   POST `/api/auth/refresh`

### Categorías

-   GET `/api/categories`
-   POST `/api/categories` *(ADMIN)*

### Notas

-   GET `/api/notes/me`
-   POST `/api/notes`
-   PUT/DELETE `/api/notes/{id}` *(propias o admin)*

## 🔐 Seguridad

-   BCrypt para contraseñas
-   JWT firmados HMAC-SHA256
-   CORS para localhost:3000 y 4200
-   Stateless session

## 📄 Swagger

Accede a:

-   `/swagger-ui.html`
-   `/v3/api-docs`