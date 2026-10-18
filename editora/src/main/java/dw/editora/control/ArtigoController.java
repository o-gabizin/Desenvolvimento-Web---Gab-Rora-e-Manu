package dw.editora.control;

import dw.editora.entity.Artigo;
import dw.editora.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArtigoController {

    @Autowired
    private ArtigoService artigoService;

    @GetMapping("/")
    public ResponseEntity<List<Artigo>> listar(@RequestParam(name = "titulo", required = false) String titulo) {
        return ResponseEntity.ok(artigoService.listar(titulo)); //200
    }

    @GetMapping("/publicado")
    public ResponseEntity<List<Artigo>> listarPublicados() {
        return ResponseEntity.ok(artigoService.listarPublicados()); //200
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artigo> buscarPorId(@PathVariable("id") long id) {
        return ResponseEntity.ok(artigoService.buscarPorId(id)); // 200/404
    }

    @PostMapping({"/", "/api/artigos"})
    public ResponseEntity<Artigo> criar(@RequestBody Artigo artigo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artigoService.criar(artigo)); //201
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artigo> atualizar(@PathVariable("id") long id, @RequestBody Artigo artigo) {
        return ResponseEntity.ok(artigoService.atualizar(id, artigo)); // 200/404
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") long id) {
        artigoService.deletar(id);
        return ResponseEntity.noContent().build(); // 204/404
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deletarTodos() {
        artigoService.deletarTodos();
        return ResponseEntity.noContent().build(); //204
    }
}