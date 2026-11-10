# Bitácora de Desarrollo - CS2 / LP3

Este documento registra la asistencia de IA, el modelo utilizado y un resumen
de los prompts relevantes para la construcción y cierre del trabajo práctico.

## Marca del asistente y modelo

- **Asistente:** OpenCode (`opencode`)
- **Modelo exacto:** `opencode/big-pickle`
- **Rol:** asistente de programación para Java, Spring Boot, JPA, pruebas y
  documentación.
- **Herramientas auxiliares previas registradas en el proyecto:** OpenCode
  1.18.18 con modelo Big Pickle y Gemini 3.5 Flash-Lite.

## Alcance de la colaboración

- Modelado y persistencia de la jerarquía de armas con JPA `SINGLE_TABLE`.
- Creación de la entidad `Jugador` con vida inicial 100 y lógica de daño.
- Creación de la entidad `Simulacion` para persistir cada disparo.
- Repositorios JPA para `Arma`, `Fusil`, `Granada`, `Jugador` y `Simulacion`.
- Servicios y CRUD REST para el padre y dos hijas de la familia de armas.
- Endpoint de simulación `POST /simulacion/disparar` con actualización de vida e
  historial persistido.
- Jerarquía de excepciones de dominio y `GlobalExceptionHandler`.
- Configuración de H2 persistente y consola web.
- Actualización de README, pruebas y colección de Insomnia.

## Resumen de prompts relevantes

### 1. Modelado de constructores sobrecargados
- **Prompt resumido:** actualizar `Fusil` y `Flash` con atributos privados,
  constructor vacío, constructor parcial y constructor completo llamando a
  `super(...)`.
- **Resultado:** jerarquía con encapsulamiento, herencia y sobrecarga.

### 2. Interfaz `UICapable`
- **Prompt resumido:** crear una interfaz de dominio con imagen, sonido,
  velocidad de disparo, rango y un método `default mostrarEnUI()`.
- **Resultado:** contrato polimórfico usado por `/ui-capables`.

### 3. Ampliación del dominio
- **Prompt resumido:** agregar `AWP` y `Granada`, completar el catálogo y las
  métricas de interfaz.
- **Resultado:** clases integradas con constructores sobrecargados y pruebas.

### 4. Persistencia JPA y jugadores
- **Prompt resumido:** mapear al menos tres clases del modelado CS2 con JPA,
  agregar `Jugador` con nombre y vida 100, repositorio y método para perder vida
  al recibir un disparo.
- **Resultado:** jerarquía `Arma` con `SINGLE_TABLE`, `Jugador`, repositorios y
  regla de daño en el dominio.

### 5. CRUD REST completo
- **Prompt resumido:** implementar CRUD REST con controlador, servicios y
  repositorios para la clase padre y dos clases hijas.
- **Resultado:** `/armas`, `/armas/fusiles` y `/armas/granadas` con GET, POST,
  PUT y DELETE; además CRUD de `/jugadores`.

### 6. Simulación de disparos
- **Prompt resumido:** crear un endpoint que dispare un arma a un jugador,
  reduzca su vida según el daño y persista el estado.
- **Resultado:** `POST /simulacion/disparar` con actualización de `Jugador` y
  registro en `Simulacion`; historial en `/simulacion/historial`.

### 7. Excepciones y respuestas HTTP
- **Prompt resumido:** crear una excepción base, dos hijas y un
  `@ControllerAdvice` con handlers HTTP específicos.
- **Resultado:** `DomainException`, `DatosInvalidosException` (400),
  `RecursoNoEncontradoException` (404) y `GlobalExceptionHandler`.

### 8. H2, documentación e Insomnia
- **Prompt resumido:** configurar H2 con URL fija para `/h2-console`, actualizar
  README, crear `BITACORA.md` y versionar una colección de Insomnia con todos
  los casos exigidos.
- **Resultado:** configuración `jdbc:h2:file:./data/cs2db;AUTO_SERVER=TRUE`,
  README completo, esta bitácora y `insomnia/cs2-insomnia.json`.

### 9. Verificación final
- **Prompt resumido:** revisar el repositorio contra la rúbrica e implementar
  automáticamente todo lo faltante para dejarlo listo para entregar.
- **Resultado:** CRUD, simulación, excepciones, persistencia, pruebas,
  documentación y colección de Insomnia integrados y verificados.

### 10. Chaleco, munición real y HTTP 409
- **Prompt resumido:** incorporar la entidad `Chaleco` con absorción de daño,
  implementar consumo real de munición y cargadores, agregar
  `MunicionInsuficienteException` y `JugadorEliminadoException`, y mapear ambas
  reglas de negocio a HTTP 409.
- **Resultado:** `Chaleco` persistido en cascada con `Jugador`, absorción del
  50% del daño mientras quede durabilidad, munición descontada al disparar,
  recarga con cargadores de repuesto, excepciones específicas y handlers 409.
  La simulación ahora consume munición y aplica el chaleco antes de restar vida.

### 11. Discriminador JSON, errores 409 y documentación POO-08
- **Prompt resumido:** incorporar `@JsonTypeInfo` y `@JsonSubTypes` para que las
  respuestas de armas incluyan `tipoArma`, agregar el nombre de la excepción al
  JSON de error, completar los casos 409 de Insomnia y redactar las respuestas
  pendientes del README y las especificaciones del dominio.
- **Resultado:** respuestas de armas con discriminador explícito, cuerpo de error
  con `excepcion`, colección Insomnia con `GET /` de verificación, preparación y
  ejecución de los 409, GET posterior al POST ilegal, README ampliado y
  `Docs/ESPECIFICACIONES-POO-08.md` creado.

### 12. Cierre de auditoría contra la rúbrica
- **Prompt resumido:** corregir los puntos parciales detectados en la auditoría:
  propiedades H2 faltantes en README, retorno `List<UICapable>`, constructores
  vacíos protegidos, orden de DELETE en Insomnia y casing de `tipoArma`; además
  actualizar la documentación.
- **Resultado:** README y especificaciones alineados con la configuración H2,
  `UIElementResponse` implementa `UICapable`, controlador con retorno de
  interfaz, entidades con constructor vacío `protected`, DELETE movidos a
  `09 - Limpieza` y `tipoArma` normalizado en minúsculas.

## Verificación de calidad

- `cd cs2 && ./mvnw clean compile` → `BUILD SUCCESS`.
- `cd cs2 && ./mvnw test` → `BUILD SUCCESS` con 33 tests, 0 fallas.
- Se verificaron por HTTP los 409 de munición insuficiente y jugador eliminado,
  además de la persistencia del blindaje después de una simulación.
- Prueba manual con la aplicación en ejecución:
  - `POST /jugadores` → 201 con vida 100.
  - `POST /armas/fusiles` → 201.
  - `POST /simulacion/disparar` → 201 con vida reducida y daño aplicado.
  - `GET /simulacion/historial` → 200 con el disparo persistido.
  - `GET /armas/99999` → 404 con `RecursoNoEncontradoException`.
  - `POST /armas` con precio negativo → 400 con `DatosInvalidosException`.
  - `GET /h2-console` → redirección a la consola web.
