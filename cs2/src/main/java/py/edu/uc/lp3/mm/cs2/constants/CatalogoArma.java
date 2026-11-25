package py.edu.uc.lp3.mm.cs2.constants;

import java.util.Arrays;
import java.util.List;

public enum CatalogoArma {

    GLOCK_18("Glock-18", "pistola", "TT", 200, 30, 78, 20, 120, 3, 45, 0.70,
            "Pistola ligera de los terroristas, útil para presionar a corta distancia."),
    USP_S("USP-S", "pistola", "CT", 200, 35, 82, 12, 48, 1, 55, 0.85,
            "Pistola silenciada de los antiterroristas, precisa y discreta."),
    DESERT_EAGLE("Desert Eagle", "pistola", "AMBOS", 700, 58, 76, 7, 28, 1, 65, 0.92,
            "Pistola pesada de alto impacto con cargador corto."),
    FAMAS("FAMAS", "fusil", "CT", 2250, 31, 78, 25, 75, 3, 110, 0.80,
            "Fusil automático de los antiterroristas, equilibrado y económico."),
    AK47("AK-47", "fusil", "TT", 2700, 38, 74, 30, 90, 3, 130, 0.78,
            "Fusil automático de los terroristas, potente incluso con chaleco."),
    M4A4("M4A4", "fusil", "CT", 3100, 34, 80, 30, 90, 3, 120, 0.82,
            "Fusil automático de los antiterroristas, estable en ráfaga."),
    M4A1_S("M4A1-S", "fusil", "CT", 2900, 33, 84, 25, 75, 3, 125, 0.88,
            "Variante silenciada del M4, más precisa y con menos retroceso."),
    GALIL("Galil AR", "fusil", "TT", 1800, 32, 76, 30, 90, 3, 115, 0.75,
            "Fusil económico de los terroristas para rondas de ahorro."),
    MP9("MP9", "subfusil", "CT", 1250, 27, 82, 30, 120, 5, 70, 0.65,
            "Subfusil compacto de los antiterroristas, muy móvil."),
    P90("P90", "subfusil", "CT", 2350, 30, 74, 50, 150, 5, 80, 0.68,
            "Subfusil de cargador grande para defender zonas cerradas."),
    NOVA("Nova", "escopeta", "AMBOS", 1050, 85, 55, 8, 32, 1, 45, 0.50,
            "Escopeta de bombillo, letal en distancias muy cortas."),
    SSG08("SSG 08", "francotirador", "AMBOS", 1700, 88, 92, 10, 40, 1, 180, 0.92,
            "Francotirador semiautomático económico, bueno para angulos largos."),
    AWP("AWP", "francotirador", "AMBOS", 4750, 115, 96, 5, 20, 1, 200, 0.97,
            "Francotirador de un disparo letal con cargador pequeño."),
    HE_GRANADA("HE Granada", "granada", "AMBOS", 300, 98, 100, 1, 1, 1, 15, 0.60,
            "Granada explosiva de un solo uso para dañar areas reducidas.");

    private final String nombre;
    private final String familia;
    private final String equipo;
    private final int precio;
    private final int dano;
    private final int precision;
    private final int capacidadCargador;
    private final int reservaBalas;
    private final int rafagaBalas;
    private final int alcanceMetros;
    private final double relacionBlindaje;
    private final String descripcion;

    CatalogoArma(String nombre, String familia, String equipo, int precio, int dano, int precision,
                 int capacidadCargador, int reservaBalas, int rafagaBalas, int alcanceMetros,
                 double relacionBlindaje, String descripcion) {
        this.nombre = nombre;
        this.familia = familia;
        this.equipo = equipo;
        this.precio = precio;
        this.dano = dano;
        this.precision = precision;
        this.capacidadCargador = capacidadCargador;
        this.reservaBalas = reservaBalas;
        this.rafagaBalas = rafagaBalas;
        this.alcanceMetros = alcanceMetros;
        this.relacionBlindaje = relacionBlindaje;
        this.descripcion = descripcion;
    }

    public static List<CatalogoArma> todos() {
        return Arrays.asList(values());
    }

    public String getNombre() {
        return nombre;
    }

    public String getFamilia() {
        return familia;
    }

    public String getEquipo() {
        return equipo;
    }

    public int getPrecio() {
        return precio;
    }

    public int getDano() {
        return dano;
    }

    public int getPrecision() {
        return precision;
    }

    public int getCapacidadCargador() {
        return capacidadCargador;
    }

    public int getReservaBalas() {
        return reservaBalas;
    }

    public int getRafagaBalas() {
        return rafagaBalas;
    }

    public int getAlcanceMetros() {
        return alcanceMetros;
    }

    public double getRelacionBlindaje() {
        return relacionBlindaje;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
