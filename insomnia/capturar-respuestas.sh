#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

python3 - "$SCRIPT_DIR" <<'PY'
import json
import os
import subprocess
import sys
import urllib.error
import urllib.request
from datetime import datetime
from pathlib import Path

script_dir = Path(sys.argv[1])
root_dir = script_dir.parent
salida = Path(os.environ.get("CAPTURA_DIR", script_dir / "respuestas" / "capturas"))
salida.mkdir(parents=True, exist_ok=True)

a1 = os.environ.get("A1_BASE_URL", "http://localhost:8080").rstrip("/")
a2 = os.environ.get("A2_BASE_URL", "http://localhost:8081").rstrip("/")
a3 = os.environ.get("A3_BASE_URL", "http://localhost:8082").rstrip("/")
a1_openapi = os.environ.get("A1_OPENAPI_URL", a1 + "/v3/api-docs")
a2_openapi = os.environ.get("A2_OPENAPI_URL", a2 + "/v3/api-docs")
a3_openapi = os.environ.get("A3_OPENAPI_URL", a3 + "/v3/api-docs")

sello = datetime.now().astimezone().strftime("%Y%m%dT%H%M%S%z")
prefijo = "captura-" + sello
registros = []

try:
    a1_commit = subprocess.run(
        ["git", "-C", str(root_dir), "rev-parse", "--short", "HEAD"],
        check=True, capture_output=True, text=True).stdout.strip()
except Exception:
    a1_commit = "sin-commit"
a2_commit = os.environ.get("A2_COMMIT", "sin-commit-a2")
a3_commit = os.environ.get("A3_COMMIT", "sin-commit-a3")


def capturar(nombre, metodo, url, cuerpo=None, esperado=None):
    datos = None if cuerpo is None else json.dumps(cuerpo, ensure_ascii=False).encode("utf-8")
    solicitud = urllib.request.Request(
        url,
        data=datos,
        method=metodo,
        headers={"Content-Type": "application/json", "Accept": "application/json"})
    try:
        with urllib.request.urlopen(solicitud, timeout=20) as respuesta:
            status = respuesta.status
            texto = respuesta.read().decode("utf-8", errors="replace")
    except urllib.error.HTTPError as error:
        status = error.code
        texto = error.read().decode("utf-8", errors="replace")
    except urllib.error.URLError as error:
        raise SystemExit("No se pudo conectar con " + url + ": " + str(error.reason))

    if not texto:
        cuerpo_respuesta = None
    else:
        try:
            cuerpo_respuesta = json.loads(texto)
        except json.JSONDecodeError:
            cuerpo_respuesta = texto

    registro = {
        "nombre": nombre,
        "fecha": datetime.now().astimezone().isoformat(timespec="seconds"),
        "metodo": metodo,
        "url": url,
        "cuerpoEnviado": cuerpo,
        "statusHttp": status,
        "respuesta": cuerpo_respuesta,
    }
    ruta = salida / (nombre + ".json")
    ruta.write_text(json.dumps(registro, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    registros.append((nombre, metodo, url, status))
    print(f"{nombre}: {status}")

    if esperado is not None and status != esperado:
        raise SystemExit(f"Se esperaba HTTP {esperado} en {nombre}, pero llegó {status}. Ver {ruta}")

    return status, cuerpo_respuesta


capturar("00-openapi-a1", "GET", a1_openapi, esperado=200)
capturar("01-openapi-a2", "GET", a2_openapi, esperado=200)
capturar("02-openapi-a3", "GET", a3_openapi, esperado=200)
capturar("03-catalogo-a1", "GET", a1 + "/api/catalogo", esperado=200)
capturar("04-alias-municion-a1", "POST", a1 + "/api/companeros/municion",
         {"carga": 30, "dano": 36}, esperado=200)
capturar("05-alias-danio-a1", "POST", a1 + "/api/companeros/danio",
         {"vida": 100, "blindaje": 0}, esperado=200)

_, jugador = capturar("06-crear-jugador", "POST", a1 + "/jugadores",
                      {"nombre": prefijo + "-objetivo", "blindaje": 0}, esperado=201)
jugador_id = jugador["id"]

_, fusil = capturar("07-crear-fusil", "POST", a1 + "/armas/fusiles",
                    {"nombre": prefijo + "-AK47", "dano": 36, "precision": 75,
                     "capacidadCargador": 30, "precio": 2700}, esperado=201)
fusil_id = fusil["id"]

_, fusil_vacio = capturar("08-crear-fusil-sin-municion", "POST", a1 + "/armas/fusiles",
                          {"nombre": prefijo + "-AK47-vacio", "dano": 36, "precision": 75,
                           "capacidadCargador": 0, "precio": 2700}, esperado=201)
fusil_vacio_id = fusil_vacio["id"]

capturar("09-regla-a2-aceptada", "POST", a2 + "/api/reglas/municion",
         {"cargador": 30, "balasPorDisparo": 1, "disparos": 1}, esperado=200)
capturar("10-regla-a3-aceptada", "POST", a3 + "/api/reglas/danio",
         {"tirador": "A1-MigueMedina", "equipoTirador": "TERRORISTAS",
          "objetivo": jugador["nombre"], "equipoObjetivo": "ANTITERRORISTAS",
          "vidaObjetivo": 100, "blindajeObjetivo": 0, "danio": 36}, esperado=200)
capturar("11-regla-a2-rechazada", "POST", a2 + "/api/reglas/municion",
         {"cargador": 0, "balasPorDisparo": 1, "disparos": 1}, esperado=409)
capturar("12-regla-a3-rechazada", "POST", a3 + "/api/reglas/danio",
         {"tirador": "A1-MigueMedina", "equipoTirador": "TERRORISTAS",
          "objetivo": jugador["nombre"], "equipoObjetivo": "TERRORISTAS",
          "vidaObjetivo": 100, "blindajeObjetivo": 0, "danio": 36}, esperado=422)

clave_feliz = prefijo + "-feliz"
feliz_body = {"jugadorId": jugador_id, "armaId": fusil_id,
              "equipoTirador": "TERRORISTAS", "equipoObjetivo": "ANTITERRORISTAS",
              "idempotencyKey": clave_feliz}
_, feliz = capturar("13-escenario-feliz", "POST", a1 + "/escenarios/disparo-coordinado",
                    feliz_body, esperado=201)
escenario_id = feliz["id"]

_, repetido = capturar("14-escenario-idempotente", "POST", a1 + "/escenarios/disparo-coordinado",
                       feliz_body, esperado=201)
if repetido["id"] != escenario_id:
    raise SystemExit("La repetición con la misma idempotencyKey devolvió otro id")

capturar("15-get-escenario", "GET", a1 + "/escenarios/" + str(escenario_id), esperado=200)
_, lista_tras_feliz = capturar("16-escenarios-tras-feliz", "GET", a1 + "/escenarios", esperado=200)

capturar("17-rechazo-a2", "POST", a1 + "/escenarios/disparo-coordinado",
         {"jugadorId": jugador_id, "armaId": fusil_vacio_id,
          "equipoTirador": "TERRORISTAS", "equipoObjetivo": "ANTITERRORISTAS",
          "idempotencyKey": prefijo + "-rechazo-a2"}, esperado=409)

capturar("18-rechazo-a3", "POST", a1 + "/escenarios/disparo-coordinado",
         {"jugadorId": jugador_id, "armaId": fusil_id,
          "equipoTirador": "TERRORISTAS", "equipoObjetivo": "TERRORISTAS",
          "idempotencyKey": prefijo + "-rechazo-a3"}, esperado=409)

capturar("19-validacion-400", "POST", a1 + "/escenarios/disparo-coordinado",
         {"armaId": fusil_id, "idempotencyKey": prefijo + "-invalido"}, esperado=400)

_, lista_tras_rechazos = capturar("20-escenarios-tras-rechazos", "GET", a1 + "/escenarios", esperado=200)

if len(lista_tras_rechazos) != len(lista_tras_feliz):
    raise SystemExit("Los rechazos o la validación 400 cambiaron la cantidad de escenarios")

resumen = [
    "# Captura reproducible POO-09",
    "",
    f"- Fecha: {sello}",
    f"- A1: {a1} (commit `{a1_commit}`, working tree)",
    f"- A2: {a2} (commit `{a2_commit}`)",
    f"- A3: {a3} (commit `{a3_commit}`)",
    "- Regeneración: levantar los tres servicios y ejecutar `bash insomnia/capturar-respuestas.sh`.",
    f"- Escenario feliz: HTTP 201, id `{escenario_id}`.",
    f"- Idempotencia: la repetición con `{clave_feliz}` devolvió el mismo id.",
    f"- Rechazo A2: HTTP 409 con `rechazadoPor: A2`.",
    f"- Rechazo A3: HTTP 409 con `rechazadoPor: A3`.",
    f"- Validación: HTTP 400 con `campo`, `errores` y `traceId`.",
    f"- Escenarios antes de los rechazos: {len(lista_tras_feliz)}.",
    f"- Escenarios después de los rechazos: {len(lista_tras_rechazos)}.",
    "",
    "| Archivo | Método | URL | HTTP |",
    "|---|---|---|---|",
]
for nombre, metodo, url, status in registros:
    resumen.append(f"| [`{nombre}.json`]({nombre}.json) | {metodo} | {url} | {status} |")

(salida / "resumen.md").write_text("\n".join(resumen) + "\n", encoding="utf-8")
print("Capturas guardadas en " + str(salida))
PY
