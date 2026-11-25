package py.edu.uc.lp3.mm.cs2.rest.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

import py.edu.uc.lp3.mm.cs2.domain.EscenarioCoordinado;

@Schema(description = "Escenario persistido tras recibir la aprobación de A2 y A3.")
public record EscenarioResponse(
        Long id,
        SimulacionResponse simulacion,
        Aprobacion a2,
        Aprobacion a3,
        LocalDateTime fecha) {

    public static EscenarioResponse desde(EscenarioCoordinado entidad) {
        return new EscenarioResponse(
                entidad.getId(),
                SimulacionResponse.desde(entidad.getSimulacion()),
                new Aprobacion("A2", entidad.getA2Status(), entidad.getA2Cuerpo(), entidad.getA2Operacion()),
                new Aprobacion("A3", entidad.getA3Status(), entidad.getA3Cuerpo(), entidad.getA3Operacion()),
                entidad.getFecha());
    }

    public record Aprobacion(String companero, int status, String cuerpo, String operacion) {
    }
}
