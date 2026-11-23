# Counter-Strike 2 (CS2) - API REST con persistencia

Proyecto de la cátedra **Lenguaje de Programación 3** (Universidad Católica
"Nuestra Señora de la Asunción"). Modela el dominio de armas y jugadores de
*Counter-Strike 2* aplicando POO, lo persiste con **JPA/Hibernate** y lo expone
mediante una **API REST** con **Spring Boot**.

- **Paquete base:** `py.edu.uc.lp3.mm.cs2`
- **Java:** 21
- **Spring Boot:** 4.1.1
- **Base de datos:** H2 persistente en archivo
- **Build:** Maven Wrapper (`bash ./mvnw`, no hace falta instalar Maven)

---

## Entregables cubiertos

| Requerimiento | Estado |
|---|---|
| Al menos 3 clases del dominio mapeadas con JPA | `Arma`, `ArmaDeFuego`, `Arrojadiza`, `Fusil`, `Pistola`, `Escopeta`, `AWP`, `Flash`, `Humo` y `Granada` usan herencia `SINGLE_TABLE`. |
| Entidad `Jugador` con nombre y vida inicial 100 | Implementada en `domain/Jugador.java`, con `recibirDisparo(int)` y persistencia JPA. |
| Entidad `Chaleco` con absorción de daño | Implementada en `domain/Chaleco.java`, asociada a `Jugador` con cascada JPA y durabilidad persistida. |
| Repositorio JPA de `Jugador` | `repository/JugadorRepository.java`. |
| Interfaz de dominio | `UICapable` (imagen, sonido, rango, velocidad) y `ArmaOfensiva` (daño); `UICapable` la implementan armas y `Jugador`. |
| CRUD REST de una familia | `ArmaCrudController`, `ArmaService`, `FusilService` y `GranadaService` con repositorios para el padre y dos hijas. |
| Endpoint de simulación | `POST /simulacion/disparar` descuenta munición, aplica el chaleco y actualiza la vida. |
| Reglas de negocio persistidas | `ValidadorService` y `SimulacionService` validan munición, chaleco, vida mínima 0 y estado de muerte; cada disparo queda en `simulaciones`. |
| Jerarquía de excepciones | `DomainException` + `DatosInvalidosException` + `RecursoNoEncontradoException` + `MunicionInsuficienteException` + `JugadorEliminadoException`. |
| `@RestControllerAdvice` | `GlobalExceptionHandler` con respuestas HTTP específicas, incluidos los 409 de negocio. |
| H2 lista para consola | URL fija `jdbc:h2:file:./data/cs2db;AUTO_SERVER=TRUE` y `/h2-console` habilitado. |
| Documentación | Este README, `BITACORA.md`, `Docs/ESPECIFICACIONES-POO-08.md` y colección de Insomnia. |
| OpenAPI propio | `/v3/api-docs` y `/swagger-ui.html` documentan CRUD, simulación, reglas y errores 400/404/409. |
| Clientes A2/A3 | `OpenApiOperationResolver` lee el OpenAPI ajeno y `CompaneroClient` resuelve y cachea la ruta para cada `operationId`. |
| Escenario persistido | `POST /escenarios/disparo-coordinado` exige dos aprobaciones y guarda `EscenarioCoordinado` en H2. |
| Atribución de rechazo | `409` con `rechazadoPor`, `statusRemoto` y `cuerpoRemoto`; la base queda igual. |

---

## Estado de la corrección de POO-08

La corrección de POO-08 **tuvo observaciones y todas están cerradas** en este
repositorio. No se vuelve a aplicar la rúbrica de 24; este ítem aporta los 3
puntos correspondientes a «observaciones cerradas».

| Observación del docente | Cierre aplicado | Commit |
|---|---|---|
| Validaciones y excepciones dentro del dominio | Movidas a `ValidadorService` y `SimulacionService`; las entidades ya no lanzan excepciones. | [`bd27f27`](https://github.com/MigueMedina/mmedina-tplp3-2026/commit/bd27f27b373aa44a5afceba6496b518581c59606) |
| `setVida` público en `Jugador` | Reemplazado por asignación privada; la vida se modifica sólo con `recibirDisparo()`. | [`bd27f27`](https://github.com/MigueMedina/mmedina-tplp3-2026/commit/bd27f27b373aa44a5afceba6496b518581c59606) |
| `setMunicionActual` e `id` expuestos | Sin setters públicos; el id lo asigna H2 y la munición cambia con mensajes del dominio. | [`bd27f27`](https://github.com/MigueMedina/mmedina-tplp3-2026/commit/bd27f27b373aa44a5afceba6496b518581c59606) |
| `Jugador` no implementaba `UICapable` | Ahora implementa la interfaz y aparece en `/ui-capables`. | [`bd27f27`](https://github.com/MigueMedina/mmedina-tplp3-2026/commit/bd27f27b373aa44a5afceba6496b518581c59606) |
| `/ui-capables` no leía la base | `UICapableService` recorre `ArmaRepository` y `JugadorRepository`. | [`bd27f27`](https://github.com/MigueMedina/mmedina-tplp3-2026/commit/bd27f27b373aa44a5afceba6496b518581c59606) |
| Diagrama Mermaid desactualizado | Reescrito con dominio, repositorios, services, controllers, excepciones y jugadores. | [`3679ff2`](https://github.com/MigueMedina/mmedina-tplp3-2026/commit/3679ff24a8404e23833b7a1a280ef9d0922ded55) |
| Enlace del commit sin completar | `Docs/ESPECIFICACIONES-POO-08.md` apunta al commit exacto de la corrección. | [`3679ff2`](https://github.com/MigueMedina/mmedina-tplp3-2026/commit/3679ff24a8404e23833b7a1a280ef9d0922ded55) |
| README y bitácora alineadas con las reglas en service | Textos actualizados después del refactor. | [`7be5449`](https://github.com/MigueMedina/mmedina-tplp3-2026/commit/7be5449c624766232fb2564b2bffa1a87945f794) |
| Errores de ortografía en Insomnia | Corregidos `despues` → `después` y `vacio` → `vacío`. | [`4d67d6d`](https://github.com/MigueMedina/mmedina-tplp3-2026/commit/4d67d6db45af73423a9f19791ad4cb8700940213) |

**Convención del enlace exacto de POO-09:** un commit no puede contener su propio
hash. El enlace de la solución se informa en Classroom y se replica en el README
apuntando al commit inmediatamente anterior al cierre documental; así el hash
publicado es verificable y no hay autorreferencia imposible.

---

## POO-09 — APIs cruzadas

Este repositorio es **A1**. A2 y A3 son los dos compañeros CS2 acordados; hasta
que se confirme el grupo, sus datos quedan como placeholders y se reemplazan con
variables de entorno sin tocar código.

| Rol | Dueño | URL base | OpenAPI | Regla que aporta |
|---|---|---|---|---|
| A1 | Este repo | `http://localhost:8080` | `http://localhost:8080/v3/api-docs` | Orquesta y persiste el escenario. |
| A2 | Pendiente de grupo CS2 | `A2_BASE_URL` / `http://localhost:8081` | `A2_OPENAPI_URL` / `http://localhost:8081/v3/api-docs` | Valida munición suficiente. |
| A3 | Pendiente de grupo CS2 | `A3_BASE_URL` / `http://localhost:8082` | `A3_OPENAPI_URL` / `http://localhost:8082/v3/api-docs` | Valida que el jugador esté vivo. |

**Escenario en una frase:** `POST /escenarios/disparo-coordinado` recibe
`{"jugadorId":1,"armaId":1}`, consulta A2 y A3 en ese orden y, sólo si los dos
aceptan, dispara y guarda en H2 la simulación junto con ambas aprobaciones. Un
`GET /escenarios/{id}` vuelve a leer la jugada. Si A2 o A3 rechazan, responde
`409` con `rechazadoPor` y ni `ESCENARIOS` ni `SIMULACIONES` cambian.

Las rutas ajenas se resuelven en runtime desde el OpenAPI de cada compañero.
Los `operationId` por defecto son `validarMunicion` (A2) y `validarJugadorVivo`
(A3), configurables con `A2_OPERATION_ID` y `A3_OPERATION_ID`. El cliente lee
también `servers[0].url` del documento y cachea la ruta por rol, operación y URL.

Para poder rotar y atender consultas como A2 o A3, este servicio también publica
sus dos reglas sin tocar H2: `GET /api/reglas`, `POST /api/reglas/municion` y
`POST /api/reglas/jugador-vivo`. La ficha para compañeros está en
`Docs/PARA-COMPANEROS.md`.

El enunciado aplicado, el detalle de los 15 puntos y los diagramas de flujo están
en `Docs/POO-09-enunciado-aplicado.md`.

### Preguntas que pide el enunciado

- **OpenAPI:** vive en `/v3/api-docs` y se prueba en `/swagger-ui.html`. Incluye
  CRUD, simulación, escenario y errores 400/404/409 con descripciones y ejemplos.
- **Grupo:** A1 es este servicio; A2 y A3 se completan con los compañeros CS2.
  La tabla anterior registra URLs y reglas en una frase.
- **Escenario:** necesita munición (A2) y jugador vivo (A3) para que el disparo
  sea legal; el éxito se ve en `/escenarios` y en H2.
- **Cliente:** las URLs y los `operationId` se configuran por propiedades o
  variables de entorno; la ruta se lee del OpenAPI ajeno.
- **Errores:** se traducen a `CompanneroRechazoException`, se propician el
  código y cuerpo remotos, y el handler distingue `A2` de `A3`.

---

## Estructura del proyecto

```text
mmedina-tplp3-2026/
├── README.md
├── BITACORA.md
├── insomnia/
│   ├── cs2-insomnia.json
│   ├── README.md
│   └── respuestas/
│       ├── escenario-feliz.http
│       ├── rechazo-a2.http
│       └── rechazo-a3.http
├── Docs/
│   ├── diagramamermaid.md
│   ├── ESPECIFICACIONES-POO-08.md
│   └── POO-09-enunciado-aplicado.md
└── cs2/
    ├── pom.xml
    ├── mvnw / mvnw.cmd
    └── src/
        ├── main/
        │   ├── java/py/edu/uc/lp3/mm/cs2/
        │   │   ├── Cs2Application.java
        │   │   ├── constants/ApiPaths.java
        │   │   ├── client/
        │   │   │   ├── ApiClientesProperties.java
        │   │   │   ├── ClientHttpConfig.java
        │   │   │   ├── CompaneroClient.java
        │   │   │   ├── OpenApiOperationResolver.java
        │   │   │   ├── ReglaRequest.java
        │   │   │   ├── RespuestaCompanero.java
        │   │   │   └── RolCompanero.java
        │   │   ├── domain/
        │   │   │   ├── Arma.java                 (@Entity, raíz SINGLE_TABLE)
        │   │   │   ├── ArmaDeFuego.java          (abstracta)
        │   │   │   ├── Arrojadiza.java           (abstracta)
        │   │   │   ├── ArmaOfensiva.java         (interfaz de daño)
        │   │   │   ├── UICapable.java            (interfaz de UI)
        │   │   │   ├── Fusil.java / Pistola.java / Escopeta.java / AWP.java
        │   │   │   ├── Flash.java / Humo.java / Granada.java
        │   │   │   ├── Jugador.java              (@Entity)
        │   │   │   ├── Chaleco.java              (@Entity, absorción de daño)
        │   │   │   ├── Simulacion.java           (@Entity, historial de disparos)
        │   │   │   └── EscenarioCoordinado.java  (@Entity, escenario POO-09)
        │   │   ├── exception/
        │   │   │   ├── DomainException.java
        │   │   │   ├── DatosInvalidosException.java
        │   │   │   ├── RecursoNoEncontradoException.java
        │   │   │   ├── MunicionInsuficienteException.java
        │   │   │   ├── JugadorEliminadoException.java
        │   │   │   └── CompanneroRechazoException.java
        │   │   ├── repository/
        │   │   │   ├── ArmaRepository.java
        │   │   │   ├── FusilRepository.java
        │   │   │   ├── GranadaRepository.java
        │   │   │   ├── JugadorRepository.java
        │   │   │   ├── SimulacionRepository.java
        │   │   │   └── EscenarioRepository.java
        │   │   ├── service/
        │   │   │   ├── ArmaMapper.java
        │   │   │   ├── ArmaService.java
        │   │   │   ├── FusilService.java
         │   │   │   ├── GranadaService.java
         │   │   │   ├── JugadorService.java
         │   │   │   ├── SimulacionService.java
         │   │   │   ├── UICapableService.java
         │   │   │   ├── ValidadorService.java
        │   │   │   └── EscenarioService.java
        │   │   └── rest/
        │   │       ├── advice/GlobalExceptionHandler.java
        │   │       ├── controller/
        │   │       │   ├── ArmaController.java        (endpoints POO existentes)
        │   │       │   ├── ArmaCrudController.java    (CRUD persistente)
        │   │       │   ├── JugadorController.java
        │   │       │   ├── SimulacionController.java
        │   │       │   ├── UIController.java
        │   │       │   ├── IndexController.java
        │   │       │   └── EscenarioController.java
        │   │       ├── dto/
        │   │       │   ├── ArmaRequest.java
        │   │       │   ├── DisparoRequest.java
        │   │       │   ├── JugadorRequest.java
        │   │       │   ├── JugadorResponse.java
        │   │       │   ├── SimulacionResponse.java
        │   │       │   ├── EscenarioRequest.java
        │   │       │   └── EscenarioResponse.java
        │   │       └── response/UIElementResponse.java
        │   └── resources/application.properties
        └── test/
            ├── resources/application.properties
            └── java/py/edu/uc/lp3/mm/cs2/...
```

---

## Diagrama Mermaid

```mermaid
classDiagram
    direction LR

    class Arma {
        <<abstract, @Entity>>
        -int id
        -String nombre
        -double precio
        +disparar() String
        +recargar() String
        +obtenerDetalleTienda() String
    }

    class ArmaDeFuego {
        <<abstract, @Entity>>
        -int dano
        -int precision
        -double recarga
        -double velocidad
        -int municionActual
        -int capacidadCargador
        -int cargadoresRestantes
        +getDano() int
        +getCargadoresRestantes() int
        +recargar() String
        +recargar(int) String
    }

    class Arrojadiza {
        <<abstract, @Entity>>
        -String tipo
        -double radio
        -double distancia
        -double duracion
        -double cooldown
    }

    class Fusil
    class Pistola
    class Escopeta
    class AWP
    class Flash
    class Humo
    class Granada

    class UICapable {
        <<interface>>
        +getImagen() String
        +getSonido() String
        +getVelocidadDisparo() double
        +getRango() double
        +mostrarEnUI() String
    }

    class ArmaOfensiva {
        <<interface>>
        +getDano() int
    }

    class Jugador {
        <<@Entity>>
        -Long id
        -String nombre
        -int vida
        -boolean vivo
        -Chaleco chaleco
        +recibirDisparo(int) int
        +equiparChaleco(Chaleco) void
        +getBlindaje() int
        +isVivo() boolean
        +revivir() void
    }

    class Chaleco {
        <<@Entity>>
        -Long id
        -int durabilidad
        +absorber(int) int
        +getDurabilidad() int
    }

    class Simulacion {
        <<@Entity>>
        -Long id
        -Jugador jugador
        -Arma arma
        -int dano
        -int vidaAntes
        -int vidaDespues
        -LocalDateTime fecha
    }

    class EscenarioCoordinado {
        <<@Entity>>
        -Long id
        -Simulacion simulacion
        -int a2Status
        -int a3Status
        -String a2Operacion
        -String a3Operacion
        -LocalDateTime fecha
    }

    class ArmaRepository {
        <<interface>>
        +findAllByOrderByIdAsc()
        +findByNombreContainingIgnoreCaseOrderByIdAsc(String)
    }
    class FusilRepository { <<interface>> }
    class GranadaRepository { <<interface>> }
    class JugadorRepository { <<interface>> }
    class SimulacionRepository { <<interface>> }
    class EscenarioRepository { <<interface>> }

    class ArmaCrudController
    class JugadorController
    class SimulacionController
    class UIController
    class EscenarioController
    class ArmaService
    class FusilService
    class GranadaService
    class JugadorService
    class SimulacionService
    class UICapableService
    class ValidadorService
    class EscenarioService

    class RolCompanero {
        <<enumeration>>
        A2
        A3
    }
    class OpenApiOperationResolver
    class CompaneroClient

    class DomainException {
        <<abstract>>
    }
    class DatosInvalidosException
    class RecursoNoEncontradoException
    class MunicionInsuficienteException
    class JugadorEliminadoException
    class CompanneroRechazoException
    class GlobalExceptionHandler

    Arma <|-- ArmaDeFuego
    Arma <|-- Arrojadiza
    ArmaDeFuego <|-- Fusil
    ArmaDeFuego <|-- Pistola
    ArmaDeFuego <|-- Escopeta
    ArmaDeFuego <|-- AWP
    Arrojadiza <|-- Flash
    Arrojadiza <|-- Humo
    Arrojadiza <|-- Granada

    UICapable <|.. Fusil
    UICapable <|.. AWP
    UICapable <|.. Flash
    UICapable <|.. Granada
    UICapable <|.. Jugador
    ArmaOfensiva <|.. ArmaDeFuego
    ArmaOfensiva <|.. Granada

    DomainException <|-- DatosInvalidosException
    DomainException <|-- RecursoNoEncontradoException
    DomainException <|-- MunicionInsuficienteException
    DomainException <|-- JugadorEliminadoException
    Jugador "0..1" o-- "1" Chaleco : usa

    Jugador "1" --> "*" Simulacion : participa
    Arma "1" --> "*" Simulacion : se usa en

    ArmaCrudController --> ArmaService
    ArmaCrudController --> FusilService
    ArmaCrudController --> GranadaService
    JugadorController --> JugadorService
    SimulacionController --> SimulacionService
    ArmaService --> ArmaRepository
    FusilService --> FusilRepository
    GranadaService --> GranadaRepository
    JugadorService --> JugadorRepository
    SimulacionService --> SimulacionRepository
    SimulacionService --> ArmaRepository
    SimulacionService --> JugadorRepository
    UIController --> UICapableService
    UICapableService --> ArmaRepository
    UICapableService --> JugadorRepository
    ArmaService --> ValidadorService
    FusilService --> ValidadorService
    GranadaService --> ValidadorService
    JugadorService --> ValidadorService
    SimulacionService --> ValidadorService
    GlobalExceptionHandler ..> DomainException

    Simulacion "1" --> "0..1" EscenarioCoordinado : se registra en
    EscenarioController --> EscenarioService
    EscenarioService --> EscenarioRepository
    EscenarioService --> SimulacionService
    EscenarioService --> CompaneroClient
    CompaneroClient --> OpenApiOperationResolver
    CompaneroClient --> RolCompanero
    DomainException <|-- CompanneroRechazoException
    EscenarioService ..> CompanneroRechazoException
    GlobalExceptionHandler ..> CompanneroRechazoException
```

La jerarquía de armas usa `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`,
una sola tabla `armas` y la columna discriminadora `tipo_arma`. En JSON, `Arma`
declara `@JsonTypeInfo` con la propiedad `tipoArma` y `@JsonSubTypes` para
`fusil`, `pistola`, `escopeta`, `awp`, `flash`, `humo` y `granada`.

---

## Reglas de negocio

Las validaciones, las reglas y las excepciones viven en la capa `service`
(`ValidadorService` y `SimulacionService`). Las entidades solo guardan el estado
encapsulado y responden a mensajes como `recibirDisparo()` o `recargar()`.

1. **Regla de munición:** `SimulacionService` comprueba el cargador antes de
   disparar; si no hay balas lanza `MunicionInsuficienteException` y la API
   responde 409. Cada disparo consume una bala de `municionActual`. Recargar, en
   cualquiera de sus sobrecargas, consume un cargador de repuesto; sin repuestos
   `ValidadorService` también responde 409.
2. **Regla del chaleco:** si el jugador tiene chaleco, absorbe el 50% del daño
   mientras le quede durabilidad. La durabilidad baja en la cantidad absorbida.
   El daño restante pasa a la vida.
3. **Regla de daño:** la vida del jugador nunca puede quedar por debajo de cero.
4. **Regla de estado:** si la vida llega a cero el jugador queda muerto y no
   admite más disparos; `SimulacionService` lanza
   `JugadorEliminadoException` (HTTP 409) antes de modificar el estado. Cada
   disparo actualiza al jugador, el arma y el chaleco, y se registra en
   `simulaciones`.

---

## Requisitos

- JDK 21 (`java -version`).
- No hace falta instalar Maven: el proyecto incluye `mvnw`.

---

## Cómo arrancar

Todos los comandos se ejecutan dentro de `cs2`:

```bash
cd cs2
```

Compilar:

```bash
bash ./mvnw clean compile
```

Ejecutar la aplicación:

```bash
bash ./mvnw spring-boot:run
```

La API queda en `http://localhost:8080`.

Alternativa con JAR:

```bash
bash ./mvnw clean package
java -jar target/cs2-0.0.1-SNAPSHOT.jar
```

Detener con `Ctrl + C`.

### Arrancar A1, A2 y A3

A1 es este repositorio:

```bash
cd cs2
bash ./mvnw spring-boot:run
```

A2 y A3 son repositorios de los compañeros. Cuando se confirme el grupo,
reemplazar estos marcadores por los enlaces reales:

```bash
git clone <URL_REPO_A2> a2
cd a2
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

```bash
git clone <URL_REPO_A3> a3
cd a3
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8082
```

Si los compañeros usan otros puertos, cambiar `A2_BASE_URL` y `A3_BASE_URL` en
el arranque de A1.

---

## Configuración de H2

Archivo: `cs2/src/main/resources/application.properties`.

| Propiedad | Valor | Explicación |
|---|---|---|
| `spring.application.name` | `cs2` | Nombre de la aplicación en logs, actuator y trazas. |
| `spring.datasource.url` | `jdbc:h2:file:./data/cs2db;AUTO_SERVER=TRUE` | URL fija y persistente. `./data/cs2db` es relativo a la carpeta `cs2`. `AUTO_SERVER=TRUE` permite que la consola web se conecte mientras corre la app. |
| `spring.datasource.driver-class-name` | `org.h2.Driver` | Driver JDBC de H2. |
| `spring.datasource.username` | `sa` | Usuario por defecto. |
| `spring.datasource.password` | *(vacío)* | Sin contraseña en desarrollo. |
| `spring.jpa.hibernate.ddl-auto` | `update` | Crea/actualiza las tablas automáticamente. |
| `spring.jpa.show-sql` | `true` | Muestra el SQL en consola para la defensa. |
| `spring.jpa.properties.hibernate.format_sql` | `true` | Formatea el SQL mostrado para que sea legible en la defensa. |
| `spring.jpa.open-in-view` | `false` | Evita mantener la sesión JPA abierta durante la serialización HTTP; obliga a resolver las relaciones dentro de la capa de servicio. |
| `spring.h2.console.enabled` | `true` | Habilita `/h2-console`. |
| `spring.h2.console.path` | `/h2-console` | Ruta de la consola. |
| `spring.h2.console.settings.web-allow-others` | `false` | Permite la consola solo desde la misma máquina, no desde otros equipos de la red. |
| `server.port` | `8080` | Puerto de la API. |

### Abrir la consola H2

1. Arrancar la aplicación.
2. Entrar a `http://localhost:8080/h2-console`.
3. Completar:
   - **JDBC URL:** `jdbc:h2:file:./data/cs2db;AUTO_SERVER=TRUE`
   - **User Name:** `sa`
   - **Password:** *(dejar vacío)*
4. Verificar las tablas `ARMAS`, `JUGADORES`, `CHALECOS` y `SIMULACIONES`.

> La carpeta `data/` se ignora en Git porque contiene la base local.

### Configuración de APIs cruzadas

| Propiedad / variable | Valor por defecto | Explicación |
|---|---|---|
| `apis.a2.base-url` / `A2_BASE_URL` | `http://localhost:8081` | URL base de A2. |
| `apis.a2.openapi-url` / `A2_OPENAPI_URL` | *(vacía)* | Si está vacía se deriva `/v3/api-docs`. |
| `apis.a2.operation-id` / `A2_OPERATION_ID` | `validarMunicion` | Operación buscada en el OpenAPI de A2. |
| `apis.a3.base-url` / `A3_BASE_URL` | `http://localhost:8082` | URL base de A3. |
| `apis.a3.openapi-url` / `A3_OPENAPI_URL` | *(vacía)* | Si está vacía se deriva `/v3/api-docs`. |
| `apis.a3.operation-id` / `A3_OPERATION_ID` | `validarJugadorVivo` | Operación buscada en el OpenAPI de A3. |
| `springdoc.api-docs.path` | `/v3/api-docs` | Documento OpenAPI propio. |
| `springdoc.swagger-ui.path` | `/swagger-ui.html` | Swagger UI propio. |

Los nombres de los campos del JSON enviado a A2/A3 también son configurables
con `apis.a2.request.*` y `apis.a3.request.*`, para adaptarse al cuerpo que
publique cada compañero sin modificar Java.

---

## Endpoints

### Endpoints de dominio POO

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/` | Saludo de prueba. |
| GET | `/ui-capables` | Lista polimórfica de armas y jugadores persistentes que implementan `UICapable`. |
| GET | `/arma` | Crea un arma en memoria según `tipo` (sin persistir). |
| GET | `/arma/accion` | Devuelve un arma, su recarga y detalle de tienda. |

### CRUD persistente de armas

| Método | Ruta | Descripción | Código esperado |
|---|---|---|---|
| GET | `/armas` | Lista todas las armas (opcional `?nombre=AK`). | 200 |
| GET | `/armas/{id}` | Busca un arma. | 200 / 404 |
| POST | `/armas` | Crea un arma según `tipo`. | 201 / 400 |
| PUT | `/armas/{id}` | Actualiza un arma. | 200 / 404 / 400 |
| DELETE | `/armas/{id}` | Elimina un arma. | 204 / 404 |
| GET | `/armas/fusiles` | Lista solo fusiles. | 200 |
| GET | `/armas/fusiles/{id}` | Busca un fusil. | 200 / 404 |
| POST | `/armas/fusiles` | Crea un fusil. | 201 / 400 |
| PUT | `/armas/fusiles/{id}` | Actualiza un fusil. | 200 / 404 / 400 |
| DELETE | `/armas/fusiles/{id}` | Elimina un fusil. | 204 / 404 |
| GET | `/armas/granadas` | Lista solo granadas. | 200 |
| GET | `/armas/granadas/{id}` | Busca una granada. | 200 / 404 |
| POST | `/armas/granadas` | Crea una granada. | 201 / 400 |
| PUT | `/armas/granadas/{id}` | Actualiza una granada. | 200 / 404 / 400 |
| DELETE | `/armas/granadas/{id}` | Elimina una granada. | 204 / 404 |

### CRUD de jugadores

| Método | Ruta | Descripción | Código esperado |
|---|---|---|---|
| GET | `/jugadores` | Lista jugadores. | 200 |
| GET | `/jugadores/{id}` | Busca un jugador. | 200 / 404 |
| POST | `/jugadores` | Crea un jugador con vida 100 y blindaje opcional. | 201 / 400 |
| PUT | `/jugadores/{id}` | Cambia el nombre y/o el blindaje. | 200 / 404 / 400 |
| DELETE | `/jugadores/{id}` | Elimina un jugador. | 204 / 404 |

### Simulación

| Método | Ruta | Descripción | Código esperado |
|---|---|---|---|
| POST | `/simulacion/disparar` | Dispara un arma a un jugador. | 201 / 400 / 404 / 409 |
| GET | `/simulacion/historial` | Historial completo persistido. | 200 |
| GET | `/simulacion/historial/jugador/{jugadorId}` | Historial de un jugador. | 200 / 404 |

### APIs cruzadas y escenario POO-09

| Método | Ruta | Descripción | Código esperado |
|---|---|---|---|
| GET | `/v3/api-docs` | Documento OpenAPI 3 de A1. | 200 |
| GET | `/swagger-ui.html` | Swagger UI para importar/probar el contrato. | 200 |
| POST | `/escenarios/disparo-coordinado` | Consulta A2 y A3; si ambas aceptan, dispara y persiste el escenario. | 201 / 400 / 404 / 409 |
| GET | `/escenarios` | Lista los escenarios aceptados y persistidos. | 200 |
| GET | `/escenarios/{id}` | Busca un escenario por id. | 200 / 404 |

Body del escenario:

```json
{"jugadorId":1,"armaId":1}
```

Respuesta de rechazo de un compañero:

```json
{
  "excepcion": "CompanneroRechazoException",
  "mensaje": "Rechazo de A3 (HTTP 409)",
  "rechazadoPor": "A3",
  "statusRemoto": 409,
  "cuerpoRemoto": "{\"excepcion\":\"JugadorEliminadoException\"}"
}
```

---

## Ejemplos con curl

### Crear un jugador

```bash
curl -X POST http://localhost:8080/jugadores \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Neo","blindaje":100}'
```

```json
{"id":1,"nombre":"Neo","vida":100,"blindaje":100,"vivo":true}
```

### Crear un fusil

```bash
curl -X POST http://localhost:8080/armas/fusiles \
  -H "Content-Type: application/json" \
  -d '{"nombre":"AK-47","precio":2700,"dano":36,"precision":75}'
```

```json
{
  "id": 1, "tipoArma": "fusil", "nombre": "AK-47", "precio": 2700.0,
  "dano": 36, "precision": 75, "capacidadCargador": 30,
  "municionActual": 30, "automatica": true, "mira": 2, "retroceso": 20,
  "silenciador": false, "imagen": "fusil_ak-47.png",
  "sonido": "fusil_disparo.ogg", "rango": 30.0, "velocidadDisparo": 10.0
}
```

> Algunos campos se muestran resumidos; la respuesta real incluye todos los
> getters de la entidad.

### Crear una granada

```bash
curl -X POST http://localhost:8080/armas/granadas \
  -H "Content-Type: application/json" \
  -d '{"nombre":"HE","dano":98,"radio":6,"aturdimiento":true}'
```

### Crear un arma usando el padre

```bash
curl -X POST http://localhost:8080/armas \
  -H "Content-Type: application/json" \
  -d '{"tipo":"awp","nombre":"AWP","dano":115}'
```

### Actualizar y eliminar

```bash
curl -X PUT http://localhost:8080/armas/fusiles/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"AK-47 Redline","dano":40}'

curl -X DELETE http://localhost:8080/armas/fusiles/1
```

### Disparar

```bash
curl -X POST http://localhost:8080/simulacion/disparar \
  -H "Content-Type: application/json" \
  -d '{"jugadorId":1,"armaId":1}'
```

```json
{
  "id": 1,
  "jugadorId": 1,
  "jugador": "Neo",
  "vidaAntes": 100,
  "vidaDespues": 64,
  "danoAplicado": 36,
  "vivo": true,
  "armaId": 1,
  "arma": "AK-47",
  "tipoArma": "fusil",
  "dano": 36,
  "fecha": "2026-11-10T09:40:24.094988"
}
```

### Historial persistido

```bash
curl http://localhost:8080/simulacion/historial
curl http://localhost:8080/simulacion/historial/jugador/1
```

### Escenario coordinado con A2 y A3

```bash
curl -X POST http://localhost:8080/escenarios/disparo-coordinado \
  -H "Content-Type: application/json" \
  -d '{"jugadorId":1,"armaId":1}'

curl http://localhost:8080/escenarios
curl http://localhost:8080/escenarios/1
```

### Endpoint de la interfaz

```bash
curl http://localhost:8080/ui-capables
```

### Error 400: POST ilegal

```bash
curl -X POST http://localhost:8080/armas \
  -H "Content-Type: application/json" \
  -d '{"tipo":"fusil","nombre":"Malo","precio":-1}'
```

```json
{
  "timestamp": "2026-11-10T09:40:24.243427081",
  "status": 400,
  "error": "Bad Request",
  "excepcion": "DatosInvalidosException",
  "mensaje": "El precio debe ser no negativo",
  "path": "/armas"
}
```

### Error 404: id inexistente

```bash
curl http://localhost:8080/armas/99999
```

```json
{
  "timestamp": "2026-11-10T09:40:24.204046273",
  "status": 404,
  "error": "Not Found",
  "excepcion": "RecursoNoEncontradoException",
  "mensaje": "No existe un arma con id 99999",
  "path": "/armas/99999"
}
```

### Error 409: munición insuficiente

```bash
curl -X POST http://localhost:8080/simulacion/disparar \
  -H "Content-Type: application/json" \
  -d '{"jugadorId":1,"armaId":2}'
```

```json
{
  "timestamp": "2026-11-10T10:25:00",
  "status": 409,
  "error": "Conflict",
  "excepcion": "MunicionInsuficienteException",
  "mensaje": "Fusil sin municion no puede disparar: cargador insuficiente (0 balas, necesita 1)",
  "path": "/simulacion/disparar"
}
```

### Error 409: jugador eliminado

```bash
curl -X POST http://localhost:8080/simulacion/disparar \
  -H "Content-Type: application/json" \
  -d '{"jugadorId":2,"armaId":3}'
```

```json
{
  "timestamp": "2026-11-10T10:26:00",
  "status": 409,
  "error": "Conflict",
  "excepcion": "JugadorEliminadoException",
  "mensaje": "El jugador eliminado ya está muerto y no puede recibir más disparos",
  "path": "/simulacion/disparar"
}
```

---

## Manejo de excepciones

```mermaid
classDiagram
    class DomainException {
        <<abstract>>
    }
    class DatosInvalidosException
    class RecursoNoEncontradoException
    class MunicionInsuficienteException
    class JugadorEliminadoException
    class CompanneroRechazoException
    class GlobalExceptionHandler

    RuntimeException <|-- DomainException
    DomainException <|-- DatosInvalidosException
    DomainException <|-- RecursoNoEncontradoException
    DomainException <|-- MunicionInsuficienteException
    DomainException <|-- JugadorEliminadoException
    DomainException <|-- CompanneroRechazoException
    GlobalExceptionHandler ..> DatosInvalidosException : 400
    GlobalExceptionHandler ..> RecursoNoEncontradoException : 404
    GlobalExceptionHandler ..> MunicionInsuficienteException : 409
    GlobalExceptionHandler ..> JugadorEliminadoException : 409
    GlobalExceptionHandler ..> CompanneroRechazoException : 409
```

| Excepción | HTTP | Cuándo se usa |
|---|---|---|
| `DatosInvalidosException` | 400 Bad Request | Datos inválidos, precio negativo, arma sin daño, blindaje fuera de rango. |
| `RecursoNoEncontradoException` | 404 Not Found | Id de arma, fusil, granada o jugador inexistente. |
| `MunicionInsuficienteException` | 409 Conflict | El cargador no tiene balas o no quedan cargadores de repuesto. |
| `JugadorEliminadoException` | 409 Conflict | Se intenta disparar a un jugador con vida 0. |
| `CompanneroRechazoException` | 409 Conflict | A2 o A3 rechazó el escenario; `rechazadoPor` distingue el origen. |
| `DomainException` | 400 Bad Request | Cualquier otra excepción de la jerarquía no especializada. |

`GlobalExceptionHandler` devuelve siempre `timestamp`, `status`, `error`,
`excepcion`, `mensaje` y `path`. El campo `excepcion` lleva el nombre simple de
la clase lanzada, de modo que `MunicionInsuficienteException` y
`JugadorEliminadoException` se distinguen aunque ambas respondan 409. El handler
de `IllegalArgumentException` queda como respaldo, pero las reglas de la API
lanzan las excepciones de la jerarquía desde la capa service.

---

## Pruebas

```bash
cd cs2
bash ./mvnw clean test
```

Resultado verificado: `BUILD SUCCESS` con 55 tests, 0 fallas y 0 errores:

- arranque del contexto Spring,
- validaciones de `ValidadorService` sobre armas, jugadores, chalecos y recarga,
- estado inicial, herencia y mensajes del dominio de armas,
- consumo de munición, sobrecarga de `recargar()` y cargadores de repuesto,
- vida inicial, daño, muerte, absorción del chaleco y mensajes de `Jugador`,
- excepciones 409 lanzadas por `SimulacionService` y `ValidadorService`,
- persistencia real de la simulación, la munición y el blindaje (JPA + H2 en memoria para tests),
- discriminador `tipoArma` en la creación y el listado de armas,
- consultas stateless de munición y jugador vivo, con casos 200, 400 y 409,
- respuesta de `/ui-capables` leyendo armas y jugadores desde H2,
- cuerpo de error con el nombre de la excepción,
- respuestas HTTP 400, 404 y 409,
- generación del OpenAPI y presencia de paths, `operationId` y errores,
- resolución de rutas desde un OpenAPI simulado y uso de `servers`,
- escenario feliz y rechazos A2/A3 sin escrituras en H2,
- endpoint HTTP del escenario con relectura y 409 con `rechazadoPor`.

Los tests usan `cs2/src/test/resources/application.properties` con una base H2
en memoria para no ensuciar la base de desarrollo.

---

## Documentación interactiva (Swagger UI)

Con la aplicación en ejecución:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

El documento describe el CRUD, la simulación, las dos reglas de POO-08, el
escenario de POO-09 y sus errores. Las operaciones llevan `operationId`
estable y las respuestas 400/404/409 incluyen ejemplos de las excepciones,
para que un compañero pueda importar el contrato sin abrir el código Java.

---

## Colección de Insomnia

La colección versionada está en `insomnia/cs2-insomnia.json`.

Para importarla:

1. Abrir Insomnia.
2. Ir a **Application → Preferences → Data → Import Data → From File**
   (o **Import/Export → Import**).
3. Seleccionar `insomnia/cs2-insomnia.json`.
4. Elegir el workspace **CS2 - TP LP3**.
5. Verificar el environment **Base**:
   - `base_url` / `a1_base_url`: `http://localhost:8080`
   - `a2_base_url` y `a3_base_url`: `http://localhost:8081` y `http://localhost:8082`.
   - `a2_openapi` y `a3_openapi`: documentos `/v3/api-docs` de los compañeros.
   - `jugador_id`, `arma_id`, `fusil_id`, `granada_id`: ids de prueba.
   - `arma_sin_municion_id`, `jugador_mortal_id`, `arma_letal_id`: ids que se
     copian de los POST de preparación de los casos 409.
6. Arrancar A1, A2 y A3 y ejecutar las carpetas en orden: `Armas`, `Fusiles`,
   `Granadas`, `Jugadores`, `Simulación`, `Interface`, `Excepciones`,
   `10 - POO-09 (APIs cruzadas)` y, por último, `Limpieza` para los DELETE.
7. Después de cada POST de creación, copiar el `id` devuelto en la variable de
   environment que corresponda. No ejecutar los DELETE antes de la simulación
   ni del escenario porque eliminan los recursos usados como ejemplo.

La colección incluye el CRUD completo, un POST ilegal seguido de un GET para
comprobar que la colección no creció, una consulta de id inexistente, el
endpoint `/ui-capables`, la simulación con disparo al jugador, un pedido para
`MunicionInsuficienteException` (409), otro para `JugadorEliminadoException`
(409) y los POST de preparación necesarios para reproducir ambos estados. La
carpeta `10 - POO-09 (APIs cruzadas)` agrega los OpenAPI de A1/A2/A3, el
escenario feliz, su relectura, los rechazos de A2 y A3 y el GET de comparación.
Los DELETE están agrupados en `09 - Limpieza`.

Las respuestas de ejemplo del feliz y de cada rechazo están en
`insomnia/respuestas/`. El recorrido completo y el orden de las excepciones
están detallados en `insomnia/README.md`.

---

## Sobrecarga y sobreescritura

La **sobrecarga** ocurre en una misma clase cuando hay varios métodos con el
mismo nombre y distinta lista de argumentos. Se agregó en `Arma` y sus hijas con
constructores simples y completos, y en `ArmaDeFuego` con `recargar()` para
llenar el cargador y `recargar(int cantidad)` para recargar una cantidad
parcial. Ambas sobrecargas consumen un cargador de repuesto; la variante con
cantidad agrega hasta lo pedido sin superar la capacidad del cargador. El
compilador elige la versión según los argumentos; no hay despacho polimórfico
entre clases.

La **sobreescritura** ocurre entre una superclase o interfaz y una subclase,
cuando se redefine el mismo método con la misma firma. `Arma` declara
`disparar()`, `recargar()` y `obtenerDetalleTienda()`; `Fusil`, `Pistola`,
`Escopeta`, `AWP`, `Flash`, `Humo` y `Granada` los implementan con su propio
comportamiento. `UICapable` declara `getImagen()`, `getSonido()`,
`getVelocidadDisparo()`, `getRango()` y `mostrarEnUI()`; `Flash` y `Granada`
sobrescriben `mostrarEnUI()` y `getVelocidadDisparo()`, mientras que las demás
armas usan el comportamiento heredado o lo especializan. En ejecución, una
variable de tipo `Arma` o `UICapable` llama al método de la instancia concreta.

En resumen: la sobrecarga se distingue por la lista de parámetros y se resuelve
al compilar; la sobreescritura se distingue por la misma firma y se resuelve en
tiempo de ejecución mediante el tipo real del objeto.

---

## Qué hay que pensar

**1. Recurso y verbo.** El sustantivo principal es `arma`; por eso las rutas
nombran recursos (`/armas`, `/armas/fusiles`, `/armas/granadas`, `/jugadores`) y
el verbo HTTP expresa la operación (`GET`, `POST`, `PUT`, `DELETE`). La ruta
`/simulacion/disparar` es una operación de negocio explícita: se expone como
`POST` porque crea un registro de simulación y modifica el estado del jugador y
del arma; no se usa un verbo en la URL.

**2. Identidad.** La clave es `id`: `int` en `Arma` y `Long` en `Jugador`,
`Chaleco` y `Simulacion`. La asigna H2 mediante
`@GeneratedValue(strategy = GenerationType.IDENTITY)`. El JSON de creación
—`ArmaRequest` o `JugadorRequest`— no lleva `id`; el cliente lo omite y lo lee
de la respuesta. La URL usa ese id de persistencia, nunca un índice de una lista
del controller.

**3. Herencia y tablas.** Se eligió `InheritanceType.SINGLE_TABLE` porque la
jerarquía de armas es cerrada, las consultas son polimórficas y se evita un
`JOIN` para listar todas las armas. La columna discriminadora es `tipo_arma`.
En JSON el subtipo se explicita con `@JsonTypeInfo(use = Id.NAME, include =
As.PROPERTY, property = "tipoArma")` y `@JsonSubTypes` en `Arma`; los nombres
son `fusil`, `pistola`, `escopeta`, `awp`, `flash`, `humo` y `granada`. Por eso
tanto la respuesta del POST como el `GET /armas` incluyen `tipoArma`. El campo
`tipo` de una `Arrojadiza` es un dato propio de la granada; no se confunde con el
discriminador de clase.

**4. Interfaz y repositorio.** `ArmaRepository` es una interfaz de Spring Data:
declara mensajes como `findAllByOrderByIdAsc()` o
`findByNombreContainingIgnoreCaseOrderByIdAsc(...)`, y Spring genera la
implementación. El controller y los servicios dependen de esa abstracción, no de
una implementación concreta; es la misma idea del protocolo de POO-07: se
programa contra el contrato y se puede cambiar la implementación sin tocar al
cliente.

**5. Protocolo del dominio.** `UICapable` declara imagen, sonido, velocidad,
rango y `mostrarEnUI()`. El endpoint de lectura es `GET /ui-capables`; el
controller devuelve `List<UICapable>` y cada elemento es un `UIElementResponse`
que implementa la interfaz, de modo que el contrato de retorno coincide con el
tipo de dominio. `UICapableService` recorre las armas y los jugadores guardados
en H2 y filtra los que implementan el protocolo. Lo implementan `Fusil` y `AWP`
desde la rama `ArmaDeFuego`, `Flash` y `Granada` desde la rama `Arrojadiza`, y
`Jugador` desde otra familia, así que el tipo de interfaz puede hablar con
instancias distintas del modelo. `ArmaOfensiva` es el otro protocolo: expone
`getDano()` y lo usa la simulación para aceptar armas que realmente infligen
daño.

**6. Ocultamiento e invariantes.** Los campos son privados y las reglas
numéricas se validan en `ValidadorService` y `SimulacionService` antes de
modificar el estado. Las entidades no lanzan excepciones: `ArmaDeFuego`
descuenta balas en `disparar()` y `recargar()`, `Jugador` responde a
`recibirDisparo()` y `Chaleco.absorber()` descuenta la durabilidad. `setVida`,
`setMunicionActual` e `id` no tienen setters públicos: la vida y la munición se
cambian con mensajes del juego y el id lo asigna la base. Los DTO de entrada
evitan que Jackson escriba directamente sobre las entidades. JPA e Hibernate
hidratan las filas por reflexión usando el constructor sin argumentos. Un POST
ilegal se rechaza antes de `save()`, por lo que la transacción no deja basura:
la colección de Insomnia incluye el POST con precio negativo y un GET posterior
para comparar el listado.

**7. PUT y PATCH.** La API usa `PUT` porque el proyecto ya tenía ese verbo y el
enunciado acepta `PUT` o `PATCH`. La semántica implementada es de actualización
parcial: `ArmaMapper.aplicar()` y `JugadorService.actualizar()` ignoran los
campos nulos u omitidos y conservan el valor persistido. No es un reemplazo
estricto del recurso completo; si se necesitara esa semántica, correspondería
`PATCH` o exigir todos los campos. El README y la colección documentan este
comportamiento para que no haya ambigüedad.

**8. DELETE.** Borrar significa eliminar físicamente la fila del arma o del
jugador. El endpoint responde `204 No Content`; un GET posterior responde `404`.
Las simulaciones son historial y se eliminan en cascada cuando se borra el
jugador o el arma asociada (`@OnDelete(CASCADE)`). El verbo HTTP no ejecuta una
regla de negocio del juego: delega en el repositorio; la decisión de permitir el
borrado y su efecto quedan documentados, no improvisados en el controller.

**9. Constructores y JPA.** `Arma`, `ArmaDeFuego`, `Arrojadiza`, `Jugador`,
`Simulacion`, `Chaleco` y todas las clases concretas tienen constructor sin
argumentos con visibilidad `protected` para que JPA pueda instanciarlos sin
exponerlos al cliente. Las clases concretas conservan constructores de negocio
públicos y sobrecargados: uno simple que crea un objeto con valores por defecto
y otro completo con todos los atributos. Hibernate usa el constructor vacío
protegido y completa los campos privados por reflexión; el código de la
aplicación usa los constructores de negocio y los servicios validan los datos
antes de persistir.

**10. Dos reglas y simulación.** La primera regla es de munición: cada disparo
descuenta una bala y recargar consume un cargador de repuesto. La segunda es de
daño con chaleco: el `Chaleco` absorbe hasta el 50% del daño según su
durabilidad, la vida nunca baja de cero y un jugador con vida 0 no admite otro
disparo. El recurso es `POST /simulacion/disparar`; participan `ArmaDeFuego`
(arma), `Jugador` y `Chaleco`, y además se persiste una `Simulacion` como
historial. Después de simular, `GET /jugadores/{id}` muestra vida y blindaje,
`GET /armas/{id}` muestra munición y `GET /simulacion/historial` muestra el
registro. Todo se ejecuta dentro de `@Transactional`.

**11. Excepciones.** `DomainException` es la base no comprobada.
`DatosInvalidosException` representa datos que rompen un invariante de
construcción o actualización (400). `RecursoNoEncontradoException` representa un
id que no existe (404). `MunicionInsuficienteException` y
`JugadorEliminadoException` representan reglas de juego incumplidas y responden
409. Un recurso inexistente y una regla de juego incumplida son tipos distintos
porque el cliente debe poder reaccionar distinto: corregir la URL o corregir el
estado del juego.

Se eligió **409 Conflict** y no 400 porque la petición está bien formada y los
recursos existen; lo que impide completarla es el estado actual (cargador vacío
o jugador con vida 0). Tampoco corresponde 404 porque el arma y el jugador
existen. Un 422 sería defendible, pero 409 expresa mejor un conflicto con el
estado del recurso y le indica al cliente que debe recargar el arma o dejar de
disparar al jugador eliminado.

**12. Excepciones comprobadas y no comprobadas.** Todas las excepciones del
dominio extienden `RuntimeException`, es decir, son no comprobadas. Se eligió así
porque una regla de negocio violada no se resuelve dentro del método que la
detecta: la maneja el `@RestControllerAdvice` y se traduce a HTTP. Una excepción
comprobada obligaría a declarar `throws` en cada servicio y controller, acoplaría
las firmas al transporte y no aportaría recuperación local. El handler de Spring
funciona con ambas, pero la no comprobada mantiene el dominio limpio.

**13. JSON de error.** `GlobalExceptionHandler` devuelve `timestamp`, `status`,
`error`, `excepcion`, `mensaje` y `path`. `excepcion` contiene el nombre simple
de la clase, por ejemplo `MunicionInsuficienteException` o
`JugadorEliminadoException`; de ese modo las dos hermanas 409 se distinguen por
el cuerpo aunque compartan el código HTTP.

**14. H2 y la consola.** La URL debe ser fija porque la consola web es un cliente
JDBC independiente y necesita apuntar a la misma base que la aplicación. Con
una URL aleatoria en memoria no se podría inspeccionar el estado persistido. La
consola se habilita en desarrollo y las tablas se crean con `ddl-auto=update`.

**15. Cambiar de base de datos.** Si mañana se reemplaza H2 por PostgreSQL o
MySQL cambian el driver, la URL, el usuario, la contraseña y posiblemente el
dialecto en `application.properties` y `pom.xml`. No cambian el dominio, los
repositorios Spring Data, los controllers, los DTO ni las reglas de negocio; esa
es la ventaja de mantener la persistencia detrás de interfaces.

**16. ¿Quién lanza la excepción?** La lanza la capa service, no el dominio ni el
controller. `ValidadorService` lanza `DatosInvalidosException` y
`MunicionInsuficienteException` cuando los datos o la recarga no son válidos;
`SimulacionService` lanza `MunicionInsuficienteException` y
`JugadorEliminadoException` antes de pedirle a las entidades que cambien su
estado. Las entidades solo guardan el estado encapsulado; el controller traduce
HTTP.

**17. ¿Por qué una entidad `Simulacion`?** Porque el jugador y el arma representan
el estado actual, mientras que `Simulacion` conserva el historial auditable de
cada disparo con vida antes/después, daño, arma, jugador y fecha. Así se puede
demostrar que la simulación persistió y no solo que la respuesta HTTP fue
correcta.

**18. ¿Por qué los tests usan H2 en memoria?** Para que sean rápidos, repetibles y
no dependan de archivos locales. La base de desarrollo sigue siendo persistente
en `./data/cs2db`; la de tests se configura en
`cs2/src/test/resources/application.properties`.

---

## Notas

- Los endpoints `/arma` y `/arma/accion` se conservan para la demostración de
  POO y no persisten datos; el CRUD persistente está en `/armas`.
- Las armas `Flash` y `Humo` no implementan `ArmaOfensiva`, por lo que no pueden
  usarse en `/simulacion/disparar` (responden 400).
- Si se modifica el esquema y H2 queda inconsistente, detener la aplicación y
  borrar la carpeta `cs2/data/` para recrear la base.

---

## Entrega POO-09

- POO-08: commit exacto de cierre de observaciones
  [`4d67d6d`](https://github.com/MigueMedina/mmedina-tplp3-2026/commit/4d67d6db45af73423a9f19791ad4cb8700940213).
- POO-09: enlace exacto al commit de la solución:
  [commit `992121d`](https://github.com/MigueMedina/mmedina-tplp3-2026/commit/992121d6805984ec26e544e138e5092c9ae07be5).

El enlace apunta al commit inmediatamente anterior al cierre documental, siguiendo
la convención de que un commit no puede contener su propio hash. El mismo enlace
se informa en Classroom.
