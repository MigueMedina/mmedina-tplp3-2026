package py.edu.uc.lp3.mm.cs2.service;

import py.edu.uc.lp3.mm.cs2.domain.AWP;
import py.edu.uc.lp3.mm.cs2.domain.Arma;
import py.edu.uc.lp3.mm.cs2.domain.ArmaDeFuego;
import py.edu.uc.lp3.mm.cs2.domain.Arrojadiza;
import py.edu.uc.lp3.mm.cs2.domain.Escopeta;
import py.edu.uc.lp3.mm.cs2.domain.Flash;
import py.edu.uc.lp3.mm.cs2.domain.Fusil;
import py.edu.uc.lp3.mm.cs2.domain.Granada;
import py.edu.uc.lp3.mm.cs2.domain.Humo;
import py.edu.uc.lp3.mm.cs2.domain.Pistola;
import py.edu.uc.lp3.mm.cs2.rest.dto.ArmaRequest;

/**
 * Traduce entre el DTO de entrada y la jerarquía de armas.
 */
public final class ArmaMapper {

    private ArmaMapper() {
    }

    public static Arma crear(ArmaRequest request) {
        String tipo = texto(request.tipo(), "pistola").toLowerCase();
        return switch (tipo) {
            case "fusil" -> crearFusil(request);
            case "awp" -> crearAwp(request);
            case "escopeta" -> crearEscopeta(request);
            case "granada" -> crearGranada(request);
            case "flash" -> crearFlash(request);
            case "humo" -> crearHumo(request);
            default -> crearPistola(request);
        };
    }

    public static Fusil crearFusil(ArmaRequest request) {
        return new Fusil(
                texto(request.nombre(), "Fusil"),
                0,
                decimal(request.precio(), 2700),
                entero(request.dano(), 36),
                entero(request.precision(), 75),
                decimal(request.recarga(), 2.5),
                decimal(request.velocidad(), 10),
                entero(request.capacidadCargador(), 30),
                booleano(request.automatica(), true),
                entero(request.mira(), 2),
                entero(request.retroceso(), 20),
                booleano(request.silenciador(), false));
    }

    public static Granada crearGranada(ArmaRequest request) {
        return new Granada(
                texto(request.nombre(), "Granada"),
                0,
                decimal(request.precio(), 300),
                texto(request.tipo(), "granada"),
                decimal(request.radio(), 6),
                decimal(request.distancia(), 30),
                decimal(request.duracion(), 2),
                decimal(request.cooldown(), 1),
                entero(request.dano(), 98),
                booleano(request.aturdimiento(), true));
    }

    private static AWP crearAwp(ArmaRequest request) {
        return new AWP(
                texto(request.nombre(), "AWP"),
                0,
                decimal(request.precio(), 4750),
                entero(request.dano(), 115),
                entero(request.precision(), 98),
                decimal(request.recarga(), 3.7),
                decimal(request.velocidad(), 5),
                entero(request.capacidadCargador(), 10),
                entero(request.mira(), 2),
                entero(request.retroceso(), 30),
                booleano(request.silenciador(), false));
    }

    private static Pistola crearPistola(ArmaRequest request) {
        return new Pistola(
                texto(request.nombre(), "Pistola"),
                0,
                decimal(request.precio(), 200),
                entero(request.dano(), 35),
                entero(request.precision(), 80),
                decimal(request.recarga(), 2.2),
                decimal(request.velocidad(), 8),
                entero(request.capacidadCargador(), 12),
                booleano(request.automatica(), false),
                booleano(request.silenciador(), false));
    }

    private static Escopeta crearEscopeta(ArmaRequest request) {
        return new Escopeta(
                texto(request.nombre(), "Escopeta"),
                0,
                decimal(request.precio(), 1200),
                entero(request.dano(), 80),
                entero(request.precision(), 60),
                decimal(request.recarga(), 3),
                decimal(request.velocidad(), 4),
                entero(request.capacidadCargador(), 8),
                entero(request.perdigon(), 9),
                decimal(request.dispersion(), 2.5));
    }

    private static Flash crearFlash(ArmaRequest request) {
        return new Flash(
                texto(request.nombre(), "Flashbang"),
                0,
                decimal(request.precio(), 200),
                texto(request.tipo(), "flash"),
                decimal(request.radio(), 5),
                decimal(request.distancia(), 20),
                decimal(request.duracion(), 2),
                decimal(request.cooldown(), 0.5),
                entero(request.intensidad(), 90),
                decimal(request.duracionCeguera(), 3.5));
    }

    private static Humo crearHumo(ArmaRequest request) {
        return new Humo(
                texto(request.nombre(), "Smoke"),
                0,
                decimal(request.precio(), 300),
                texto(request.tipo(), "humo"),
                decimal(request.radio(), 5),
                decimal(request.distancia(), 20),
                decimal(request.duracion(), 15),
                decimal(request.cooldown(), 0));
    }

    public static void aplicar(Arma arma, ArmaRequest request) {
        if (request.nombre() != null) {
            arma.setNombre(request.nombre());
        }
        if (request.precio() != null) {
            arma.setPrecio(request.precio());
        }

        if (arma instanceof ArmaDeFuego armaDeFuego) {
            if (request.dano() != null) {
                armaDeFuego.setDano(request.dano());
            }
            if (request.precision() != null) {
                armaDeFuego.setPrecision(request.precision());
            }
            if (request.recarga() != null) {
                armaDeFuego.setRecarga(request.recarga());
            }
            if (request.velocidad() != null) {
                armaDeFuego.setVelocidad(request.velocidad());
            }
            if (request.capacidadCargador() != null) {
                armaDeFuego.setCapacidadCargador(request.capacidadCargador());
            }
        }

        if (arma instanceof Arrojadiza arrojadiza) {
            if (request.tipo() != null) {
                arrojadiza.setTipo(request.tipo());
            }
            if (request.radio() != null) {
                arrojadiza.setRadio(request.radio());
            }
            if (request.distancia() != null) {
                arrojadiza.setDistancia(request.distancia());
            }
            if (request.duracion() != null) {
                arrojadiza.setDuracion(request.duracion());
            }
            if (request.cooldown() != null) {
                arrojadiza.setCooldown(request.cooldown());
            }
        }

        if (arma instanceof Fusil fusil) {
            if (request.automatica() != null) {
                fusil.setAutomatica(request.automatica());
            }
            if (request.mira() != null) {
                fusil.setMira(request.mira());
            }
            if (request.retroceso() != null) {
                fusil.setRetroceso(request.retroceso());
            }
            if (request.silenciador() != null) {
                fusil.setSilenciador(request.silenciador());
            }
        }

        if (arma instanceof AWP awp) {
            if (request.mira() != null) {
                awp.setMira(request.mira());
            }
            if (request.retroceso() != null) {
                awp.setRetroceso(request.retroceso());
            }
            if (request.silenciador() != null) {
                awp.setSilenciador(request.silenciador());
            }
        }

        if (arma instanceof Pistola pistola) {
            if (request.automatica() != null) {
                pistola.setAutomatica(request.automatica());
            }
            if (request.silenciador() != null) {
                pistola.setSilenciador(request.silenciador());
            }
        }

        if (arma instanceof Escopeta escopeta) {
            if (request.perdigon() != null) {
                escopeta.setPerdigon(request.perdigon());
            }
            if (request.dispersion() != null) {
                escopeta.setDispersion(request.dispersion());
            }
        }

        if (arma instanceof Granada granada) {
            if (request.dano() != null) {
                granada.setDano(request.dano());
            }
            if (request.aturdimiento() != null) {
                granada.setAturdimiento(request.aturdimiento());
            }
        }

        if (arma instanceof Flash flash) {
            if (request.intensidad() != null) {
                flash.setIntensidad(request.intensidad());
            }
            if (request.duracionCeguera() != null) {
                flash.setDuracionCeguera(request.duracionCeguera());
            }
        }
    }

    private static String texto(String valor, String porDefecto) {
        return valor == null || valor.isBlank() ? porDefecto : valor;
    }

    private static int entero(Integer valor, int porDefecto) {
        return valor == null ? porDefecto : valor;
    }

    private static double decimal(Double valor, double porDefecto) {
        return valor == null ? porDefecto : valor;
    }

    private static boolean booleano(Boolean valor, boolean porDefecto) {
        return valor == null ? porDefecto : valor;
    }
}
