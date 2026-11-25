# Colección de Insomnia - CS2 API

Archivo: `cs2-insomnia.json` (formato de exportación de Insomnia v4).

## Importar

1. Abrir Insomnia.
2. Ir a **Application → Preferences → Data → Import Data → From File**
   (o **Import/Export → Import**).
3. Seleccionar `insomnia/cs2-insomnia.json`.
4. Abrir el workspace **CS2 - TP LP3**.
5. Revisar el environment **Base**.
6. Arrancar la API con `cd cs2 && ./mvnw spring-boot:run` (o
   `bash ./mvnw spring-boot:run` si el wrapper no tiene permiso). Para POO-09
   arrancar también Fabricio como A2 en `http://localhost:8081` y Joaquín como
   A3 en `http://localhost:8082` (o cambiar `a2_base_url` y `a3_base_url`).
7. Ejecutar las carpetas en el orden indicado más abajo. Cuando un POST devuelva
   un `id`, copiarlo en la variable de environment correspondiente. La carpeta
   `09 - Limpieza` se ejecuta recién al final, después de simular y revisar H2.

## Environment Base

| Variable | Uso |
|---|---|
| `base_url` | URL del servicio, por defecto `http://localhost:8080`. |
| `jugador_id` | Id de un jugador creado para el caso feliz de simulación. |
| `arma_id` | Id de un arma creada para el caso feliz de simulación. |
| `fusil_id` | Id de un fusil creado en el CRUD de la hija. |
| `granada_id` | Id de una granada creada en el CRUD de la hija. |
| `arma_sin_municion_id` | Id del fusil con `capacidadCargador: 0` para provocar el 409 de munición. |
| `jugador_mortal_id` | Id del jugador que se elimina para provocar el 409 de jugador eliminado. |
| `arma_letal_id` | Id del AWP con daño 115 usado para eliminar al jugador de prueba. |
| `a1_base_url` | URL de A1, por defecto `http://localhost:8080`. |
| `a2_base_url` | URL base de Fabricio (A2), por defecto `http://localhost:8081`. |
| `a3_base_url` | URL base de Joaquín (A3), por defecto `http://localhost:8082`. |
| `a2_openapi` | OpenAPI de Fabricio; se usa `http://localhost:8081/v3/api-docs`. |
| `a3_openapi` | OpenAPI de Joaquín; se usa `http://localhost:8082/v3/api-docs`. |
| `escenario_id` | Id devuelto por el escenario feliz, para volver a leerlo. |

## Captura reproducible

La colección es para ejecutar a mano; la evidencia automática está en
`capturar-respuestas.sh`. Con los tres servicios levantados, desde la raíz:

```bash
bash insomnia/capturar-respuestas.sh
```

El script usa `urllib` de Python (no necesita `jq`) y deja en
`insomnia/respuestas/capturas/` un JSON por pedido más `resumen.md`. Recorre:
OpenAPI de A1/A2/A3, catálogo y aliases de A1, reglas directas de Fabricio y
Joaquín con caso aceptado y rechazado, escenario feliz, repetición con la misma
`idempotencyKey`, GET del escenario, rechazo de A2, rechazo de A3, validación 400
y GET final de comparación. Si un status no es el esperado, el script se detiene.

Variables opcionales: `A1_BASE_URL`, `A2_BASE_URL`, `A3_BASE_URL`,
`A1_OPENAPI_URL`, `A2_OPENAPI_URL`, `A3_OPENAPI_URL`, `A2_COMMIT`, `A3_COMMIT` y
`CAPTURA_DIR`.

## Orden sugerido para la defensa

1. **GET / - servicio vivo**. Debe responder `200` y confirmar que la API está
   arrancada.
2. `01 - Armas` → **Listar armas** (`GET /armas`).
3. `04 - Jugadores` → **Crear jugador (vida 100 con chaleco)**.
   Copiar el `id` devuelto en `jugador_id`.
4. `02 - Fusiles` → **Crear fusil**. Copiar el `id` devuelto en `fusil_id` y en
   `arma_id`.
5. `05 - Simulación` → **Disparar al jugador**. Debe responder `201` y mostrar
   `vidaDespues`, `danoAplicado`, `tipoArma` y munición consumida.
6. `04 - Jugadores` → **Buscar jugador por id**. Debe mostrar la vida y el
   blindaje persistidos después de la simulación.
7. `01 - Armas` → **Buscar arma por id**. Debe mostrar `municionActual`
   descontada y `tipoArma`.
8. `05 - Simulación` → **Ver historial completo**.
9. `06 - Interface` → **Listar UICapable**.
10. `07 - Excepciones` → ejecutar en este orden:
    - **404 - RecursoNoEncontradoException**.
    - **400 - DatosInvalidosException (POST ilegal)**.
    - **GET armas después del POST ilegal**. Comparar con el listado del paso 2:
      el arma inválida no debe aparecer.
    - **Preparar 409: fusil con cargador vacío**. Copiar el `id` en
      `arma_sin_municion_id`.
    - **409 - MunicionInsuficienteException**. Debe responder `409` y el JSON
      debe incluir `"excepcion": "MunicionInsuficienteException"`.
    - **Preparar 409: jugador para eliminar**. Copiar el `id` en
      `jugador_mortal_id`.
    - **Preparar 409: AWP letal**. Copiar el `id` en `arma_letal_id`.
    - **Preparar 409: eliminar jugador**. Debe responder `201` y dejar vida 0.
    - **409 - JugadorEliminadoException**. Debe responder `409` y el JSON debe
      incluir `"excepcion": "JugadorEliminadoException"`.
11. Abrir `http://localhost:8080/h2-console` y consultar `ARMAS`, `JUGADORES`,
    `CHALECOS` y `SIMULACIONES`.
 12. `10 - POO-09 (APIs cruzadas)` → ejecutar en este orden:
     - **Catálogo CS2 de A1**. Confirma modelos, equipo, precio, daño,
       cargador, reserva, ráfaga, alcance y penetración de blindaje.
     - **Alias A2 - operationId municion** y **Alias A3 - operationId danio**.
       Muestran que A1 expone los mismos `operationId` que espera consumir.
     - **OpenAPI A1 (propio)**, **OpenAPI A2 (Fabricio)** y
       **OpenAPI A3 (Joaquín)**. Confirmar que los tres exponen `/v3/api-docs`.

    - **Escenario feliz (A2 y A3 aceptan)**. Debe responder `201`; A1 consulta
      `municion` en Fabricio y `danio` en Joaquín, exige `permitido:true` en
      ambas respuestas y guarda la jugada. Copiar el `id` devuelto en
      `escenario_id`.
    - Repetir **Escenario feliz** con la misma `idempotencyKey`: debe devolver
      el mismo `id` y no consultar a los compañeros. Para una jugada nueva,
      cambiar la clave.
    - **Validación 400 - falta jugadorId**. Debe responder `400` con `campo`,
      `errores` y `traceId`, sin consultar a los compañeros.
    - **GET escenario persistido**. Debe devolver `200` con las aprobaciones
      `a2` y `a3`.
    - **GET escenarios (comparar tras rechazo)**. Anotar la cantidad actual.
    - **Rechazo de A2 (munición)**. Debe responder `409` con
      `"rechazadoPor": "A2"` y `statusRemoto: 409`.
    - **GET escenarios (comparar tras rechazo)**. Debe mostrar la misma
      cantidad que antes del rechazo.
    - **Rechazo de A3 (jugador eliminado)**. Debe responder `409` con
      `"rechazadoPor": "A3"` y `statusRemoto: 422`; el jugador eliminado se
      preparó en el paso 10.
    - **GET escenarios (comparar tras rechazo)**. Debe seguir igual.
    - Si A2 o A3 están apagados, el escenario debe responder `502`/`504` con
      `traceId`, `statusRemoto` y `cuerpoRemoto`, sin escribir en H2; comparar
      con `insomnia/respuestas/companero-no-disponible.http`.
     - Abrir H2 y comprobar que `ESCENARIOS` y `SIMULACIONES` no crecieron con
       los rechazos ni con la repetición idempotente.
     - **GET reglas propias (resumen)**. Muestra las dos reglas de A1.
     - **Regla munición - caso aceptado / rechazado (409) / dato inválido (400)**
       y **Regla jugador vivo - caso aceptado / rechazado (409) / dato inválido
       (400)**. Son consultas stateless: no cambian H2.
     - **GET escenarios (las reglas no escriben)**. Debe repetir la cantidad
       anterior.
13. `09 - Limpieza (opcional)` → ejecutar los DELETE al final: **Eliminar arma**,
    **Eliminar fusil**, **Eliminar granada** y **Eliminar jugador**. Si
    `arma_id` y `fusil_id` apuntan al mismo recurso, el segundo DELETE responde
    `404`; es el resultado esperado de haber borrado dos veces el mismo id.

## Contenido

- **Raíz:** GET `/` para confirmar que el servicio está vivo.
- **01 - Armas (CRUD padre):** GET lista, GET por id, POST y PUT; el DELETE se
  ejecuta en `09 - Limpieza`.
- **02 - Fusiles (CRUD hija):** GET lista, GET por id, POST y PUT; el DELETE se
  ejecuta en `09 - Limpieza`.
- **03 - Granadas (CRUD hija):** GET lista, GET por id, POST y PUT; el DELETE se
  ejecuta en `09 - Limpieza`.
- **04 - Jugadores:** GET lista, GET por id, POST y PUT; el DELETE se ejecuta en
  `09 - Limpieza`. El POST acepta `nombre` y `blindaje` opcional. Sin
  `blindaje`, el jugador no tiene chaleco.
- **05 - Simulación:** POST para disparar, historial completo e historial por
  jugador. Cada disparo descuenta munición, aplica el chaleco si existe y
  persiste vida, blindaje y munición.
- **06 - Interface:** GET `/ui-capables`.
- **07 - Excepciones:**
  - `404 - RecursoNoEncontradoException`.
  - `400 - DatosInvalidosException` con un POST de precio negativo.
  - `GET` posterior para comprobar que la colección no creció.
  - `409 - MunicionInsuficienteException` con un cargador vacío.
  - `409 - JugadorEliminadoException` con un segundo disparo a un jugador con
    vida 0.
- **08 - Endpoints POO (legacy):** `/arma` y `/arma/accion`.
- **09 - Limpieza (opcional):** DELETE de arma, fusil, granada y jugador. Se
  ejecuta después de la simulación y de consultar H2 para no borrar los ids que
  usa la defensa.
- **10 - POO-09 (APIs cruzadas):** catálogo CS2, aliases con los `operationId`
  esperados, OpenAPI de A1, Fabricio (A2) y Joaquín (A3); reglas propias para
  rotación; validación 400; escenario feliz idempotente; GET del escenario
  persistido; rechazos atribuidos a A2 y A3; GET de comparación. Los ejemplos
  de respuesta están en `insomnia/respuestas/` y las respuestas reales
  regenerables en `insomnia/respuestas/capturas/`.

## Resultados esperados

- `GET /` responde `200` con el mensaje de bienvenida.
- Los POST de creación responden `201` e incluyen `id`.
- Los GET de colección responden `200` y leen datos persistidos en H2.
- Un id inexistente responde `404` con `RecursoNoEncontradoException`.
- Un cuerpo inválido responde `400` con `DatosInvalidosException`.
- Munición agotada y jugador eliminado responden `409` con el nombre de la
  excepción en el campo `excepcion`.
- Los listados de armas incluyen `tipoArma` en minúsculas (`fusil`, `awp`,
  `granada`, etc.) para distinguir el subtipo.
- Los DELETE de `09 - Limpieza` responden `204`; si se repite el mismo id, el
  segundo intento responde `404`.
- `/api/catalogo` responde `200` con el catálogo CS2 de A1 y los aliases
  `/api/companeros/municion` y `/api/companeros/danio` responden `200`.
- `/v3/api-docs` responde `200` en A1, A2 y A3.
- El escenario feliz responde `201` e incluye `a2.status` y `a3.status` en `200`
  con `permitido:true`.
- Repetir la misma `idempotencyKey` devuelve el mismo escenario sin escribir de
  nuevo; un cuerpo sin `jugadorId` responde `400` con `traceId`.
- Los rechazos de compañero responden `409` con `rechazadoPor` igual a `A2` o
  `A3`; si el compañero no responde, responde `502`/`504` con `traceId`. En
  ambos casos `ESCENARIOS` y `SIMULACIONES` no crecen.
- `insomnia/respuestas/capturas/resumen.md` refleja la corrida real: 21 pedidos
  con sus statuses, idempotencia verificada y la lista de escenarios igual antes
  y después de los rechazos.
