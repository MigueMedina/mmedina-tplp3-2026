# Bitácora de Desarrollo - POO-06

Este documento registra las iteraciones y el soporte de asistencia técnica utilizados durante el diseño, refactorización y documentación del proyecto CS2 Spring Boot.

## Registro de Asistencia de IA y Herramientas

* **Herramientas de desarrollo:** OpenCode 1.18.18 model Big Pickle y Gemini (Google).
* **Alcance de la colaboración:**
  - Estructuración del dominio orientado a objetos (`Arma`, `ArmaDeFuego`, `Arrojadiza`, `Fusil`, `Flash`).
  - Implementación de constructores simples y sobrecargados utilizando la llamada a `super(...)` para cumplir con los requerimientos de POO mediante OpenCode.
  - Generación de diagramas de clases estructurados en notación Mermaid para el README.
  - Verificación y pruebas de compilación limpia con Maven (`BUILD SUCCESS`).

---

## Historial de Prompts Relevantes

### 1. Modelado de Constructores Sobrecargados con OpenCode
* **Instrucción dada al entorno:**
  > "Actualiza la clase Fusil.java dentro del paquete py.edu.uc.lp3.mm.cs2.domain aplicando estrictamente los principios de Programación Orientada a Objetos (POO). Asegurate de incluir: 1. Encapsulamiento correcto con atributos privados. 2. Un constructor vacío (simple) predeterminado. 3. Un constructor sobrecargado parcial para inicialización rápida. 4. Un constructor completamente sobrecargado que reciba todos los parámetros..."
* **Resultado:** Implementación exitosa de los tres niveles de constructores respetando la encapsulación y la herencia por medio de OpenCode.

### 2. Extensión de Constructores en la Rama Arrojadiza
* **Instrucción dada al entorno:**
  > "Actualiza la clase Flash.java dentro del paquete py.edu.uc.lp3.mm.cs2.domain aplicando constructores simples y sobrecargados. Asegurate de incluir atributos privados, constructor vacío, constructor parcial y constructor completamente sobrecargado llamando a super(...)."
* **Resultado:** Cobertura total de constructores sobrecargados en la segunda rama de la jerarquía abstracta.

### 3. Modelado de Constructores Sobrecargados (`Fusil` y `Flash`)
* **Instrucción dada al entorno:**
  > "Actualiza la clase Fusil.java y Flash.java dentro del paquete py.edu.uc.lp3.mm.cs2.domain aplicando estrictamente los principios de POO: atributos privados, constructor vacío, constructor parcial e inicialización completamente sobrecargada llamando a super(...)."
* **Resultado:** Implementación exitosa de los tres niveles de constructores respetando la encapsulación y la herencia por medio de OpenCode.

### 4. Creación de la Interfaz `UICapable`
* **Instrucción dada al entorno:**
  > "Crea la interfaz UICapable dentro del paquete py.edu.uc.lp3.mm.cs2.domain. Debe incluir los métodos abstractos String getImagen(); y String getSonido();, además de un método con comportamiento por defecto (default String mostrarEnUI()) que devuelva un mensaje formateado combinando la imagen y el sonido."
* **Resultado:** Contrato de interfaz inicial configurado para el comportamiento polimórfico en `/ui-capables`.

### 5. Ampliación del Dominio (`AWP` y `Granada`)
* **Instrucción dada al entorno:**
  > "Agrega las clases AWP (extendiendo de ArmaDeFuego) y Granada (extendiendo de Arrojadiza) al paquete py.edu.uc.lp3.mm.cs2.domain siguiendo el estilo de las clases existentes con constructores sobrecargados y la implementación de UICapable."
* **Resultado:** Creación e integración limpia de ambas clases completando el catálogo de armas.

### 6. Enriquecimiento de la Interfaz `UICapable` y Métricas de UI
* **Instrucción dada al entorno:**
  > "Agrega los métodos getVelocidadDisparo() y getRango() a la interfaz UICapable, actualizando su método default mostrarEnUI(). Hereda la velocidad desde ArmaDeFuego y el rango desde Arrojadiza, aplicando valores específicos a Fusil (30.0) y AWP (100.0)."
* **Resultado:** Contrato de interfaz ampliado y totalmente polimórfico, exponiendo las métricas técnicas de cada elemento en la interfaz de usuario.

---

## Verificación de Calidad y Compilación
* **Comando de prueba:** `./mvnw clean compile`
* **Estado:** `BUILD SUCCESS` (Compilación limpia sin errores tras la refactorización con OpenCode).