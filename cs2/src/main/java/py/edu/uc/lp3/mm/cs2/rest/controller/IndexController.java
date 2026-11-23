package py.edu.uc.lp3.mm.cs2.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Salud", description = "Verificación de que el servicio está vivo.")
public class IndexController {

	@GetMapping("/")
	@Operation(operationId = "verificarServicio", summary = "Comprobar que el servicio responde")
	public String holaMundo() {
		return "Hola Mundo";
	}
}
