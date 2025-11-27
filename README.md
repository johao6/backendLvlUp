# Level UP - Backend API

API REST para e-commerce de productos gamer desarrollada con Spring Boot.

## 🚀 Despliegue en Producción

**URL Base:** `http://levelup.us-east-1.elasticbeanstalk.com`

### Tecnologías
- Java 21 (Amazon Corretto)
- Spring Boot 3.5.7
- PostgreSQL (AWS RDS)
- AWS Elastic Beanstalk

## 📡 Endpoints Principales

### Públicos
- `GET /api/productos` - Lista de productos
- `GET /api/productos/{id}` - Producto por ID
- `GET /api/categorias` - Categorías
- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Registro
- `GET /swagger-ui.html` - Documentación API

### Protegidos (requieren JWT)
- `POST /api/productos` - Crear producto
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto
- `GET /api/carrito/{usuarioId}` - Ver carrito
- `POST /api/carrito/{usuarioId}/{productoId}` - Agregar al carrito

## 🛠️ Desarrollo Local

### Prerrequisitos
- Java 21
- Maven 3.9+
- PostgreSQL

### Ejecutar localmente
```bash
./mvnw spring-boot:run
```

### Empaquetar como JAR
```bash
./mvnw clean package -DskipTests
```

El JAR se genera en: `target/demo-0.0.1-SNAPSHOT.jar`

## 🔐 Variables de Entorno (Producción)

```
RDS_DB_URL=jdbc:postgresql://db-levelup.chguglymjysp.us-east-1.rds.amazonaws.com:5432/levelup
RDS_USERNAME=levelup
RDS_PASSWORD=***
SPRING_PROFILES_ACTIVE=prod
```

## 📦 Desplegar Nueva Versión

1. Empaquetar: `./mvnw clean package -DskipTests`
2. En AWS Beanstalk Console → Upload and Deploy
3. Seleccionar: `target/demo-0.0.1-SNAPSHOT.jar`

## 👤 Usuario de Prueba

```
Email: admin@test.com
Password: password123
```

## 📁 Estructura del Proyecto

```
src/main/java/com/levelup/backend/
├── config/          # Configuración (CORS, Swagger, LoadDatabase)
├── controllers/     # Controladores REST
├── dto/             # Data Transfer Objects
├── exceptions/      # Manejo de excepciones
├── models/          # Entidades JPA
├── repositories/    # Repositorios JPA
├── security/        # Configuración de seguridad y JWT
├── services/        # Lógica de negocio
└── utils/           # Utilidades (compresión de imágenes)
```

## 📝 Notas

- Las imágenes de productos están almacenadas en AWS S3
- La base de datos está en AWS RDS (PostgreSQL)
- Las tablas se crean automáticamente con `ddl-auto=validate`
- Los datos iniciales se cargan solo si no existen (idempotente)


