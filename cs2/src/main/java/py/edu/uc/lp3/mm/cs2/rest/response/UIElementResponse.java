package py.edu.uc.lp3.mm.cs2.rest.response;

public record UIElementResponse(
		String tipo,
		String imagen,
		String sonido,
		double velocidadDisparo,
		double rango,
		String mensaje) {
}