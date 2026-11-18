# Estudio de Derecho — Android

Aplicación Android nativa, diseñada para funcionar sin conexión. La base de preguntas se incluye en `app/src/main/assets/preguntas.json` y se importa a una base de datos SQLite local en el primer inicio.

## Compilar

Desde esta carpeta, con Android SDK, JDK 17 y Gradle 8.7:

`gradle assembleDebug`

El APK de depuración queda en `app/build/outputs/apk/debug/app-debug.apk`.
