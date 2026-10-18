package dw.editora.repository;

import dw.editora.entity.Artigo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtigoRepository extends JpaRepository<Artigo, Long> {

    List<Artigo> findByPublicado(boolean publicado);

    List<Artigo> findByTituloContaining(String titulo);

    List<Artigo> findByAutorId(long autorId);

    List<Artigo> findByAutorIdAndPublicado(long autorId, boolean publicado);

    boolean existsByAutorId(long autorId);
}
