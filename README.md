# DynamicMemoryAI2

Aplicación Java basada en Maven para almacenar y navegar imágenes en memoria usando una lista doblemente enlazada.

El proyecto genera 5 imágenes de prueba de forma programática, las guarda en una estructura de datos propia y muestra una interfaz gráfica simple con Swing para recorrerlas.

## Características

- Gestión de imágenes completamente en memoria.
- Estructura propia de lista doblemente enlazada.
- Generación programática de imágenes de prueba sin depender de archivos externos.
- Interfaz gráfica con Swing para ver la imagen actual y navegar entre las anteriores y siguientes.
- Pruebas unitarias e integradas con JUnit 5, jqwik y Mockito.
- Uso de clases genéricas para reutilizar la estructura de lista y el nodo con distintos tipos de datos.

## Tecnologías utilizadas

- Java 19
- Maven
- Swing
- Lombok
- JUnit 5
- jqwik
- Mockito

## Estructura del proyecto

- `src/main/java/com/listimage/Main.java`: punto de entrada de la aplicación.
- `src/main/java/com/listimage/model/Image.java`: modelo de imagen en memoria.
- `src/main/java/com/listimage/list/`: contrato e implementación de la lista doblemente enlazada.
- `src/main/java/com/listimage/service/`: capa de servicio para gestionar imágenes.
- `src/main/java/com/listimage/ui/SwingView.java`: interfaz gráfica de navegación.
- `src/main/java/com/listimage/util/ImageFactory.java`: generador de imágenes de prueba.
- `src/test/java/`: pruebas unitarias e integración.

## Requisitos

- Java 19 o superior.
- Maven 3.8+.

## Ejecución

1. Compilar el proyecto:

```bash
mvn clean package
```

2. Ejecutar la aplicación:

```bash
java -jar target/listImage-1.0-SNAPSHOT.jar
```

También puedes ejecutarla directamente desde Maven:

```bash
mvn exec:java
```

> Nota: el JAR se genera con `com.listimage.Main` como clase principal.

## Pruebas

Para ejecutar toda la suite de pruebas:

```bash
mvn test
```

## Cómo funciona

1. `Main` crea una instancia de `ImageFactory`.
2. `ImageFactory` genera 5 imágenes de prueba en memoria.
3. Las imágenes se agregan a un `ImageManager`, que delega en una `DoublyLinkedList`.
4. `SwingView` muestra la imagen actual y permite navegar con los botones `Anterior`, `Siguiente` y `Cerrar`.

## Uso de clases genéricas

Este proyecto utiliza clases genéricas en componentes como `LinkedList<T>` y `Node<T>`. Eso permite que la estructura de datos no dependa solo de imágenes, sino que pueda reutilizarse con otros tipos de objetos en el futuro.

### Cuándo es buena idea usar clases genéricas

- Cuando quieres escribir una estructura reutilizable para distintos tipos de datos.
- Cuando buscas evitar duplicar código para tipos similares.
- Cuando la lógica de la clase no depende de un tipo concreto, sino del comportamiento general de un conjunto de elementos.
- Cuando necesitas seguridad de tipos en compilación sin hacer conversiones manuales.

### Cuándo no es buena idea usarlas

- Cuando la clase solo tendrá un único tipo de uso y no se va a reutilizar.
- Cuando el tipo concreto es esencial para la lógica y la genérica complica innecesariamente el diseño.
- Cuando la abstracción reduce la claridad del código más de lo que aporta.
- Cuando se intenta generalizar demasiado una solución que en realidad es simple y específica.

## Comportamiento de la interfaz

- Si existen imágenes, se muestra una ventana con la imagen actual y su posición.
- Si se llega al inicio o al final de la colección, se muestra el mensaje `No hay más imágenes`.
- Si no hay imágenes cargadas, se muestra `No hay imágenes registradas`.

## Observaciones

- Las imágenes se generan dinámicamente, por lo que no es necesario incluir archivos externos.
- Cada imagen recibe un identificador incremental a medida que se crea.
