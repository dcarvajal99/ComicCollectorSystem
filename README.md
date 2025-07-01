# ComicCollectorSystem

Este es un sistema de gestión de cómics desarrollado en Java. Permite gestionar cómics y usuarios, realizar préstamos y guardar la información en archivos CSV.

## Estructura del proyecto

- `src/main/java/com/diegocarvajal/comiccollectorsystem/App.java`: Clase principal con menú interactivo.
- `models/`: Contiene las clases de dominio (`Comic`, `Usuario`, `TiendaDeComics`).
- `services/`: Servicios para leer y escribir archivos CSV de cómics y usuarios.
- `exceptions/`: Excepciones personalizadas para la lógica de negocio.
- `data/`: Archivos CSV con los datos de cómics y usuarios.

## Funcionalidades principales

- Mostrar cómics y usuarios.
- Buscar cómics por título.
- Prestar cómics a usuarios registrados.
- Registrar nuevos usuarios.
- Guardar los datos en archivos CSV.

## Ejecución

1. Compila el proyecto con Maven o tu IDE favorito.
2. Ejecuta la clase `App.java`.
3. Sigue las instrucciones del menú en consola.

## Notas

- Los archivos `comics.csv` y `usuarios.csv` se encuentran en `src/main/java/com/diegocarvajal/comiccollectorsystem/data/`.
- Si los archivos no existen, el sistema los crea automáticamente con la cabecera correspondiente.
- No uses el carácter `|` en los campos de entrada.

## Autores

Diego Carvajal - Carolina Solis

Grupo 5 - Duoc UC