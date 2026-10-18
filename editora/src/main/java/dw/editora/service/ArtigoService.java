package dw.editora.service;

import dw.editora.entity.Artigo;
import dw.editora.entity.Autor;
import dw.editora.repository.ArtigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ArtigoService {

    @Autowired
    private ArtigoRepository artigoRepository;

    @Autowired
    private AutorService autorService;

    public List<Artigo> listar(String titulo) {
        if (titulo == null) {
            return artigoRepository.findAll();
        }
        return artigoRepository.findByTituloContaining(titulo);
    }

    public List<Artigo> listarPublicados() {
        return artigoRepository.findByPublicado(true);
    }

    public List<Artigo> listarPorAutor(long autorId, Boolean publicado) {
        autorService.buscarPorId(autorId); // 404 se o autor não existir
        if (publicado == null) {
            return artigoRepository.findByAutorId(autorId);
        }
        return artigoRepository.findByAutorIdAndPublicado(autorId, publicado);
    }

    public Artigo buscarPorId(long id) {
        return artigoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artigo não encontrado: " + id));
    }

    public Artigo criar(Artigo artigo) {
        validar(artigo);
        if (artigo.getAutor() == null || artigo.getAutor().getId() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Informe o autor do artigo. Exemplo: \"autor\": { \"id\": 1 }");
        }
        artigo.setAutor(buscarAutorReal(artigo));
        artigo.setId(0);
        return artigoRepository.save(artigo);
    }

    public Artigo atualizar(long id, Artigo dados) {
        validar(dados);
        Artigo artigo = buscarPorId(id);
        artigo.setTitulo(dados.getTitulo());
        artigo.setResumo(dados.getResumo());
        artigo.setPublicado(dados.isPublicado());
        if (dados.getAutor() != null) {
            artigo.setAutor(buscarAutorReal(dados));
        }
        return artigoRepository.save(artigo);
    }

    public void deletar(long id) {
        artigoRepository.delete(buscarPorId(id));
    }

    public void deletarTodos() {
        artigoRepository.deleteAll();
    }

    private void validar(Artigo artigo) {
        if (artigo.getTitulo() == null || artigo.getTitulo().isBlank()
                || artigo.getResumo() == null || artigo.getResumo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Título e resumo são obrigatórios");
        }
        if (artigo.getTitulo().length() > 80) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O título pode ter no máximo 80 caracteres");
        }
    }

    private Autor buscarAutorReal(Artigo artigo) {
        return autorService.buscarPorId(artigo.getAutor().getId());
    }
}
