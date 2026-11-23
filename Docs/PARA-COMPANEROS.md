# POO-09 — Mi API para el grupo (Miguel Medina, CS2)

Esta ficha es para quien consulte mi servicio como su A2 o su A3. Tiene todo lo
necesario para usar mis reglas sin abrir mi código.

| | |
|---|---|
| **Quién** | Miguel Medina · GitHub `MigueMedina` |
| **Juego** | Counter-Strike 2 |
| **Repositorio** | https://github.com/MigueMedina/mmedina-tplp3-2026 |
| **Módulo** | `cs2/` |
| **OpenAPI (JSON)** | `http://<host>:<puerto>/v3/api-docs` |
| **OpenAPI (navegador)** | `http://<host>:<puerto>/swagger-ui.html` |
| **Mis reglas en JSON** | `GET http://<host>:<puerto>/api/reglas` |

## Cómo arrancar mi servicio en tu máquina

Requiere Java 21.

```bash
git clone https://github.com/MigueMedina/mmedina-tplp3-2026.git
cd mmedina-tplp3-2026/cs2
./mvnw spring-boot:run
```

Por defecto escucha en el puerto 8080. Si tu servicio ya usa ese puerto,
arrancá el mío en otro:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8082
```

Está listo cuando la terminal muestra `Started Cs2Application`. Para comprobarlo,
abrí `http://localhost:8082/swagger-ui.html`.

Si `./mvnw` no tiene permiso de ejecución en tu sistema, usá
`bash ./mvnw spring-boot:run`.

## Mis dos reglas

1. **Munición:** un arma de fuego dispara si el cargador tiene al menos una bala;
   sin balas no dispara.
2. **Vida:** un jugador con vida mayor que 0 sigue en juego; con vida 0 no recibe
   más disparos.

Mis `operationId` son `validarMunicion` y `validarJugadorVivo`, así que un A1
puede resolver las rutas desde mi OpenAPI sin copiarlas.

## Cómo consultarlas desde tu escenario

Estos tres endpoints reciben el estado en el JSON y **no leen ni cambian mi
base**. Así tu escenario me puede consultar sin modificar mis datos, y si tu otro
compañero rechaza la jugada, mi base no quedó modificada.

| Endpoint | `operationId` | Acepta | Rechaza |
|---|---|---|---|
| `GET /api/reglas` | `listarReglas` | 200 con las dos reglas | |
| `POST /api/reglas/municion` | `validarMunicion` | 200 si `carga >= 1` | 400 si faltan campos; 409 si `carga = 0` |
| `POST /api/reglas/jugador-vivo` | `validarJugadorVivo` | 200 si `vida > 0` | 400 si vida/blindaje fuera de rango; 409 si `vida = 0` |

### Regla de munición

Campos: `carga` (obligatorio) y `dano` (obligatorio, mayor que cero).

```bash
curl -X POST localhost:8082/api/reglas/municion \
  -H "Content-Type: application/json" \
  -d '{"carga":1,"dano":36}'
```

```json
{
  "permitido": true,
  "regla": "Un arma de fuego dispara si el cargador tiene al menos una bala; sin balas no dispara.",
  "detalle": "Hay munición suficiente para un disparo",
  "carga": 1,
  "dano": 36
}
```

Rechazo:

```bash
curl -X POST localhost:8082/api/reglas/municion \
  -H "Content-Type: application/json" \
  -d '{"carga":0,"dano":36}'
```

```json
{"excepcion":"MunicionInsuficienteException","mensaje":"No hay munición suficiente: cargador en 0 y se necesita 1 bala"}
```

### Regla de jugador vivo

Campos: `vida` (obligatoria, entre 0 y 100) y `blindaje` (opcional, entre 0 y
100; omitido equivale a 0).

```bash
curl -X POST localhost:8082/api/reglas/jugador-vivo \
  -H "Content-Type: application/json" \
  -d '{"vida":100,"blindaje":0}'
```

```json
{
  "permitido": true,
  "regla": "Un jugador con vida mayor que 0 sigue en juego; con vida 0 no recibe más disparos.",
  "detalle": "El jugador sigue vivo y puede recibir el disparo",
  "vida": 100,
  "blindaje": 0
}
```

Rechazo:

```bash
curl -X POST localhost:8082/api/reglas/jugador-vivo \
  -H "Content-Type: application/json" \
  -d '{"vida":0}'
```

```json
{"excepcion":"JugadorEliminadoException","mensaje":"El jugador ya fue eliminado: vida 0"}
```

## Qué te pido a cambio

Para consultarte como mi A2 o mi A3 necesito:

1. La URL de tu repositorio y el puerto en el que arranca tu servicio.
2. La URL de tu OpenAPI.
3. Tus dos reglas, en una frase cada una.
4. Tus `operationId` y un cuerpo que tu regla acepte y otro que rechace, con el
   código y el cuerpo de error que devolvés.

Si tu API solo valida las reglas cambiando tu base, te sugiero agregar un
endpoint de consulta que no guarde nada, como los míos: así un rechazo del otro
compañero no te deja la base modificada.

El enunciado no permite copiar el código del otro: solo se usa su OpenAPI. Si a
mío le falta algo para entender una regla, avisame y lo completo.
