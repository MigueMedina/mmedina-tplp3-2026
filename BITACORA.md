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
- Publicación de OpenAPI con springdoc y anotaciones en controladores y DTO.
- Clientes HTTP hacia A2 y A3 con resolución de rutas desde OpenAPI en runtime.
- Escenario persistido con doble aprobación y rechazos atribuidos por compañero.
- Integración de A2 con Fabricio (`municion`, puerto 8081) y A3 con Joaquín
  (`danio`, puerto 8082), con cuerpos construidos desde el escenario local.

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
  regla de daño validada en la capa service.

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

### 13. Recarga parcial con consumo de repuesto
- **Prompt resumido:** corregir `recargar(int cantidad)` para que también
  consuma un cargador de repuesto, agregar pruebas del caso sin repuestos y
  documentar la regla en README y especificaciones.
- **Resultado:** la recarga parcial descuenta un repuesto, respeta la capacidad
  máxima del cargador y falla con `MunicionInsuficienteException` cuando no
  quedan repuestos.

### 14. Correcciones del docente
- **Prompt resumido:** mover validaciones y excepciones del dominio a la capa
  service, ocultar `setVida`, `setMunicionActual` e `id`, hacer que `Jugador`
  implemente `UICapable` y que `/ui-capables` lea de H2, actualizar el diagrama
  Mermaid, completar el enlace del commit y corregir la ortografía de Insomnia.
- **Resultado:** `ValidadorService` y `SimulacionService` contienen las reglas,
  las entidades solo guardan estado, el endpoint de interfaz recorre la base con
  armas y jugadores, y la documentación quedó alineada con el código.

### 15. Análisis del enunciado de POO-09 y plan de acción
- **Prompt resumido:** analizar el challenge de APIs cruzadas, el mapa A1/A2/A3,
  el ejemplo de UNO, la calificación de 15 puntos y el estado actual del repo,
  y proponer un plan antes de escribir código.
- **Resultado:** se confirmó que se trabaja en `mmedina-tplp3-2026`, que POO-08
  tiene observaciones cerradas, que A2 y A3 se completarán con placeholders
  hasta acordar el grupo CS2, que el hash exacto se publicará en Classroom y se
   replicará en el README con la convención del commit anterior, y que los
   clientes resolverán las rutas leyendo el OpenAPI en tiempo de ejecución.

### 16. OpenAPI propio y documentación de errores
- **Prompt resumido:** publicar OpenAPI 3 en Spring Boot, anotar controladores y
  DTO, documentar CRUD, simulación, reglas y respuestas 400/404/409, y agregar
  una prueba que lea `/v3/api-docs`.
- **Resultado:** springdoc 3.2.1, `/v3/api-docs` y `/swagger-ui.html`,
  `@OpenAPIDefinition`, `@Tag`, `@Operation`, `@ApiResponses` y `@Schema`;
  `OpenApiDocumentationTest` verifica paths, `operationId` y textos de error.

### 17. Clientes hacia A2 y A3
- **Prompt resumido:** implementar clientes HTTP configurables que consulten el
  OpenAPI de cada compañero, resuelvan la ruta por `operationId`, usen posibles
  `servers`, cacheen el resultado y atribuyan los rechazos al compañero.
- **Resultado:** paquete `client` con `OpenApiOperationResolver`,
  `CompaneroClient`, `ApiClientesProperties`, `ClientHttpConfig`, `RolCompanero`
  y DTO de regla/respuesta; `CompaneroClientTest` valida la resolución y el
  rechazo de A3.

### 18. Escenario persistido con doble aprobación
- **Prompt resumido:** crear un endpoint propio que consulte A2 y A3, guarde la
  jugada en H2 sólo si ambos aceptan, permita volver a leerla con GET y devuelva
  un 409 que diga quién rechazó sin modificar la base.
- **Resultado:** `EscenarioCoordinado`, `EscenarioRepository`,
  `EscenarioService`, `EscenarioController`, `CompanneroRechazoException`,
  handler 409 y pruebas de servicio y HTTP para feliz, rechazo A2 y rechazo A3.

### 19. Insomnia y enunciado aplicado
- **Prompt resumido:** extender Insomnia con el mapa A1/A2/A3, OpenAPI propios y
   ajenos, escenario feliz, rechazos por compañero y respuestas de ejemplo;
   actualizar README, bitácora y documento aplicado para Classroom.
 - **Resultado:** carpeta `10 - POO-09 (APIs cruzadas)`,
   `insomnia/respuestas/*.http`, `Docs/POO-09-enunciado-aplicado.md`, mapa y
   preguntas del enunciado respondidas en el README.

### 20. Reglas propias publicadas para rotación
 - **Prompt resumido:** publicar las dos reglas de A1 como consultas stateless,
   con `GET /api/reglas` y dos POST con los mismos `operationId` que A2 y A3,
   sin ids, repositorios ni escrituras en H2.
 - **Resultado:** `ReglasService`, `ReglasController`, DTOs con `@Schema`, rutas
    en `ApiPaths`, pruebas unitarias y HTTP, además de
    `Docs/doc-para-companeros.md` para que el grupo pueda consultarnos al rotar.

### 21. Trazabilidad y cierre documental
 - **Prompt resumido:** agregar la columna de commits exactos a las observaciones
   cerradas de POO-08, enlazar la ficha de compañeros desde README y documento
   aplicado, y fijar el hash final siguiendo la convención del commit anterior.
 - **Resultado:** tabla POO-08 con hashes `bd27f27`, `3679ff2`, `7be5449` y
   `4d67d6d`; README y `Docs/POO-09-enunciado-aplicado.md` apuntan a la ficha de
   rotación; Classroom y el README usarán el último hash previo al cierre.

### 22. OpenAPI 3.0 y colección ampliada
 - **Prompt resumido:** fijar OpenAPI 3.0, desactivar respuestas genéricas,
   documentar 400/409 por operación, agregar los casos de las reglas a Insomnia
   y dejar `mvnw` ejecutable.
 - **Resultado:** propiedades springdoc en configuración principal y de tests,
   `OpenApiDocumentationTest` verifica versión 3.0 y los nuevos `operationId`,
   Insomnia incluye casos felices y de error para ambas reglas, y `mvnw` pasó a
   modo `100755`.

### 23. Cableado definitivo de A2 y A3
  - **Prompt resumido:** auditar los repositorios de compañeros, usar Fabricio
    como A2 y Joaquín como A3, adaptar los cuerpos a `municion` y `danio`, y
    documentar arranque en 8080/8081/8082 sin hacer `git add` ni commits.
  - **Resultado:** `ReglaRequest` pasó a llevar un mapa de valores, las claves
    del JSON se mapean por `apis.a2.cuerpo.*` y `apis.a3.cuerpo.*`, y el
    escenario arma `{cargador, balasPorDisparo, disparos}` para Fabricio y
    `{tirador, equipoTirador, objetivo, equipoObjetivo, vidaObjetivo,
    blindajeObjetivo, danio}` para Joaquín. README, documento aplicado,
    diagramas, ficha de compañeros e Insomnia quedaron alineados con esos roles.

### 24. Robustez de clientes, transacciones y validación
  - **Prompt resumido:** reforzar fallos de configuración, distinguir rechazo de negocio de
    indisponibilidad, sacar HTTP de la transacción, agregar idempotencia,
    concurrencia controlada, validación de entrada y timeouts configurables.
  - **Resultado:** se corrigió `apis.a3.cuerpo.danio` en producción, se agregó
    fail-fast de mapeos remotos, caché OpenAPI con TTL de 5 minutos, preferencia
    de `base-url`, `RestClient` con timeouts y buffering, aprobación semántica
    `permitido`, `502`/`504` con `traceId` para compañeros no disponibles,
    `400` con Bean Validation, `TransactionTemplate` con revalidación y
    `@Version`, idempotencia por `idempotencyKey` e integridad referencial del
    escenario. README, documento aplicado, Insomnia y sus respuestas quedaron
    alineados.

### 25. Contrato más fácil para compañeros y uso de IA
  - **Asistente/agente:** `opencode`, ejecutado en este repositorio.
  - **Modelo LLM exacto:** `opencode/big-pickle`.
  - **Resumen de prompts:** comparar dos veces contra Pomona06 para detectar
    gaps; evaluar si un compañero podría consumir A1 sin fricción; agregar
    `GET /api/catalogo`, tipar errores 400/409/502/504, alinear `operationId` y
    payloads, ordenar Insomnia, actualizar README/BITACORA y generar
    `ENTREGA-POO-09.md` con marcador de commit, sin `git add` ni commits.
  - **Resultado:** catálogo CS2 propio con 14 modelos y DTO tipado; errores
    tipados con `campo`, `errores`, `rechazadoPor`, `statusRemoto`,
    `cuerpoRemoto`, `statusHttp` y `traceId`; aliases `/api/companeros/municion`
    y `/api/companeros/danio` con los `operationId` esperados; handler 400 para
    parámetros mal tipados; Insomnia con catálogo, aliases, validación 400 y
    respuestas claras del escenario.

### 26. Captura reproducible con los tres servicios reales
  - **Asistente/agente:** `opencode`, modelo `opencode/big-pickle`.
  - **Resumen de prompts:** agregar una captura reproducible además de los
    `.http`, ejecutar A1/A2/A3 reales, guardar cada pedido y respuesta con su
    status, verificar idempotencia y rechazos, y documentar el recorrido en
    Insomnia, README, bitácora y entrega, sin `git add` ni commits.
  - **Resultado:** `insomnia/capturar-respuestas.sh` recorre los tres OpenAPI,
    catálogo, aliases, reglas directas aceptadas/rechazadas, escenario feliz,
    repetición idempotente, rechazos A2/A3, validación 400 y comparación final.
    Genera 21 JSON y `resumen.md` en `insomnia/respuestas/capturas/`.

## Verificación de calidad


- `cd cs2 && bash ./mvnw clean compile` → `BUILD SUCCESS`.
- `cd cs2 && bash ./mvnw clean test -q` → `BUILD SUCCESS` con 71 tests, 0 fallas y
  0 errores.
- Se verificaron por HTTP los 409 de munición insuficiente y jugador eliminado,
  además de la persistencia del blindaje después de una simulación.
- Se verificaron el documento `/v3/api-docs` en versión 3.0, la resolución de
  rutas desde un OpenAPI simulado, el escenario feliz con relectura y los 409
  atribuidos a A2 y A3 sin escrituras en H2.
- Se verificaron por HTTP las consultas stateless de las dos reglas, con casos
  200, 400 y 409, y que `GET /escenarios` no cambia después de consultarlas.
- Se verificaron los cuerpos enviados a Fabricio y Joaquín con
  `CompaneroClientTest` y `EscenarioIntegrationTest`.
- Se verificó el bindeo real de `apis.a2.cuerpo.*` y `apis.a3.cuerpo.*` con
  `Cs2ApplicationTests`, y que `application.properties` de producción usa
  `apis.a3.cuerpo.danio` (no `dano`).
- Se verificaron el fail-fast de mapeos, la caché con invalidación, la
  clasificación de 5xx y un cliente HTTP real que respeta la `base-url`.
- Se verificaron la aprobación semántica `permitido`, la idempotencia por
  `idempotencyKey`, el bloqueo optimista con `@Version` y la integridad
  referencial que impide borrar una simulación con escenario asociado.
- Se verificaron por HTTP la validación `400` con `campo`, `errores` y `traceId`
  y el `502` con `statusRemoto`, `statusHttp` y `cuerpoRemoto`, sin escrituras
  cuando un compañero no está disponible.
- Se verificaron `GET /api/catalogo`, los aliases con `operationId` `municion` y
  `danio`, el error tipado para parámetros mal tipados y la presencia de los
  esquemas en `/v3/api-docs`.
- Se ejecutó `bash insomnia/capturar-respuestas.sh` con A1 en `8080`, Fabricio en
  `8081` y Joaquín en `8082`: 21 respuestas reales, escenario feliz `201`,
  repetición idempotente con el mismo id, rechazos `409` con `rechazadoPor` A2 y
  A3, validación `400` con `campo`/`errores`/`traceId`, y lista de escenarios sin
  cambios tras los rechazos.
- Se actualizaron las respuestas de Insomnia con los DTO de Fabricio y Joaquín:
  `EvaluacionMunicion`, `Aceptada`, `SIN_MUNICION`, `DISPARO_NO_PERMITIDO` y
  `companero-no-disponible.http` con `traceId`.
- `python3 -m json.tool insomnia/cs2-insomnia.json` → JSON válido.
- `git diff --check` → sin errores de whitespace.
- Prueba manual con la aplicación en ejecución:
  - `POST /jugadores` → 201 con vida 100.
  - `POST /armas/fusiles` → 201.
  - `POST /simulacion/disparar` → 201 con vida reducida y daño aplicado.
  - `GET /simulacion/historial` → 200 con el disparo persistido.
  - `GET /armas/99999` → 404 con `RecursoNoEncontradoException`.
  - `POST /armas` con precio negativo → 400 con `DatosInvalidosException`.
  - `GET /h2-console` → redirección a la consola web.
