# Library Web (Biblioteca API V2)

Esta é uma API para gestão de livros e usuários, desenvolvida como parte de um processo seletivo para a **Emakers Jr.**. Esta versão é a **segunda tentativa** de desenvolvimento de uma API para esse processo seletivo, com melhorias planejadas para adicionar funcionalidades adicionais e melhorar a testabilidade.

A versão anterior dessa API está disponível em [BibliotecaAPI](https://github.com/gabrafo/BibliotecaAPI).

## Funcionalidades planejadas

- **Testagem robusta**: Planejo adicionar uma cobertura de testes abrangente, com testes unitários e de integração.
- **Autenticação com OpenID**: Implementação de login via serviços de terceiros (como Google, GitHub, etc.) utilizando OpenID, permitindo que os usuários façam login de forma simples e segura.
- **Frontend com Thymeleaf**: Criação de um frontend para a aplicação utilizando **Thymeleaf** como template engine, integrado com o backend em Spring Boot.
- **Confirmação de email e recuperação de senha**: Implementação de funcionalidades para confirmação de e-mail durante o cadastro e recuperação de senha utilizando o **Java Mail Sender**.
- **Consumo de APIs externas**: A aplicação irá consumir a API **ViaCEP** para obter informações de endereço a partir de um CEP. Além disso, será consumida uma API de livros (a ser definida) para populamento das entidades `Livro` e `ExemplarDeLivro`.

## Arquitetura

### Estrutura de Entidades

Para ter acesso ao diagrama relacional original, clique [aqui](https://prnt.sc/xtjWkUvJ8Sh-).

A arquitetura de dados foi planejada com base nas seguintes entidades principais:

1. **User**:
   - Observação: Excluí o atributo `CEP` da entidade `Pessoa`, optando por criar uma nova entidade `Address` para lidar com o endereço do usuário.

2. **Address**:
   - A ideia é que a entidade `Address` seja preenchida com os dados retornados pela requisição à API **ViaCEP**, utilizando o código postal como base.

3. **Book**:
   - Representa o livro enquanto obra, sem considerar edições específicas. Assim, contemplará informações mais gerais como gênero textual, autor, etc.

4. **BookUnity**:
   - Representa um exemplar físico de um livro, considerando a editora, o ano de edição, o número de exemplares e outros detalhes específicos.

### Mudanças no Diagrama Relacional

A arquitetura foi planejada com base nas necessidades do sistema, e algumas mudanças foram feitas no diagrama relacional fornecido pela empresa:

- A exclusão do atributo `CEP` da entidade `Pessoa`, substituindo-o por uma relação com a nova entidade `Address`.
- A introdução de uma nova entidade `BookUnity` para representar exemplares individuais de livros, possibilitando a gestão de várias edições do mesmo livro.

## Tecnologias utilizadas (ou a serem utilizadas)

- **Spring Boot**: Framework principal para o backend.
- **Spring Security**: Implementação de segurança, incluindo autenticação via OpenID.
- **Thymeleaf**: Engine de templates para o frontend.
- **Java Mail Sender**: Para envio de e-mails de confirmação e recuperação de senha.
- **APIs externas**: Consumo da API **ViaCEP** para obter informações de endereço, além de uma API de livros (a ser definida).
- **JUnit, Mockito**: Para testes de unidade e integração.

## Como executar o projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/gabrafo/Library-Web.git
   cd Library-Web
   ```

2. Certifique-se de ter o JDK 17 ou superior instalado.

3. Compile o projeto utilizando o Maven:
   ```bash
   mvn clean install
   ```

4. Execute o servidor:
   ```bash
   mvn spring-boot:run
   ```

5. A API estará disponível em `http://localhost:8080`.

## Melhorias planejadas

- **Testes**: Como mencionado, planejo adicionar cobertura de testes robusta.
- **Integração com OpenID**: Será implementado o login com contas de serviços como o Gmail.
- **Frontend**: Desenvolvimento de uma interface de usuário com Thymeleaf.
- **Outras APIs**: Consumo de mais APIs para enriquecer os dados da aplicação.
