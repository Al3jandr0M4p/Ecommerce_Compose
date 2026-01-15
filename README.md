# Ecommerce Compose

Aplicación de comercio electrónico multiplataforma desarrollada en Android con Kotlin y Jetpack Compose.

## Descripción

Ecommerce Compose es una aplicación móvil de e-commerce que permite a los usuarios comprar productos de diferentes negocios locales, gestionar pedidos, realizar pagos y recibir entregas a domicilio. La aplicación cuenta con múltiples roles de usuario: cliente, negocio, repartidor y administrador.

## Características Principales

- **Autenticación**: Registro e inicio de sesión con Supabase Auth
- **Catálogo de productos**: Visualización de productos por negocio
- **Carrito de compras**: Gestión de productos seleccionados
- **Proceso de pago**: Sistema de pagos integrado
- **Gestión de pedidos**: Seguimiento de estado de pedidos
- **Roles de usuario**: Cliente, Negocio, Repartidor, Administrador
- **Notificaciones push**: Firebase Cloud Messaging
- **Geolocalización**: Mapbox y Google Maps para ubicaciones
- **Interfaz moderna**: Material 3 con Jetpack Compose

## Tecnologías

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Backend**: Supabase (PostgreSQL, Auth, Realtime, Storage, Functions)
- **Networking**: Ktor Client
- **Navegación**: Navigation Compose
- **Imágenes**: Coil
- **Mapas**: Mapbox
- **Notificaciones**: Firebase Cloud Messaging
- **Arquitectura**: Clean Architecture + MVVM
- **Build**: Gradle 8.13

## Api
```bash
EndPoints
api/payments
    / # EndPoint para verificar que la api esta activa
    /coupons/aply # EndPoint para aplicar los cupones
    
api/orders
    / # EndPoint para crear una orden
    /:orderId/invoice # EndPoint para debugear los datos de la factura
    /:orderId/invoice/pdf # EndPoint para descargar la factura del lado del cliente por id de la orden
```

## Requisitos Previos

- Android Studio Ladybug o superior
- JDK 11 o superior
- Android SDK 26+ (minSdk 26)
- Gradle 8.13
- Cuenta de Supabase
- Cuenta de Firebase (para notificaciones)
- Cuenta de Mapbox (opcional, para mapas)

## Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Al3jandr0M4p/Ecommerce_Compose.git
   cd Ecommerce_Compose
   ```

2. **Abrir en Android Studio**
   - File > Open > Seleccionar la carpeta del proyecto
   - Esperar a que se sincronicen las dependencias

3. **Configurar variables de entorno**
   
   Crear archivo `local.properties` en la raíz del proyecto:
   ```properties
   SUPABASE_URL=tu_url_de_supabase
   SUPABASE_KEY=tu_anon_key_de_supabase
   MAPBOX_ACCESS_TOKEN=tu_token_de_mapbox (opcional) no es una variable de entorno es un token xml
   ```

4. **Configurar Firebase**
   
   Descargar el archivo `google-services.json` desde Firebase Console y colocarlo en:
   ```
   app/google-services.json
   ```

5. **Ejecutar la aplicación**
   ```bash
   ./gradlew assembleDebug
   ```
   O desde Android Studio: Run > Run 'app'

## Estructura del Proyecto

```
Ecommerce_Compose/
├── app/
│   ├── src/main/java/com/clay/ecommerce_compose/
│   │   ├── activity/           # Actividades principales
│   │   ├── data/               # Capa de datos
│   │   │   ├── remote/         # Configuración API
│   │   │   └── repository/     # Repositorios
│   │   ├── domain/             # Capa de dominio
│   │   │   ├── model/          # Modelos de datos
│   │   │   └── usecase/        # Casos de uso
│   │   ├── navigation/         # Navegación
│   │   ├── notifications/      # Notificaciones
│   │   ├── ui/                 # Capa de presentación
│   │   │   ├── components/     # Componentes reutilizables
│   │   │   ├── screens/        # Pantallas
│   │   │   └── theme/          # Temas y estilos
│   │   └── utils/              # Utilidades
│   └── src/test/               # Tests unitarios
├── gradle/                     # Configuración Gradle
├── build.gradle.kts           # Build del proyecto
├── settings.gradle.kts        # Configuración settings
└── gradle.properties          # Propiedades Gradle
```

## Roles de Usuario

### Cliente
- Explorar negocios y productos
- Agregar al carrito
- Realizar pedidos
- Seguir estado de entregas

### Negocio
- Gestionar productos
- Ver pedidos recibidos
- Actualizar estado de preparación

### Repartidor
- Ver pedidos disponibles
- Aceptar entregas
- Actualizar estado de entrega

### Administrador
- Gestionar usuarios
- Administrar negocios
- Estadísticas globales

## Configuración de Supabase

1. Crear proyecto en [Supabase](https://supabase.com)
2. Configurar tablas: users, products, orders, businesses, deliveries
3. Habilitar Auth, Realtime, Storage y Functions
4. Copiar URL y anon key a `local.properties`

## Configuración de Firebase

1. Crear proyecto en [Firebase Console](https://console.firebase.google.com)
2. Agregar aplicación Android
3. Descargar `google-services.json`
4. Habilitar Firebase Cloud Messaging

## Construir Release

```bash
./gradlew assembleRelease
```

El APK se generará en: `app/build/outputs/apk/release/`

## Testing

```bash
# Tests unitarios
./gradlew test

# Tests instrumentados
./gradlew connectedAndroidTest
```

## Contribuciones

1. Fork del repositorio
2. Crear rama: `git checkout -b feature/nueva-caracteristica`
3. Commit: `git commit -m 'Agregar nueva característica'`
4. Push: `git push origin feature/nueva-caracteristica`
5. Crear Pull Request

## Licencia

MIT License - ver archivo LICENSE para más detalles.

## Contacto

- Autor: Clay
- Email: molle0711@gmail.com
