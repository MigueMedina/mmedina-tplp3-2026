package py.edu.uc.lp3.mm.cs2.rest.response;

import py.edu.uc.lp3.mm.cs2.domain.UICapable;

public record UIElementResponse(
		String tipo,
		String imagen,
		String sonido,
		double velocidadDisparo,
		double rango,
		String mensaje) implements UICapable {

	@Override
	public String getImagen() {
		return imagen;
	}

	@Override
	public String getSonido() {
		return sonido;
	}

	@Override
	public double getVelocidadDisparo() {
		return velocidadDisparo;
	}

	@Override
	public double getRango() {
		return rango;
	}

	@Override
	public String mostrarEnUI() {
		return mensaje;
	}
}