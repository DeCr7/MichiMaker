# MichiMaker 🐱

## Descripción del proyecto

MichiMaker es una aplicación móvil desarrollada para Android que permite a los usuarios capturar fotografías, aplicar filtros inspirados en estilos felinos y guardar sus transformaciones personales.

La aplicación ofrece un flujo completo desde la creación de una cuenta hasta la generación y almacenamiento de imágenes modificadas. Los usuarios pueden utilizar la cámara del dispositivo, seleccionar diferentes filtros, agregar descripciones a sus creaciones y consultar estadísticas sobre sus transformaciones realizadas.

La solución está diseñada con una interfaz moderna, utilizando componentes nativos de Android y una arquitectura organizada para separar la interfaz, lógica de negocio y acceso a datos.

---

# Funcionalidades principales

## Gestión de usuarios

La aplicación incluye un sistema de autenticación que permite:

* Crear nuevas cuentas.
* Iniciar sesión mediante usuario y contraseña.
* Mantener una sesión activa.
* Proteger pantallas que requieren autenticación.
* Cerrar sesión.

---

## Captura y edición de imágenes

MichiMaker permite:

* Acceder a la cámara del dispositivo.
* Capturar fotografías.
* Visualizar la imagen antes de aplicar cambios.
* Seleccionar filtros disponibles.
* Generar una nueva versión transformada de la imagen.

---

## Transformaciones

El usuario puede:

* Aplicar filtros visuales.
* Agregar una leyenda personalizada.
* Guardar la transformación realizada.
* Descartar resultados si no desea conservarlos.

---

## Historial

Cada usuario dispone de un historial personal donde puede:

* Consultar sus transformaciones guardadas.
* Visualizar imágenes generadas.
* Ver el filtro utilizado.
* Revisar la fecha y descripción asociada.

---

## Estadísticas

La aplicación genera información sobre el uso del sistema:

* Cantidad total de transformaciones.
* Cantidad de usos por filtro.
* Resumen personalizado por usuario.

---

## Configuración

Incluye opciones como:

* Administración del historial personal.
* Limpieza de transformaciones guardadas.
* Información de la aplicación.
* Cierre de sesión.

---

# Flujo general de la aplicación

```
Inicio
  |
  ├── Login / Registro
  |
  ├── Pantalla principal
  |
  ├── Cámara
  |
  ├── Selección de filtro
  |
  ├── Resultado
  |
  ├── Guardar transformación
  |
  └── Historial / Estadísticas
```

---

# Pantallas principales

## LoginScreen

Módulo encargado del acceso de usuarios.

Características:

* Campos de usuario y contraseña.
* Validación mediante ViewModel.
* Indicador de carga.
* Mensajes de respuesta.
* Redirección automática al iniciar sesión correctamente.

---

## RegisterScreen

Permite crear una nueva cuenta.

Datos registrados:

* Nombre.
* Usuario.
* Correo.
* Contraseña.

La información es enviada mediante un objeto `UsuarioDto`.

---

## HomeScreen

Pantalla principal después de iniciar sesión.

Funciones disponibles:

* Abrir cámara.
* Consultar historial.
* Revisar estadísticas.
* Acceder a configuración.
* Cerrar sesión.

También muestra información del usuario activo.

---

## CameraScreen

Gestiona la captura de imágenes.

Implementa:

* Permisos dinámicos de cámara.
* CameraX.
* Vista previa en tiempo real.
* Captura de fotografías.
* Navegación hacia el módulo de filtros.

---

## FilterScreen

Pantalla encargada de seleccionar transformaciones.

Funciones:

* Carga de imagen capturada.
* Catálogo de filtros.
* Selección del filtro.
* Envío de datos hacia el resultado.

---

## ResultScreen

Procesa y muestra la transformación generada.

Funciones:

* Aplicación del filtro seleccionado.
* Visualización del resultado.
* Escritura de leyendas.
* Guardado de transformación.
* Navegación hacia historial.

---

## HistoryScreen

Consulta las creaciones almacenadas.

Incluye:

* Lista de transformaciones.
* Imágenes guardadas.
* Filtros usados.
* Leyendas personalizadas.

---

## StatsScreen

Muestra estadísticas del usuario.

Información presentada:

* Total de transformaciones.
* Uso agrupado por filtro.

---

## SettingsScreen

Administración de preferencias del usuario.

Permite:

* Limpiar historial.
* Revisar información de la aplicación.
* Cerrar sesión.

---

# Arquitectura

La aplicación utiliza una arquitectura basada en:

## MVVM (Model - View - ViewModel)

Separación de responsabilidades:

### View

Compuesta por pantallas desarrolladas con Jetpack Compose.

Responsabilidades:

* Mostrar interfaz.
* Capturar acciones del usuario.
* Observar estados.

---

### ViewModel

Gestiona:

* Estados de pantalla.
* Procesos asíncronos.
* Comunicación entre UI y datos.

Ejemplos:

* `UserViewModel`
* `CameraViewModel`
* `FilterViewModel`
* `ResultViewModel`
* `TransformacionViewModel`

---

### Repository Pattern

Centraliza el acceso a datos.

Responsabilidades:

* Consultas.
* Guardado de transformaciones.
* Comunicación con servicios externos o almacenamiento.

---

# Tecnologías utilizadas

## Lenguaje

* Kotlin

## Desarrollo Android

* Android Studio
* Jetpack Compose
* Material Design 3

## Arquitectura

* MVVM
* Repository Pattern

## Navegación

* Navigation Component

## Cámara

* CameraX

  * Preview
  * ImageCapture

## Manejo de estado

* Kotlin Coroutines
* StateFlow
* Lifecycle

## Imágenes

* Bitmap
* Base64
* ImageUtils personalizado

## Inteligencia artificial / procesamiento

* ML Kit para funcionalidades relacionadas con filtros y reconocimiento.

---

# Diseño visual

La aplicación utiliza una estética basada en:

* Colores pastel.
* Fondos degradados.
* Componentes Material Design.
* Diseño amigable y creativo.

La identidad visual busca transmitir una experiencia divertida relacionada con la transformación de fotografías en estilos felinos.

---

# Estructura general del proyecto

```
ni.edu.uam.michimaker

├── screens
│   ├── LoginScreen
│   ├── RegisterScreen
│   ├── HomeScreen
│   ├── CameraScreen
│   ├── FilterScreen
│   ├── ResultScreen
│   ├── HistoryScreen
│   ├── StatsScreen
│   └── SettingsScreen
│
├── viewmodel
│   ├── UserViewModel
│   ├── CameraViewModel
│   ├── FilterViewModel
│   ├── ResultViewModel
│   └── StatsScreenViewModel
│
├── repository
│
├── utils
│
├── navigation
│
└── database
```

---

# Objetivo del proyecto

Crear una aplicación móvil interactiva que combine captura multimedia, edición visual y almacenamiento personalizado, utilizando buenas prácticas de desarrollo Android moderno.

# Autores
* Dennis Amaru Cruz Abrego
* Richard Bernard Castro Fonseca