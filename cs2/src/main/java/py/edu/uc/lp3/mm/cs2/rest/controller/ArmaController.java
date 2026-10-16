package py.edu.uc.lp3.mm.cs2.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.mm.cs2.constants.ApiPaths;
import py.edu.uc.lp3.mm.cs2.domain.AWP;
import py.edu.uc.lp3.mm.cs2.domain.Arma;
import py.edu.uc.lp3.mm.cs2.domain.Flash;
import py.edu.uc.lp3.mm.cs2.domain.Fusil;
import py.edu.uc.lp3.mm.cs2.domain.Granada;
import py.edu.uc.lp3.mm.cs2.domain.Pistola;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ArmaController {

	@GetMapping(ApiPaths.ARMA)
	public Arma crearArma(
			@RequestParam(defaultValue = "pistola") String tipo,
			@RequestParam(defaultValue = "Arma") String nombre,
			@RequestParam(defaultValue = "1") int id,
			@RequestParam(defaultValue = "100") double precio,
			@RequestParam(defaultValue = "10") int dano,
			@RequestParam(defaultValue = "50") int precision,
			@RequestParam(defaultValue = "2.0") double recarga,
			@RequestParam(defaultValue = "5.0") double velocidad,
			@RequestParam(defaultValue = "30") int capacidadCargador,
			@RequestParam(defaultValue = "false") boolean automatica,
			@RequestParam(defaultValue = "false") boolean silenciador,
			@RequestParam(defaultValue = "1") int mira,
			@RequestParam(defaultValue = "10") int retroceso,
			@RequestParam(defaultValue = "1.0") double radio,
			@RequestParam(defaultValue = "1.0") double duracionCeguera,
			@RequestParam(defaultValue = "50") int intensidad) {
		Arma arma;
		switch (tipo) {
			case "fusil":
				arma = new Fusil(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador,
						automatica, mira, retroceso, silenciador);
				break;
			case "awp":
				arma = new AWP(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador,
						mira, retroceso, silenciador);
				break;
			case "granada":
				arma = new Granada(nombre, id, precio, "granada", radio, 0, 0, 0, dano, automatica);
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

	@GetMapping(ApiPaths.ARMA_ACCION)
	public Map<String, Object> accionArma(
			@RequestParam(defaultValue = "pistola") String tipo,
			@RequestParam(defaultValue = "Arma") String nombre,
			@RequestParam(defaultValue = "1") int id,
			@RequestParam(defaultValue = "100") double precio,
			@RequestParam(defaultValue = "10") int dano,
			@RequestParam(defaultValue = "50") int precision,
			@RequestParam(defaultValue = "2.0") double recarga,
			@RequestParam(defaultValue = "5.0") double velocidad,
			@RequestParam(defaultValue = "30") int capacidadCargador,
			@RequestParam(defaultValue = "false") boolean automatica,
			@RequestParam(defaultValue = "false") boolean silenciador,
			@RequestParam(defaultValue = "1") int mira,
			@RequestParam(defaultValue = "10") int retroceso,
			@RequestParam(defaultValue = "1.0") double radio,
			@RequestParam(defaultValue = "1.0") double duracionCeguera,
			@RequestParam(defaultValue = "50") int intensidad) {
		Arma arma;
		switch (tipo) {
			case "fusil":
				arma = new Fusil(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador,
						automatica, mira, retroceso, silenciador);
				break;
			case "awp":
				arma = new AWP(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador,
						mira, retroceso, silenciador);
				break;
			case "granada":
				arma = new Granada(nombre, id, precio, "granada", radio, 0, 0, 0, dano, automatica);
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