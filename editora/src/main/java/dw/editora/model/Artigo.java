package dw.editora.model;

import jakarta.persistence.*;

@Entity
@Table(name = "artigo")
public class Artigo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 80, nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String resumo;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean publicado = true;

    @ManyToOne
    @JoinColumn(name = "id_autor")
    private Autor autor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public boolean isPublicado() {
        return publicado;
    }

    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
