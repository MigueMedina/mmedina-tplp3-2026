package py.edu.uc.lp3.mm.cs2.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.mm.cs2.constants.ApiPaths;
import py.edu.uc.lp3.mm.cs2.domain.AWP;
import py.edu.uc.lp3.mm.cs2.domain.Flash;
import py.edu.uc.lp3.mm.cs2.domain.Fusil;
import py.edu.uc.lp3.mm.cs2.domain.Granada;
import py.edu.uc.lp3.mm.cs2.domain.UICapable;
import py.edu.uc.lp3.mm.cs2.rest.response.UIElementResponse;

@RestController
public class UIController {

	@GetMapping(ApiPaths.UI_CAPABLES)
	public List<UIElementResponse> obtenerElementosUI() {
		UICapable fusil = new Fusil("AK-47", 1, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false);
		UICapable flash = new Flash("Flashbang", 2, 200, "flash", 5, 20, 2, 0.5, 90, 3.5);
		UICapable awp = new AWP("AWP", 3, 4750, 115, 98, 3.7, 5, 10, 2, 30, false);
		UICapable granada = new Granada("HE Grenade", 4, 300, "granada", 6, 30, 2, 1, 98, true);

		return List.of(
				crearRespuesta("Fusil", fusil),
				crearRespuesta("Flash", flash),
				crearRespuesta("AWP", awp),
				crearRespuesta("Granada", granada));
	}

	private UIElementResponse crearRespuesta(String tipo, UICapable elemento) {
		return new UIElementResponse(
				tipo,
				elemento.getImagen(),
				elemento.getSonido(),
				elemento.getVelocidadDisparo(),
				elemento.getRango(),
				elemento.mostrarEnUI());
	}
}