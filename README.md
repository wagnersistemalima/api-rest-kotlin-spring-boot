# api-forum-alura

* Adicionar o Spring Data JPA na API, incluindo suas dependências no arquivo pom.xml;
* Configurar o Spring Data JPA via propriedades no arquivo application.yml;
* Mapear as classes de domínio como entidades JPA, utilizando as anotações @Entity, @Id, @ManyToOne e @OneToMany;
* Criar interfaces repository para acesso ao banco de dados, herdando da interface JpaRepository do Spring Data JPA;
* Testar as mudanças na API utilizando o Postman para envio de requisições HTTP.

## Adicionando plugin da jpa e no-arg

```
  <compilerPlugins>
	<plugin>spring</plugin>
	<plugin>jpa</plugin>
	<plugin>no-arg</plugin>
  </compilerPlugins>

```

## Adicionando dependecia noarg

```
<dependency>
	<groupId>org.jetbrains.kotlin</groupId>
	<artifactId>kotlin-maven-noarg</artifactId>
	<version>${kotlin.version}</version>
</dependency>

```

## motivaçoes sobre migrations / Banco de dados da api

* Definir o schema de criação de tabelas
* Como realizar evoluçoes nas tabelas
* Como reverter a alteraçao

## Solução migrations

* Cada mudança e realizada em um script sql (migration)
* Uma tabela no banco de dados registra a execução dos scripts
* Uma ferramenta automatiza o processo
* Utilizando a ferramenta Flyway
* Adicionar dependencia do flyway
```
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>

```

* Com a dependencia do flyway adicionada no pom.xml, o spring boot reconhece a ferramenta e automaticamente
  o hibernate deixa de gerenciar o banco de dados, sendo assim teremos um controle maior
  sobre o esquema do banco

## Organização de pacotes para migrations do banco e scripts

![alter text](/image/db-migration.png)

## Métodos de consultas com filtros nos repositórios da API

* utilizando o padrão de nomenclatura findBy do Spring Data JPA
* findByCursoNome
* Filtrar na tabela Topico, atributo curso, Tabela Curso, atributo nome

```
@Repository
interface TopicoRepositoy: JpaRepository<Topico, Long > {

    fun findByCursoNome(nomeCurso: String, paginacao: Pageable): Page<Topico>
}
```

## Paginação

* Como realizar paginação e ordenação nas consultas ao banco de dados,
  utilizando a interface Pageable do Spring Data JPA
* Recurso pronto no Spring Boot para realizar a paginação
* Pageable (org.springframework.data.domain)
* Classe do propio Spring

```
Pageable
```

## Ordenação

* parâmetros default de paginação e ordenação com a utilização
  da anotação @PageableDefault
* Pageable tem suporte para ordenação
* Exemplo: Default quando não passar parametro na url

```
// ordenando 10 registros por pagina (size = 10)
// pela data de criação, (sort = ["data_criacao"])
// os mais recentes,  (direction = Sort.Direction.DESC)

@GetMapping
    fun listar(
        @RequestParam(required = false) nomeCurso: String?,
        @PageableDefault(size = 10, sort = ["dataCriacao"], direction = Sort.Direction.DESC) paginacao: Pageable

    ): Page<TopicoView> {

        return topicoService.listar(nomeCurso, paginacao)
    }

```
## cache do Spring.

* o Spring Boot tem um módulo exatamente para nós trabalharmos com essa parte de cache.
* Adicionar dependencia no pom.xml

```
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

```

* utilizar um cache em memória mesmo, utilizando um mapa. Só que isso é para ser
  utilizado apenas em ambiente de desenvolvimento.

* Se você for colocar essa aplicação em produção, de fato, além da dependência
  do starter cache, você vai ter que baixar também a dependência da ferramenta que
  você quer utilizar de cache. Como, por exemplo, o Redis, o Memcached, ou qualquer
  ferramenta que você queira utilizar como provedor de cache.

* Para habilitar o uso do cach na aplicação, adicionar a anotação na classe da aplicação:
* @EnableCaching
```
@SpringBootApplication
@EnableCaching
class ForumApplication

fun main(args: Array<String>) {
	runApplication<ForumApplication>(*args)
}


```

* A realizar cache de consultas no banco de dados utilizando a anotação
  @Cacheable em métodos dos controllers
* @Cacheable(value = ["topicos"]) no metodo que quiser guadar em memoria o retorno
* Exemplo na classe Controler, no end point "/topicos"


````
    @GetMapping
    @Cacheable(value = ["topicos"])
    fun listar(
        @RequestParam(required = false) nomeCurso: String?,
        @PageableDefault(size = 10, sort = ["dataCriacao"], direction = Sort.Direction.DESC) paginacao: Pageable

    )

````

## Limpar o cash

* Usar a anotação @CacheEvict(value = ["topicos"])
* Limpar todos os registros que estão no cash sar (allEntries = true)

```
    @PostMapping
    @CacheEvict(value = ["topicos"], allEntries = true)
    fun cadastrar(@RequestBody @Valid form: NovoTopicoForm)
```

## Exemplo adicionando pom.xml outro banco de dados

* No entanto, é totalmente possível utilizar outros bancos de dados na API,
* como o MySQL, PostgreSQL, dentre outros, bastando para isso substituir no
* pom.xml a dependência do H2 e as configurações no arquivo application.yml
* MySQL

```
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>

```

* Configuraçao do application.yml

```
spring:
  datasource:
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost/forum
    username: root
    password: root
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect

```

