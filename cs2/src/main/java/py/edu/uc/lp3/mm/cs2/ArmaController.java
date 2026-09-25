package py.edu.uc.lp3.mm.cs2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.mm.cs2.Arma;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ArmaController {

	@GetMapping("/arma")
	public Arma crearArma(
			@RequestParam(defaultValue = "pistola") String tipo,
			@RequestParam String nombre,
			@RequestParam int id,
			@RequestParam double precio,
			@RequestParam int dano,
			@RequestParam int precision,
			@RequestParam double recarga,
			@RequestParam double velocidad,
			@RequestParam int capacidadCargador,
			@RequestParam boolean automatica,
			@RequestParam boolean silenciador,
			@RequestParam int mira,
			@RequestParam int retroceso,
			@RequestParam double radio,
			@RequestParam double duracionCeguera,
			@RequestParam int intensidad) {
		Arma arma;
		switch (tipo) {
			case "fusil":
				arma = new Fusil(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador,
						automatica, mira, retroceso, silenciador);
				break;
			case "flash":
				arma = new Flash(nombre, id, precio, "flash", radio, 0, 0, 0, intensidad, duracionCeguera);
				break;
			case "pistola":
			default:
				arma = new Pistola(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador,
						automatica, silenciador);
				break;
		}
		return arma;
	}

	@GetMapping("/arma/accion")
	public Map<String, Object> accionArma(
			@RequestParam(defaultValue = "pistola") String tipo,
			@RequestParam String nombre,
			@RequestParam int id,
			@RequestParam double precio,
			@RequestParam int dano,
			@RequestParam int precision,
			@RequestParam double recarga,
			@RequestParam double velocidad,
			@RequestParam int capacidadCargador,
			@RequestParam boolean automatica,
			@RequestParam boolean silenciador,
			@RequestParam int mira,
			@RequestParam int retroceso,
			@RequestParam double radio,
			@RequestParam double duracionCeguera,
			@RequestParam int intensidad) {
		Arma arma;
		switch (tipo) {
			case "fusil":
				arma = new Fusil(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador,
						automatica, mira, retroceso, silenciador);
				break;
			case "flash":
				arma = new Flash(nombre, id, precio, "flash", radio, 0, 0, 0, intensidad, duracionCeguera);
				break;
			case "pistola":
			default:
				arma = new Pistola(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador,
						automatica, silenciador);
				break;
		}
		Map<String, Object> resultado = new HashMap<>();
		resultado.put("arma_generada", arma);
		resultado.put("mensaje_recarga", arma.recargar());
		resultado.put("mensaje_tienda", arma.obtenerDetalleTienda());
		return resultado;
	}
}