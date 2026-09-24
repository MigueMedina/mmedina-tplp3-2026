package py.edu.uc.lp3.mm.cs2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArmaController {

	@GetMapping("/arma")
	public Pistola crearArma(
			@RequestParam String nombre,
			@RequestParam int id,
			@RequestParam double precio,
			@RequestParam int dano,
			@RequestParam int precision,
			@RequestParam double recarga,
			@RequestParam double velocidad,
			@RequestParam boolean automatica,
			@RequestParam boolean silenciador) {
		Pistola pistola = new Pistola(nombre, id, precio, dano, precision, recarga, velocidad, automatica, silenciador);
		return pistola;
	}
}