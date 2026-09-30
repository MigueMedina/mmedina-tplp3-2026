# mmedina-tplp3-2026
# Diagrama Mermaid
```mermaid
classDiagram
    %% Clase Base
    class Arma {
        <<abstract>>
        -String nombre
        -int id
        -double precio
        +disparar()*
        +recargar()* String
        +obtenerDetalleTienda()* String
    }

    %% Clases Intermedias (Heredan de Arma)
    class ArmaDeFuego {
        -int dano
        -int precision
        -double recarga
        -double velocidad
        -int municionActual
        -int capacidadCargador
        +recargar() String
    }

    class Arrojadiza {
        -String tipo
        -double radio
        -double distancia
        -double duracion
        -double cooldown
        +recargar() String
    }

    %% Clases Finales (Heredan de las Intermedias)
    class Fusil {
        -boolean automatica
        -int mira
        -int retroceso
        -boolean silenciador
    }

    class Pistola {
        -boolean automatica
        -boolean silenciador
    }

    class Escopeta {
        -int perdigon
        -double dispersion
    }

    class Flash {
        -int intensidad
        -double duracionCeguera
    }

    class Granada {
        -int dano
        -boolean aturdimiento
    }

    class Humo {
        -int duracion
    }

    %% Relaciones de Herencia
    Arma <|-- ArmaDeFuego
    Arma <|-- Arrojadiza

    ArmaDeFuego <|-- Fusil
    ArmaDeFuego <|-- Pistola
    ArmaDeFuego <|-- Escopeta

    Arrojadiza <|-- Flash
    Arrojadiza <|-- Granada
    Arrojadiza <|-- Humo
```
