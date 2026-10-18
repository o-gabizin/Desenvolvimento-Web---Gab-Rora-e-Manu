package dw.editora.control;

import dw.editora.entity.Artigo;
import dw.editora.entity.Autor;
import dw.editora.service.ArtigoService;
import dw.editora.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @Autowired
    private ArtigoService artigoService;

    @PostMapping
    public ResponseEntity<Autor> cadastrar(@RequestBody Autor autor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.salvar(autor)); // 201, 400/409
    }

    @GetMapping
    public ResponseEntity<List<Autor>> listar() {
        return ResponseEntity.ok(autorService.listar()); //200
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> buscarPorId(@PathVariable("id") long id) {
        return ResponseEntity.ok(autorService.buscarPorId(id)); // 200/404
    }

    @GetMapping("/{id}/artigos")
    public ResponseEntity<List<Artigo>> listarArtigosDoAutor(
            @PathVariable("id") long id,
            @RequestParam(name = "publicado", required = false, defaultValue = "true") Boolean publicado) {
        return ResponseEntity.ok(artigoService.listarPorAutor(id, publicado)); // 200/404
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") long id) {
        autorService.deletar(id);
        return ResponseEntity.noContent().build(); // 204/404/409
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarTodos() {
        autorService.deletarTodos();
        return ResponseEntity.noContent().build();
    }
}
