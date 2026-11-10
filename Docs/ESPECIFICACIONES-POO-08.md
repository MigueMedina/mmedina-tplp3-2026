# Especificaciones POO-08 - Dominio Counter-Strike 2

- **Ejercicio:** `ejercicio-poo-08-crud-h2-insomnia-2026-10-28`
- **Asignatura:** Lenguaje de Programación 3 (CYT646)
- **Proyecto:** `mmedina-tplp3-2026`
- **Paquete base:** `py.edu.uc.lp3.mm.cs2`
- **Stack:** Java 21, Spring Boot 4.1.1, Spring Data JPA, H2, Maven Wrapper.

## Registro de Asistencia de IA y Herramientas

- **Herramientas de desarrollo:** OpenCode 1.18.18 model Big Pickle y Gemini 3.5 Flash-Lite.

## Objetivo

Publicar una API REST sobre el dominio de Counter-Strike 2 que persista armas,
jugadores y chalecos en H2, permita el CRUD completo de la familia de armas y
ejecute una simulación de disparo con dos reglas de negocio: consumo real de
munición y absorción de daño por chaleco. Las reglas viven en el dominio, el
controller solo traduce HTTP y las excepciones de dominio se convierten en
respuestas JSON con códigos HTTP diferenciados.

## Clases del modelo

- `Arma`: clase abstracta persistida con `SINGLE_TABLE`.
- `ArmaDeFuego` y `Arrojadiza`: ramas abstractas de la jerarquía.
- `Fusil`, `Pistola`, `Escopeta`, `AWP`, `Flash`, `Humo` y `Granada`: clases
  concretas persistidas.
- `Jugador`: entidad con vida, estado vivo/muerto y chaleco opcional.
- `Chaleco`: entidad con durabilidad y absorción del 50% del daño.
- `Simulacion`: historial persistido de cada disparo.
- `UICapable` y `ArmaOfensiva`: interfaces de dominio.

## URLs

| Método | Ruta | Operación |
|---|---|---|
| `GET` | `/` | Comprobar que el servicio está vivo. |
| `GET` | `/ui-capables` | Listar elementos que implementan `UICapable`. |
| `GET` | `/armas` | Listar armas persistidas, con filtro opcional `?nombre=`. |
| `GET` | `/armas/{id}` | Buscar un arma por id. |
| `POST` | `/armas` | Crear un arma según `tipo`. |
| `PUT` | `/armas/{id}` | Actualizar parcialmente un arma. |
| `DELETE` | `/armas/{id}` | Eliminar un arma. |
| `GET` | `/armas/fusiles` | Listar fusiles. |
| `GET` | `/armas/fusiles/{id}` | Buscar un fusil. |
| `POST` | `/armas/fusiles` | Crear un fusil. |
| `PUT` | `/armas/fusiles/{id}` | Actualizar un fusil. |
| `DELETE` | `/armas/fusiles/{id}` | Eliminar un fusil. |
| `GET` | `/armas/granadas` | Listar granadas. |
| `GET` | `/armas/granadas/{id}` | Buscar una granada. |
| `POST` | `/armas/granadas` | Crear una granada. |
| `PUT` | `/armas/granadas/{id}` | Actualizar una granada. |
| `DELETE` | `/armas/granadas/{id}` | Eliminar una granada. |
| `GET` | `/jugadores` | Listar jugadores. |
| `GET` | `/jugadores/{id}` | Buscar un jugador. |
| `POST` | `/jugadores` | Crear un jugador con vida 100 y blindaje opcional. |
| `PUT` | `/jugadores/{id}` | Actualizar nombre y/o blindaje. |
| `DELETE` | `/jugadores/{id}` | Eliminar un jugador. |
| `POST` | `/simulacion/disparar` | Disparar un arma a un jugador y persistir el resultado. |
| `GET` | `/simulacion/historial` | Listar el historial completo. |
| `GET` | `/simulacion/historial/jugador/{jugadorId}` | Listar el historial de un jugador. |

El JSON de creación no incluye `id`: lo asigna H2 con
`GenerationType.IDENTITY`. Las respuestas de armas incluyen `tipoArma` para
distinguir el subtipo, tanto al crear como al listar. El historial de simulación
usa el mismo vocabulario en minúsculas (`fusil`, `pistola`, `escopeta`, `awp`,
`flash`, `humo`, `granada`).

## Configuración de H2

- `spring.application.name=cs2`: identifica la aplicación en logs y trazas.
- `spring.datasource.url=jdbc:h2:file:./data/cs2db;AUTO_SERVER=TRUE`: mantiene
  la base persistente y permite que la consola web se conecte con la aplicación
  en ejecución.
- `spring.jpa.show-sql=true` y
  `spring.jpa.properties.hibernate.format_sql=true`: muestran el SQL formateado
  para la defensa.
- `spring.jpa.open-in-view=false`: cierra la sesión JPA al terminar el servicio,
  en lugar de mantenerla abierta durante la serialización HTTP.
- `spring.h2.console.enabled=true` y
  `spring.h2.console.settings.web-allow-others=false`: habilitan la consola solo
  desde la misma máquina.

## Interfaces, constructores y polimorfismo

`GET /ui-capables` declara retorno `List<UICapable>` y devuelve objetos
`UIElementResponse`, que implementan `UICapable`. Así el contrato del controller
habla con la interfaz del dominio y no con un DTO concreto.

Todos los constructores sin argumentos de las entidades son `protected` para que
JPA pueda hidratarlas sin exponerlos al cliente. Las clases concretas mantienen
constructores de negocio públicos y sobrecargados: uno simple y uno completo.

La sobrecarga está en `ArmaDeFuego.recargar()` y `recargar(int cantidad)`, además
de los constructores simples y completos. La sobreescritura está en
`disparar()`, `recargar()` y `obtenerDetalleTienda()` de las clases hijas, y en
`mostrarEnUI()` y `getVelocidadDisparo()` de `Flash` y `Granada`. La primera se
resuelve al compilar por la lista de argumentos; la segunda se resuelve en
ejecución por el tipo real del objeto.

## Invariantes

### Arma

- El nombre no puede ser nulo ni vacío.
- El precio no puede ser negativo.
- El id de negocio no puede ser negativo; el id persistido lo asigna la base.
- En un arma de fuego, la capacidad del cargador y los cargadores restantes no
  pueden ser negativos.
- La munición actual debe estar entre 0 y la capacidad del cargador.
- Cada disparo consume una bala; si no hay balas se lanza
  `MunicionInsuficienteException`.
- Recargar consume un cargador de repuesto; si no quedan, se lanza
  `MunicionInsuficienteException`.

### Jugador y chaleco

- La vida inicial es 100 y siempre está entre 0 y 100.
- Un daño menor o igual a cero es inválido.
- Un jugador con vida 0 está muerto y no puede recibir otro disparo:
  `JugadorEliminadoException`.
- El chaleco tiene durabilidad entre 0 y 100.
- El chaleco absorbe el 50% del daño, limitado por su durabilidad restante; el
  resto del daño se descuenta de la vida.
- La vida nunca queda negativa: el mínimo es 0.

### Simulación

- Debe indicarse `jugadorId` y `armaId`.
- El arma debe existir, el jugador debe existir y el arma debe implementar
  `ArmaOfensiva`.
- El disparo consume munición y actualiza vida y chaleco en una sola transacción.
- Se persiste una fila en `simulaciones` con vida antes, vida después, daño,
  jugador, arma y fecha.

## Excepciones y códigos HTTP

| Excepción | HTTP | Uso |
|---|---|---|
| `DatosInvalidosException` | 400 Bad Request | Invariante de construcción o actualización incumplido. |
| `RecursoNoEncontradoException` | 404 Not Found | Id de arma, jugador o recurso inexistente. |
| `MunicionInsuficienteException` | 409 Conflict | Cargador vacío o sin cargadores de repuesto. |
| `JugadorEliminadoException` | 409 Conflict | Disparo contra un jugador con vida 0. |
| `DomainException` | 400 Bad Request | Otra excepción de dominio no especializada. |

El cuerpo de error contiene `timestamp`, `status`, `error`, `excepcion`,
`mensaje` y `path`. El campo `excepcion` permite distinguir las dos respuestas
409.

Se usa **409 Conflict** porque la petición es válida y los recursos existen; el
conflicto está en el estado actual: el cargador no tiene balas o el jugador ya
está eliminado. No es 400 porque el cuerpo no es inválido, ni 404 porque el arma
y el jugador existen. El cliente puede resolverlo recargando el arma o
cambiando el jugador objetivo.

## Cómo probar

1. Arrancar la aplicación:

   ```bash
   cd cs2
   ./mvnw spring-boot:run
   ```

2. Abrir la consola H2 en `http://localhost:8080/h2-console` con la URL
   `jdbc:h2:file:./data/cs2db;AUTO_SERVER=TRUE`, usuario `sa` y contraseña vacía.

3. Importar `insomnia/cs2-insomnia.json` y seguir el orden de
   `insomnia/README.md`. Los DELETE están en la carpeta `09 - Limpieza` y se
   ejecutan al final, después de la simulación y de revisar H2.

4. Recorrido mínimo:

   - `GET /` responde que el servicio vive.
   - `POST /jugadores` con `{"nombre":"Neo","blindaje":100}` responde 201.
   - `POST /armas/fusiles` con un fusil válido responde 201 e incluye
     `tipoArma`.
   - `GET /armas` lista el fusil creado y muestra su `tipoArma`.
   - `GET /armas/{id}` coincide con el recurso creado.
   - `PUT /armas/fusiles/{id}` cambia un campo y un GET posterior lo conserva.
   - `POST /armas` con `precio:-1` responde 400 y un GET posterior muestra que
     la colección no creció.
   - `POST /simulacion/disparar` responde 201, descuenta munición y reduce vida
     según el chaleco.
   - `GET /jugadores/{id}` y `GET /armas/{id}` muestran el estado persistido.
   - `GET /ui-capables` devuelve los elementos de la interfaz y el
     `tipoArma` del historial usa minúsculas.
   - Un arma con cargador vacío responde 409 con
     `"excepcion":"MunicionInsuficienteException"`.
   - Un segundo disparo a un jugador con vida 0 responde 409 con
     `"excepcion":"JugadorEliminadoException"`.
   - Al final, `09 - Limpieza` elimina los recursos con 204 y un GET posterior
     responde 404.

5. Ejecutar las pruebas automáticas:

   ```bash
   cd cs2
   ./mvnw test
   ```

## Criterios de aceptación

- El CRUD persiste y lee desde H2, no desde listas en memoria.
- La herencia de armas se refleja en la tabla `armas` y en el campo JSON
  `tipoArma`.
- Las reglas de munición y chaleco se ejecutan en métodos del dominio.
- La simulación actualiza más de un objeto y deja historial persistido.
- Cada excepción de la jerarquía tiene su handler y su código HTTP.
- `GET /ui-capables` declara `List<UICapable>` y `UIElementResponse` implementa
  la interfaz.
- Los constructores vacíos de las entidades son `protected`; los de negocio
  siguen siendo públicos y sobrecargados.
- La colección Insomnia permite reproducir el caso feliz y cada error, con los
  DELETE al final en `09 - Limpieza`.
- El README y esta especificación describen el mismo comportamiento que el
  código.

## Entrega

Link del commit del repositorio.
