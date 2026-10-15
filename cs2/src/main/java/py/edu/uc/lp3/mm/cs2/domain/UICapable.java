package py.edu.uc.lp3.mm.cs2.domain;

public interface UICapable {

	String getImagen();

	String getSonido();

	double getVelocidadDisparo();

	double getRango();

	default String mostrarEnUI() {
		return "Mostrando " + getImagen() + " con sonido " + getSonido()
				+ " (velocidad de disparo " + getVelocidadDisparo() + ", rango " + getRango() + ")";
	}
}