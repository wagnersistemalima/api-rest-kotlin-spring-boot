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
* @RequestParam(required = false)  -> esse parametro pode ser opcional, se nao vinher trazer toda a lista

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

## Testes unidade

* O teste de unidade tem como característica testar realmente a menor parte do nosso sistema, seja uma classe ou até mesmo um método onde podemos validar, por exemplo, uma regra de negócio.

* utilizando o MockK, que é uma ferramenta aqui do Kotlin que vem ganhando bastante espaço no mercado.

* utilizar tambem a biblioteca AasertJ

* MockK para mockar recursos, como um repositório. Já o AssertJ para fazer asserções.

[AasertJ github](https://joel-costigliola.github.io/assertj/)


## testes de container

[testcontainers](https://www.testcontainers.org/)

* Testcontainers é uma biblioteca Java que oferece suporte a testes JUnit, fornecendo instâncias leves e descartáveis ​​de bancos de dados comuns, navegadores da Web Selenium ou qualquer outra coisa que possa ser executada em um contêiner do Docker.

* pre requisitos: Dependências do Maven

```
<properties>
   <testcontainers.version>1.17.3</testcontainers.version>
<properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>testcontainers-bom</artifactId>
            <version>${testcontainers.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>


```

* dependencia mysql

```
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mysql</artifactId>
    <scope>test</scope>
</dependency>


```

* dependencia junit-jupter

```

<dependency>
	<groupId>org.testcontainers</groupId>
	<artifactId>junit-jupiter</artifactId>
	<scope>test</scope>
</dependency>

```

## Testes de integração

* testar a integração da nossa aplicação com o banco de dados por exemplo
* teste de containers é baseado na ideia de: ao invés de eu fazer um teste integração da minha aplicação com a base de produção, com a base de homologação ou com a base mesmo de dev, e dessa forma eu sujar, digamos assim, com os dados do teste essas bases, eu tenho uma ferramenta que em tempo de execução dos meus testes vai subir um container, vai aplicar as migrações, eu vou fazer os testes dessa integração e depois esse container não vai existir mais.
* adicionar dependencia no pom.xml do spring security test

```

<!-- https://mvnrepository.com/artifact/org.springframework.security/spring-security-test -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>


```




## Testes de Api

* testes de API utilizando o mock mvc
* é um recurso do próprio Spring que nos fornece aqui uma forma de testar as nossas APIs. Dessa maneira, conseguimos testar se nossa API não está aceitando parâmetros errados, que o recurso da nossa aplicação está fazendo a chamada correta para o método correto, ou seja, através de uma entrada a saída que eu espero tem que ser a de tópicos por exemplo.


## Documentado Api

* Utilizar o swagger, que é uma ferramenta bastante utilizada também no mercado de documentação de API para documentar as nossas APIs de tópico.
* Biblioteca Springdoc OpenAPI UI
* Utilizar a interface gráfica do Swagger para realizar requisições para APIs. Para isso é necessário inserir a dependência no pom.xml da aplicação. Dessa maneira, já serão reconhecidos os endpoints da aplicação;
* http://www.localhost:8080/swagger-ui/index.html

```
<!-- https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.6.9</version>
</dependency>

```

* Configuração do Swagger para fazer requisições via interface utilizando um token JWT.
* @SecurityRequirement(name = "bearerAuth") na classe TopicoController
```
@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
class SwaggerConfiguration {
}
```

![swagger](/image/swagger.png)