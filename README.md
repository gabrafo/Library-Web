# Library Web (Biblioteca API V2)

Esta é uma API para gestão de livros e usuários, desenvolvida como parte de um processo seletivo para a **Emakers Jr.**. Esta versão é a **segunda tentativa** de desenvolvimento de uma API para esse processo seletivo, com melhorias planejadas para adicionar funcionalidades adicionais e melhorar a testabilidade.

A versão anterior dessa API está disponível em [BibliotecaAPI](https://github.com/gabrafo/BibliotecaAPI).

## Arquitetura

### Estrutura de Entidades

Para ter acesso ao diagrama relacional original, clique [aqui](https://prnt.sc/xtjWkUvJ8Sh-).

A arquitetura de dados foi planejada com base nas seguintes entidades principais:

1. **User**:
   - Observação: Excluí o atributo `CEP` da entidade `Pessoa`, optando por criar uma nova entidade `Address` para lidar com o endereço do usuário.

2. **Address**:
   - A ideia é que a entidade `Address` seja preenchida com os dados retornados pela requisição à API **ViaCEP**, utilizando o código postal como base.

3. **Book**:
   - Representa o livro, contemplando informações como título, quantidade de exemplares disponíveis, autor, etc.

### Mudanças no Diagrama Relacional

A arquitetura foi planejada com base nas necessidades do sistema, e algumas mudanças foram feitas no diagrama relacional fornecido pela empresa:

- A exclusão do atributo `CEP` da entidade `Pessoa`, substituindo-o por uma relação com a nova entidade `Address`.

## Tecnologias utilizadas

- **Spring Boot**: Framework principal para o backend.
- **Spring Security, OAuth2 e JWT**: Implementação de segurança, incluindo autenticação via OpenID.
- **Java Mail Sender**: Para envio de e-mails de confirmação e recuperação de senha.
- **Open Feign**: Consumo da API **ViaCEP** para obter informações de endereço.
- **Swagger**: Documentação dos endpoints da API.
- **JUnit, Mockito**: Para testes de unidade e integração.
- **TypeScript, HTML, CSS e React**: Para o frontend (que, atualmente, conta com telas de cadastro, login, perfil de usuário e empréstimo de livros).

## Como executar o projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/gabrafo/Library-Web.git
   cd Library-Web
   ```

### Backend

1. Navegue até a subpasta "backend":
   ```bash
   cd backend
   ```

2. Compile o projeto utilizando o Maven:
   ```bash
   mvn clean install
   ```

3. Execute o Docker Compose.
   ```bash
   docker-compose build
   docker-compose up
   ```

4. A API estará disponível em `http://localhost:8080`, e a documentação em `http://localhost:8080/swagger-ui/index.html#/`.

---

### Frontend

1. Navegue até a subpasta "frontend":
   ```bash
   cd backend
   ```

2. Instale as dependências do projeto usando NPM:
   ```bash
   npm install
   ```

3. Execute o projeto:
   ```bash
   npm run dev
   ```

## Melhorias planejadas

- **Testes**: Como mencionado, planejo melhor ainda mais a cobertura de testes, para que seja mais robusta.
