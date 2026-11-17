# Estudio de Derecho — Fase Privada

Aplicación web estática para estudiar preguntas y respuestas del material de preparación para la fase privada.

## Funciones implementadas
- Muestra una pregunta y su respuesta al mismo tiempo.
- Botón **Siguiente**.
- Orden aleatorio sin repetir preguntas dentro de cada recorrido.
- Al terminar el banco, inicia automáticamente un nuevo ciclo aleatorio.
- Contador de avance y barra de progreso.
- Permite cargar un banco de preguntas en formato JSON.

## Formato del banco de preguntas

La aplicación espera un archivo JSON con esta estructura:

```json
[
  {
    "pregunta": "Texto literal de la pregunta",
    "respuesta": "Texto literal de la respuesta"
  }
]
```

En la pantalla, selecciona **Cargar banco de preguntas (.json)** para importar el archivo.

## Estado del contenido
La interfaz y la lógica de estudio ya están creadas. El banco definitivo de las 1,000 preguntas todavía está pendiente de extraerse y verificarse contra el PDF original; no se han inventado preguntas de muestra.

## Publicar con GitHub Pages
En el repositorio, entra a **Settings → Pages**. En **Build and deployment**, selecciona **Deploy from a branch**, elige la rama **main** y la carpeta **/(root)** y guarda. Cuando GitHub termine el despliegue, mostrará la dirección pública de la aplicación.
