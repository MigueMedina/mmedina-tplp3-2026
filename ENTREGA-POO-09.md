# ENTREGA POO-09 — APIs cruzadas

## 1. Quiénes somos

| Rol | Dueño | Repositorio | Servicio | OpenAPI | Regla que aporta |
|---|---|---|---|---|---|
| A1 | Miguel Medina (`MigueMedina`) | `mmedina-tplp3-2026` | `http://localhost:8080` | `http://localhost:8080/v3/api-docs` | Orquesta el escenario y persiste en H2. |
| A2 | Fabricio G. | `fg-taller-git-2026` | `A2_BASE_URL` / `http://localhost:8081` | `A2_OPENAPI_URL` / `http://localhost:8081/v3/api-docs` | `POST /api/reglas/municion` (`municion`) con `{cargador, balasPorDisparo, disparos}`. |
| A3 | Joaquín B. | `jb-taller-git-2026` | `A3_BASE_URL` / `http://localhost:8082` | `A3_OPENAPI_URL` / `http://localhost:8082/v3/api-docs` | `POST /api/reglas/danio` (`danio`) con `{tirador, equipoTirador, objetivo, equipoObjetivo, vidaObjetivo, blindajeObjetivo, danio}`. |

A1 también publica su catálogo en `GET /api/catalogo` y aliases con los
`operationId` esperados en `/api/companeros/municion` y `/api/companeros/danio`.

## 2. Escenario en una frase

Un cliente llama sólo a A1 con `POST /escenarios/disparo-coordinado`; A1 le
pregunta a A2 si hay munición (`municion`) y a A3 si el daño contra el objetivo
es legal (`danio`), y sólo si los dos responden 2xx con `permitido:true` guarda
en H2 la simulación, las dos aprobaciones y la fecha del escenario.

## 3. Mapa de `operationId` y payload

- A1 → A2: `municion`, con `cargador`, `balasPorDisparo` y `disparos`.
- A1 → A3: `danio`, con `tirador`, `equipoTirador`, `objetivo`,
  `equipoObjetivo`, `vidaObjetivo`, `blindajeObjetivo` y `danio`.
- Compañero → A1: `/api/companeros/municion` (`operationId=municion`, payload
  `carga` y `dano`) y `/api/companeros/danio` (`operationId=danio`, payload
  `vida` y `blindaje`).
- Las rutas originales `/api/reglas/municion` y `/api/reglas/jugador-vivo`
  siguen disponibles con los `operationId` propios `validarMunicion` y
  `validarJugadorVivo`.

## 4. Cómo levantar y probar los tres servicios

1. Arrancar A1:

   ```bash
   cd mmedina-tplp3-2026/cs2
   ./mvnw spring-boot:run
   ```

2. Arrancar A2 en el puerto 8081:

   ```bash
   git clone https://github.com/FabricioG29/fg-taller-git-2026.git a2
   cd a2
   ./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
   ```

3. Arrancar A3 en el puerto 8082:

   ```bash
   git clone https://github.com/joaquinBenegas0/jb-taller-git-2026.git a3
   cd a3
   PORT=8082 ./mvnw spring-boot:run
   ```

4. Verificar los tres OpenAPI en los puertos 8080, 8081 y 8082.

5. Importar `insomnia/cs2-insomnia.json` y seguir
   `10 - POO-09 (APIs cruzadas)`: catálogo, aliases, escenario feliz con
   `idempotencyKey`, GET del escenario, rechazo de A2, rechazo de A3 y GET de
   comparación.

6. Confirmar en H2 que `ESCENARIOS` y `SIMULACIONES` crecen una sola vez en el
   caso feliz y no cambian con rechazos, validación 400 ni repeticiones
   idempotentes.
7. Ejecutar `bash insomnia/capturar-respuestas.sh` para regenerar la evidencia
   real; los JSON y `resumen.md` quedan en `insomnia/respuestas/capturas/`.

## 5. Estado de POO-08

La corrección de POO-08 tuvo observaciones y todas están cerradas; el README
documenta cada cierre con su commit exacto (`bd27f27`, `3679ff2`, `7be5449` y
`4d67d6d`). Puntaje: 3/3.

## 6. Captura reproducible

Además de los `.http` de ejemplo, `insomnia/capturar-respuestas.sh` consulta los
tres servicios levantados y guarda 21 respuestas reales con pedido, URL, status
y cuerpo. La corrida registrada usa A1 `92bf30b` (working tree), Fabricio
`d06bcf9` y Joaquín `ecba4ed`, y muestra: escenario feliz `201`, repetición
idempotente con el mismo id, rechazo de A2 `409` (`statusRemoto: 409`), rechazo
de A3 `409` (`statusRemoto: 422`), validación `400` con `campo`, `errores` y
`traceId`, y la lista de escenarios igual antes y después de los rechazos. El
índice está en `insomnia/respuestas/capturas/resumen.md`.

## 7. Enlace exacto al commit de la solución

Pegar acá el enlace del commit final, siguiendo la convención de que un commit
no puede contener su propio hash:

https://github.com/MigueMedina/mmedina-tplp3-2026/commit/0d93ffe8f04f797e75ea76a3494dfc1d1190f7f6
