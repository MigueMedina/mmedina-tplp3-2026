# Captura reproducible POO-09

- Fecha: 20261125T100926-0300
- A1: http://localhost:8080 (commit `92bf30b`, working tree)
- A2: http://localhost:8081 (commit `d06bcf9`)
- A3: http://localhost:8082 (commit `ecba4ed`)
- Regeneración: levantar los tres servicios y ejecutar `bash insomnia/capturar-respuestas.sh`.
- Escenario feliz: HTTP 201, id `1`.
- Idempotencia: la repetición con `captura-20261125T100926-0300-feliz` devolvió el mismo id.
- Rechazo A2: HTTP 409 con `rechazadoPor: A2`.
- Rechazo A3: HTTP 409 con `rechazadoPor: A3`.
- Validación: HTTP 400 con `campo`, `errores` y `traceId`.
- Escenarios antes de los rechazos: 1.
- Escenarios después de los rechazos: 1.

| Archivo | Método | URL | HTTP |
|---|---|---|---|
| [`00-openapi-a1.json`](00-openapi-a1.json) | GET | http://localhost:8080/v3/api-docs | 200 |
| [`01-openapi-a2.json`](01-openapi-a2.json) | GET | http://localhost:8081/v3/api-docs | 200 |
| [`02-openapi-a3.json`](02-openapi-a3.json) | GET | http://localhost:8082/v3/api-docs | 200 |
| [`03-catalogo-a1.json`](03-catalogo-a1.json) | GET | http://localhost:8080/api/catalogo | 200 |
| [`04-alias-municion-a1.json`](04-alias-municion-a1.json) | POST | http://localhost:8080/api/companeros/municion | 200 |
| [`05-alias-danio-a1.json`](05-alias-danio-a1.json) | POST | http://localhost:8080/api/companeros/danio | 200 |
| [`06-crear-jugador.json`](06-crear-jugador.json) | POST | http://localhost:8080/jugadores | 201 |
| [`07-crear-fusil.json`](07-crear-fusil.json) | POST | http://localhost:8080/armas/fusiles | 201 |
| [`08-crear-fusil-sin-municion.json`](08-crear-fusil-sin-municion.json) | POST | http://localhost:8080/armas/fusiles | 201 |
| [`09-regla-a2-aceptada.json`](09-regla-a2-aceptada.json) | POST | http://localhost:8081/api/reglas/municion | 200 |
| [`10-regla-a3-aceptada.json`](10-regla-a3-aceptada.json) | POST | http://localhost:8082/api/reglas/danio | 200 |
| [`11-regla-a2-rechazada.json`](11-regla-a2-rechazada.json) | POST | http://localhost:8081/api/reglas/municion | 409 |
| [`12-regla-a3-rechazada.json`](12-regla-a3-rechazada.json) | POST | http://localhost:8082/api/reglas/danio | 422 |
| [`13-escenario-feliz.json`](13-escenario-feliz.json) | POST | http://localhost:8080/escenarios/disparo-coordinado | 201 |
| [`14-escenario-idempotente.json`](14-escenario-idempotente.json) | POST | http://localhost:8080/escenarios/disparo-coordinado | 201 |
| [`15-get-escenario.json`](15-get-escenario.json) | GET | http://localhost:8080/escenarios/1 | 200 |
| [`16-escenarios-tras-feliz.json`](16-escenarios-tras-feliz.json) | GET | http://localhost:8080/escenarios | 200 |
| [`17-rechazo-a2.json`](17-rechazo-a2.json) | POST | http://localhost:8080/escenarios/disparo-coordinado | 409 |
| [`18-rechazo-a3.json`](18-rechazo-a3.json) | POST | http://localhost:8080/escenarios/disparo-coordinado | 409 |
| [`19-validacion-400.json`](19-validacion-400.json) | POST | http://localhost:8080/escenarios/disparo-coordinado | 400 |
| [`20-escenarios-tras-rechazos.json`](20-escenarios-tras-rechazos.json) | GET | http://localhost:8080/escenarios | 200 |
