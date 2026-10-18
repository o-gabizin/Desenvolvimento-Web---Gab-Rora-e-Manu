# Projeto Editora

Trabalho 01 de Desenvolvimento Web - UEPG. O seguinte trabalho é uma atualização da API da editora que já haviamos feito anteriormente, mas agora cada artigo tem um **autor**. Feito com Spring Boot, JPA/Hibernate e PostgreSQL.

Alunos: Aurora Zênite, Gabriel Martins e Manuella Bottega

## Como rodar

1. Ligue o PostgreSQL e crie um banco chamado `editora` no pgAdmin.
2. No `application.properties`, troca `SUA_SENHA` pela senha do seu Postgres.
3. No terminal do VS Code: `mvn spring-boot:run`

Vai subir em `http://localhost:8000/editora/`. As tabelas `autor` e `artigo` o Hibernate cria sozinho.

Pra testar tem o arquivo `requests.http` (extensão REST Client do VS Code).

## O que mudamos

- Criamos a entidade **Autor** (id, nome, email único, biografia) com repository, service e controller.
- Relação 1:N: um autor escreve vários artigos, e cada artigo tem um autor só.
  - No `Artigo`: `@ManyToOne` + `@JoinColumn(name = "id_autor")` (é aqui que fica a chave estrangeira)
  - No `Autor`: `@OneToMany(mappedBy = "autor")` com a lista de artigos
  - A lista de artigos do `Autor` tem `@JsonIgnore`, senão o JSON entra em loop infinito (Autor -> Artigo -> Autor -> ...)
- Agora pra criar um artigo precisa dizer quem é o autor.

## Endpoints

A URL base é `http://localhost:8000/editora`

**Autores**

- `POST /api/autores` cadastra um autor (201; 409 se o e-mail já existe; 400 se faltar nome ou e-mail)
- `GET /api/autores` lista todos
- `GET /api/autores/{id}` busca um (404 se não existir)
- `GET /api/autores/{id}/artigos` lista os artigos publicados desse autor (por padrão só os publicados; se quiser só os não publicados: `?publicado=false`)
- `DELETE /api/autores/{id}` apaga (204; 404 se não existir; 409 se ele ainda tiver artigos)
- `DELETE http://localhost:8000/editora/api/autores` apaga todos (204; 409 se tiver artigos relacionados a algum autor)

**Artigos**

- `GET /` lista todos
- `GET /?titulo=spring` busca pelo título
- `GET /publicado` só os publicados
- `GET /{id}` busca um (404 se não existir)
- `POST /` (ou `POST /api/artigos`) cria um artigo (201; 404 se o autor não existe; 400 se faltar título, resumo ou autor)
- `PUT /{id}` atualiza
- `DELETE /{id}` apaga um (204)
- `DELETE /` apaga todos (204)


## Exemplos

Cadastrar autor (`POST /editora/api/autores`):

```json
{
  "nome": "Maria Souza",
  "email": "maria@uepg.br",
  "biografia": "Pesquisadora em Engenharia de Software."
}
```

Criar artigo (`POST /editora/`) - o autor vai pelo id:

```json
{
  "titulo": "Novidades no Spring Boot 3",
  "resumo": "Um olhar sobre o novo baseline e o Jakarta EE.",
  "autor": { "id": 1 }
}
```

A resposta já vem com os dados do autor dentro:

```json
{
  "id": 1,
  "titulo": "Novidades no Spring Boot 3",
  "resumo": "Um olhar sobre o novo baseline e o Jakarta EE.",
  "publicado": true,
  "autor": { "id": 1, "nome": "Maria Souza", "email": "maria@uepg.br", "biografia": "Pesquisadora em Engenharia de Software." }
}
```

## Coisas que vale saber

- Não dá pra apagar um autor que ainda tem artigos (devolve 409). Tem que apagar os artigos primeiro.
- O e-mail do autor é salvo em minúsculo, então `Maria@x.com` e `maria@x.com` contam como o mesmo.
- No banco a coluna `id_autor` do artigo aceita nulo, pra não quebrar os artigos antigos que já estavam na tabela. Mas pela API o autor é obrigatório ao criar.
- Pra ver a chave estrangeira no pgAdmin: Databases -> editora -> Schemas -> public -> Tables -> artigo -> Constraints.