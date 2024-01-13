# Product Management API

Este é o projeto da API Rest para gerenciamento de produtos. Abaixo estão os passos para instalação e configuração.

- Acesse a documentação da API em Produção [https://product-management-production-0f65.up.railway.app/swagger-ui/index.html#/](https://product-management-production-0f65.up.railway.app/swagger-ui/index.html#/).

**Importante:**

Não é necessário rodar scripts SQL de forma manual. A aplicação está fazendo o controle do banco de dados através do Flyway, e ao subir a aplicação já está ocorrendo o insert dos Usuários e das Categorias.

## Autenticação JWT:

- A autenticação na API é feita usando JSON Web Token (JWT). Certifique-se de incluir o token JWT nas solicitações autenticadas.

**Dados para login:**

**Administrador:** 

email: adm@email.com

senha: senha123

**Estoquista:**

email: estoquista@email.com

senha: senha123

**Exemplo:**

```json
{
  "email": "estoquista@email.com",
  "senha": "senha123"
}
```

**Importante:**

Um arquivo com a collection está disponível em: `src/main/resources/Product.postman_collection.json`

Caso haja dúvida na montagem das requests, apenas importe este arquivo no Postaman.

## Requisitos

- Java JDK 17 ou superior
- Maven 3.6.0 ou superior
- MySQL Server
- IDE de sua escolha (recomendado: IntelliJ IDEA, Eclipse)

## Instalação

1. **Clonar o Repositório:**
   ```bash
   git clone https://github.com/Mateus-msj3/product-management.git

## Importar no IDE:

- Abra sua IDE e importe o projeto como um projeto Maven existente.

## Configurar o Banco de Dados:

- Crie um banco de dados MySQL chamado `db_productmanagement`.
- Configure as credenciais do banco de dados no arquivo `src/main/resources/application.properties`.

## Executar a Aplicação:

- Execute a classe principal `ProductManagementApplication` para iniciar a aplicação.

## Acessar a API:

- A API estará disponível em [http://localhost:8080](http://localhost:8080).

## Documentação da API (Swagger):

- Acesse a documentação da API em [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

## Autenticação JWT:

- A autenticação na API é feita usando JSON Web Token (JWT). Certifique-se de incluir o token JWT nas solicitações autenticadas.

## Dependências Principais

- [Spring Boot](https://spring.io/projects/spring-boot) - Framework para desenvolvimento de aplicações Java.
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - Facilita a implementação de camadas de persistência.
- [Spring Security](https://spring.io/projects/spring-security) - Fornece recursos de segurança para aplicações Spring.
- [Lombok](https://projectlombok.org/) - Simplifica a escrita de código Java eliminando a necessidade de escrever boilerplate.
- [ModelMapper](http://modelmapper.org/) - Facilita a transferência de dados entre objetos.
- [MySQL Connector](https://dev.mysql.com/downloads/connector/j/) - Driver JDBC para MySQL.
- [Apache POI](https://poi.apache.org/) - Biblioteca para manipulação de arquivos do Microsoft Office.
- [Flyway](https://flywaydb.org/) - Controle de versionamento de banco de dados.
- [Springdoc OpenAPI](https://springdoc.org/) - Geração automática de documentação OpenAPI.

Para informações detalhadas sobre outras dependências, consulte o arquivo `pom.xml`.

## Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir problemas ou enviar pull requests.
