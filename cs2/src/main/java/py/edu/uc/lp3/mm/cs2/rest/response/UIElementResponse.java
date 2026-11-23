package py.edu.uc.lp3.mm.cs2.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;

import py.edu.uc.lp3.mm.cs2.domain.UICapable;

@Schema(description = "Representación polimórfica de un elemento con interfaz de usuario.")
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