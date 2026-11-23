package py.edu.uc.lp3.mm.cs2.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import py.edu.uc.lp3.mm.cs2.constants.ApiPaths;
import py.edu.uc.lp3.mm.cs2.domain.UICapable;
import py.edu.uc.lp3.mm.cs2.rest.response.UIElementResponse;
import py.edu.uc.lp3.mm.cs2.service.UICapableService;

@RestController
@Tag(name = "Interfaz", description = "Elementos polimórficos que implementan UICapable.")
public class UIController {

	private final UICapableService uiCapableService;

	public UIController(UICapableService uiCapableService) {
		this.uiCapableService = uiCapableService;
	}

	@GetMapping(ApiPaths.UI_CAPABLES)
	@Operation(operationId = "listarUICapables", summary = "Listar armas y jugadores con interfaz",
			description = "Lee de H2 y devuelve los elementos que implementan `UICapable`.")
	public List<UICapable> obtenerElementosUI() {
		return uiCapableService.listar().stream()
				.map(this::crearRespuesta)
				.toList();
	}

	private UICapable crearRespuesta(UICapable elemento) {
		return new UIElementResponse(
				elemento.getClass().getSimpleName(),
				elemento.getImagen(),
				elemento.getSonido(),
				elemento.getVelocidadDisparo(),
				elemento.getRango(),
				elemento.mostrarEnUI());
	}
}
