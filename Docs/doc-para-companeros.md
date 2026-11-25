# doc-para-companeros — el árbitro de MigueMedina (CS2)

Si la rotación te puso del otro lado del teléfono, o sea, llamando a mi servicio
como tu A2 o tu A3, este es el manual. No hace falta que leas mi código: con mi
OpenAPI y los ejemplos de acá abajo ya podés resolver la jugada.

## El trato

Soy un árbitro con la libreta en blanco: recibo el estado del round en un JSON,
lo miro y digo si pasa o no. No pido ids, no toco repositorios y no escribo nada
en H2. Eso tiene una consecuencia práctica: si el otro compañero rechaza la
jugada, mi base no quedó modificada, así que podés preguntarme antes o después
sin miedo a dejar un rastro.

## Cómo encontrarme

- Quién: Miguel Medina, usuario `MigueMedina`; juego: Counter-Strike 2.
- Dónde vive el código: `https://github.com/MigueMedina/mmedina-tplp3-2026`, módulo `cs2/`.
- Contrato legible por máquina: `http://<host>:<puerto>/v3/api-docs`.
- Contrato legible por humano: `http://<host>:<puerto>/swagger-ui.html`.
- Las dos reglas en crudo: `GET http://<host>:<puerto>/api/reglas` (`listarReglas`).

## Si preferís correrlo en tu máquina

Necesitás Java 21. Clonás, entrás al módulo y lo levantás:

```bash
git clone https://github.com/MigueMedina/mmedina-tplp3-2026.git
cd mmedina-tplp3-2026/cs2
./mvnw spring-boot:run
```

Escucho en el 8080 por defecto. Si ese puerto ya está ocupado por tu servicio,
mandame a otro:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8180
```

La Terminal avisa con `Started Cs2Application`; la prueba más rápida es abrir
`http://localhost:8180/swagger-ui.html`. En sistemas donde `./mvnw` no tiene el
permiso de ejecución, reemplazalo por `bash ./mvnw`.

## Lo que sé responder

Dos preguntas nada más, y las dos con el mismo criterio que uso en mi A1:

1. **¿Hay munición para el disparo?** (`validarMunicion`). Con `carga >= 1` y un
   `dano` positivo, el arma dispara. Cargador en 0, no dispara.
2. **¿Sigue el jugador en la partida?** (`validarJugadorVivo`). Con `vida > 0`
   sigue y puede recibir el impacto; con `vida = 0` la jugada se corta.

Además publico aliases con los `operationId` que mi A1 espera consumir de vos:

- `POST /api/companeros/municion` — `operationId=municion`, mismo cuerpo que
  `validarMunicion`.
- `POST /api/companeros/danio` — `operationId=danio`, mismo cuerpo que
  `validarJugadorVivo`.

Así tu A1 puede configurar `municion` y `danio` y encontrarlos en mi OpenAPI sin
adaptar nombres. Si preferís las rutas originales, siguen vivas en
`/api/reglas/municion` y `/api/reglas/jugador-vivo`.

## Catálogo para elegir armas

`GET http://<host>:<puerto>/api/catalogo` devuelve modelos, equipo comprador,
precio, daño, precisión, cargador, reserva, ráfaga, alcance y penetración de
blindaje. Son valores orientativos propios de A1: sirven para no adivinar stats,
pero crear un arma sigue siendo una decisión explícita del cliente.

## Hacerme la pregunta

Los dos POST reciben el estado en el cuerpo. No consultan ni alteran mi base.

### 1. Disparo o no disparo

`POST /api/reglas/municion` — obligatorios `carga` y `dano` (mayor que cero).

```bash
curl -X POST localhost:8180/api/reglas/municion \
  -H "Content-Type: application/json" \
  -d '{"carga":1,"dano":36}'
```

Aprobado (200):

```json
{
  "permitido": true,
  "regla": "Un arma de fuego dispara si el cargador tiene al menos una bala; sin balas no dispara.",
  "detalle": "Hay munición suficiente para un disparo",
  "carga": 1,
  "dano": 36
}
```

Rechazado (409):

```bash
curl -X POST localhost:8180/api/reglas/municion \
  -H "Content-Type: application/json" \
  -d '{"carga":0,"dano":36}'
```

```json
{"excepcion":"MunicionInsuficienteException","mensaje":"No hay munición suficiente: cargador en 0 y se necesita 1 bala"}
```

### 2. Vivo o fuera de juego

`POST /api/reglas/jugador-vivo` — `vida` obligatoria entre 0 y 100; `blindaje`
opcional entre 0 y 100 (si lo omitís, cuento 0).

```bash
curl -X POST localhost:8180/api/reglas/jugador-vivo \
  -H "Content-Type: application/json" \
  -d '{"vida":100,"blindaje":0}'
```

Aprobado (200):

```json
{
  "permitido": true,
  "regla": "Un jugador con vida mayor que 0 sigue en juego; con vida 0 no recibe más disparos.",
  "detalle": "El jugador sigue vivo y puede recibir el disparo",
  "vida": 100,
  "blindaje": 0
}
```

Rechazado (409):

```bash
curl -X POST localhost:8180/api/reglas/jugador-vivo \
  -H "Content-Type: application/json" \
  -d '{"vida":0}'
```

```json
{"excepcion":"JugadorEliminadoException","mensaje":"El jugador ya fue eliminado: vida 0"}
```

## Los códigos, sin misterio

| Código | Quiere decir | Ejemplo |
|---|---|---|
| 200 | La regla aprueba el estado que me mandaste. | `carga: 1` o `vida: 100` |
| 400 | El estado no es válido: falta un campo o está fuera de rango. | `dano: 0`, `carga: -1`, `vida: 120`, `blindaje: -5` |
| 409 | El estado es válido pero la regla dice que no. | `carga: 0`, `vida: 0` |

En 400 y 409 el cuerpo trae `excepcion` y `mensaje`, con `DatosInvalidosException`
para el primer caso y la excepción de la regla para el segundo.

## Lo que te pido a cambio

Para poder consultarte cuando yo haga de A1, necesito:

1. URL de tu repositorio y puerto en el que levantás tu servicio.
2. URL de tu OpenAPI (con eso me arreglo para las rutas).
3. Tus dos reglas dichas en una frase cada una.
4. Tus `operationId`, un cuerpo que aprueben y otro que rechacen, con el código y
   el cuerpo de error que devolvés.

Si tu forma de validar implica escribir en tu base, te conviene sumar un endpoint
de solo consulta como los míos: así un rechazo ajeno no te deja datos sucios.
Y recordá la regla del enunciado: entre nosotros se comparte el OpenAPI, no el
código. Si algo de lo que escribí acá no te cierra, avisame y lo ajusto.
