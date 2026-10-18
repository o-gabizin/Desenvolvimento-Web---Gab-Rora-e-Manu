package dw.editora.service;

import dw.editora.entity.Autor;
import dw.editora.repository.ArtigoRepository;
import dw.editora.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private ArtigoRepository artigoRepository;

    public Autor salvar(Autor autor) {
        if (autor.getNome() == null || autor.getNome().isBlank()
                || autor.getEmail() == null || autor.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome e e-mail são obrigatórios");
        }

        String email = autor.getEmail().trim().toLowerCase();
        if (autorRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um autor com o e-mail " + email);
        }

        autor.setEmail(email);
        autor.setId(0);
        return autorRepository.save(autor);
    }

    public List<Autor> listar() {
        return autorRepository.findAll();
    }

    public Autor buscarPorId(long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado: " + id));
    }

    public void deletar(long id) {
        Autor autor = buscarPorId(id);

        if (artigoRepository.existsByAutorId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O autor possui artigos. Exclua os artigos antes de excluir o autor");
        }
        autorRepository.delete(autor);
    }

    public void deletarTodos() {
        if (artigoRepository.count() > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ainda existem artigos. Apague os artigos primeiro (DELETE /editora/)");
        }
        autorRepository.deleteAll();
    }
}
