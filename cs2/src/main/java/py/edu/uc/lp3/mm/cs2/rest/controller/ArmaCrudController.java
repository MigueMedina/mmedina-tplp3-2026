package py.edu.uc.lp3.mm.cs2.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import py.edu.uc.lp3.mm.cs2.constants.ApiPaths;
import py.edu.uc.lp3.mm.cs2.domain.Arma;
import py.edu.uc.lp3.mm.cs2.domain.Fusil;
import py.edu.uc.lp3.mm.cs2.domain.Granada;
import py.edu.uc.lp3.mm.cs2.rest.dto.ArmaRequest;
import py.edu.uc.lp3.mm.cs2.service.ArmaService;
import py.edu.uc.lp3.mm.cs2.service.FusilService;
import py.edu.uc.lp3.mm.cs2.service.GranadaService;

@RestController
@RequestMapping(ApiPaths.ARMAS)
@Tag(name = "Armas (CRUD)", description = "CRUD persistente de la familia Arma y de las hijas Fusil y Granada.")
public class ArmaCrudController {

    private final ArmaService armaService;
    private final FusilService fusilService;
    private final GranadaService granadaService;

    public ArmaCrudController(ArmaService armaService,
                              FusilService fusilService,
                              GranadaService granadaService) {
        this.armaService = armaService;
        this.fusilService = fusilService;
        this.granadaService = granadaService;
    }

    // CRUD del padre Arma

    @GetMapping
    @Operation(operationId = "listarArmas", summary = "Listar armas persistidas",
            description = "Filtra opcionalmente por nombre, sin distinción de mayúsculas.")
    public List<Arma> listar(@RequestParam(required = false) String nombre) {
        return nombre == null || nombre.isBlank()
                ? armaService.listar()
                : armaService.buscarPorNombre(nombre);
    }

    @GetMapping("/{id}")
    @Operation(operationId = "buscarArma", summary = "Buscar un arma por id")
    @ApiResponse(responseCode = "404", description = "No existe un arma con ese id.")
    public Arma buscar(@PathVariable Integer id) {
        return armaService.buscar(id);
    }

    @PostMapping
    @Operation(operationId = "crearArma", summary = "Crear un arma",
            description = "El id lo asigna H2. Un precio negativo responde 400.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Arma creada con `tipoArma`."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, por ejemplo precio negativo.")
    })
    public ResponseEntity<Arma> crear(@RequestBody ArmaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(armaService.crear(request));
    }

    @PutMapping("/{id}")
    @Operation(operationId = "actualizarArma", summary = "Actualizar parcialmente un arma")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Arma actualizada."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos."),
            @ApiResponse(responseCode = "404", description = "No existe un arma con ese id.")
    })
    public Arma actualizar(@PathVariable Integer id, @RequestBody ArmaRequest request) {
        return armaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "eliminarArma", summary = "Eliminar un arma")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Arma eliminada."),
            @ApiResponse(responseCode = "404", description = "No existe un arma con ese id.")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        armaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // CRUD de la hija Fusil

    @GetMapping("/fusiles")
    @Operation(operationId = "listarFusiles", summary = "Listar fusiles persistidos")
    public List<Fusil> listarFusiles() {
        return fusilService.listar();
    }

    @GetMapping("/fusiles/{id}")
    @Operation(operationId = "buscarFusil", summary = "Buscar un fusil por id")
    @ApiResponse(responseCode = "404", description = "No existe un fusil con ese id.")
    public Fusil buscarFusil(@PathVariable Integer id) {
        return fusilService.buscar(id);
    }

    @PostMapping("/fusiles")
    @Operation(operationId = "crearFusil", summary = "Crear un fusil persistente")
    @ApiResponse(responseCode = "201", description = "Fusil creado.")
    public ResponseEntity<Fusil> crearFusil(@RequestBody ArmaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fusilService.crear(request));
    }

    @PutMapping("/fusiles/{id}")
    @Operation(operationId = "actualizarFusil", summary = "Actualizar un fusil")
    @ApiResponse(responseCode = "404", description = "No existe un fusil con ese id.")
    public Fusil actualizarFusil(@PathVariable Integer id, @RequestBody ArmaRequest request) {
        return fusilService.actualizar(id, request);
    }

    @DeleteMapping("/fusiles/{id}")
    @Operation(operationId = "eliminarFusil", summary = "Eliminar un fusil")
    @ApiResponse(responseCode = "204", description = "Fusil eliminado.")
    public ResponseEntity<Void> eliminarFusil(@PathVariable Integer id) {
        fusilService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // CRUD de la hija Granada

    @GetMapping("/granadas")
    @Operation(operationId = "listarGranadas", summary = "Listar granadas persistidas")
    public List<Granada> listarGranadas() {
        return granadaService.listar();
    }

    @GetMapping("/granadas/{id}")
    @Operation(operationId = "buscarGranada", summary = "Buscar una granada por id")
    @ApiResponse(responseCode = "404", description = "No existe una granada con ese id.")
    public Granada buscarGranada(@PathVariable Integer id) {
        return granadaService.buscar(id);
    }

    @PostMapping("/granadas")
    @Operation(operationId = "crearGranada", summary = "Crear una granada persistente")
    @ApiResponse(responseCode = "201", description = "Granada creada.")
    public ResponseEntity<Granada> crearGranada(@RequestBody ArmaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(granadaService.crear(request));
    }

    @PutMapping("/granadas/{id}")
    @Operation(operationId = "actualizarGranada", summary = "Actualizar una granada")
    @ApiResponse(responseCode = "404", description = "No existe una granada con ese id.")
    public Granada actualizarGranada(@PathVariable Integer id, @RequestBody ArmaRequest request) {
        return granadaService.actualizar(id, request);
    }

    @DeleteMapping("/granadas/{id}")
    @Operation(operationId = "eliminarGranada", summary = "Eliminar una granada")
    @ApiResponse(responseCode = "204", description = "Granada eliminada.")
    public ResponseEntity<Void> eliminarGranada(@PathVariable Integer id) {
        granadaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
