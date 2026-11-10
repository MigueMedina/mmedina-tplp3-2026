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
    public List<Arma> listar(@RequestParam(required = false) String nombre) {
        return nombre == null || nombre.isBlank()
                ? armaService.listar()
                : armaService.buscarPorNombre(nombre);
    }

    @GetMapping("/{id}")
    public Arma buscar(@PathVariable Integer id) {
        return armaService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Arma> crear(@RequestBody ArmaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(armaService.crear(request));
    }

    @PutMapping("/{id}")
    public Arma actualizar(@PathVariable Integer id, @RequestBody ArmaRequest request) {
        return armaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        armaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // CRUD de la hija Fusil

    @GetMapping("/fusiles")
    public List<Fusil> listarFusiles() {
        return fusilService.listar();
    }

    @GetMapping("/fusiles/{id}")
    public Fusil buscarFusil(@PathVariable Integer id) {
        return fusilService.buscar(id);
    }

    @PostMapping("/fusiles")
    public ResponseEntity<Fusil> crearFusil(@RequestBody ArmaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fusilService.crear(request));
    }

    @PutMapping("/fusiles/{id}")
    public Fusil actualizarFusil(@PathVariable Integer id, @RequestBody ArmaRequest request) {
        return fusilService.actualizar(id, request);
    }

    @DeleteMapping("/fusiles/{id}")
    public ResponseEntity<Void> eliminarFusil(@PathVariable Integer id) {
        fusilService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // CRUD de la hija Granada

    @GetMapping("/granadas")
    public List<Granada> listarGranadas() {
        return granadaService.listar();
    }

    @GetMapping("/granadas/{id}")
    public Granada buscarGranada(@PathVariable Integer id) {
        return granadaService.buscar(id);
    }

    @PostMapping("/granadas")
    public ResponseEntity<Granada> crearGranada(@RequestBody ArmaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(granadaService.crear(request));
    }

    @PutMapping("/granadas/{id}")
    public Granada actualizarGranada(@PathVariable Integer id, @RequestBody ArmaRequest request) {
        return granadaService.actualizar(id, request);
    }

    @DeleteMapping("/granadas/{id}")
    public ResponseEntity<Void> eliminarGranada(@PathVariable Integer id) {
        granadaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
