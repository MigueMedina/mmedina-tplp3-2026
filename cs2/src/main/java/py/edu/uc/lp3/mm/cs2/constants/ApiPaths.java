package py.edu.uc.lp3.mm.cs2.constants;

public class ApiPaths {

	public static final String ARMA = "/arma";
	public static final String ARMA_ACCION = ARMA + "/accion";
	public static final String UI_CAPABLES = "/ui-capables";

	public static final String ARMAS = "/armas";
	public static final String ARMAS_ID = ARMAS + "/{id}";
	public static final String FUSILES = ARMAS + "/fusiles";
	public static final String FUSILES_ID = FUSILES + "/{id}";
	public static final String GRANADAS = ARMAS + "/granadas";
	public static final String GRANADAS_ID = GRANADAS + "/{id}";

	public static final String JUGADORES = "/jugadores";
	public static final String JUGADORES_ID = JUGADORES + "/{id}";

	public static final String SIMULACION = "/simulacion";
	public static final String SIMULACION_DISPARAR = SIMULACION + "/disparar";
	public static final String SIMULACION_HISTORIAL = SIMULACION + "/historial";
	public static final String SIMULACION_HISTORIAL_JUGADOR = SIMULACION_HISTORIAL + "/jugador/{jugadorId}";
}