# Documentação do Sistema de Reservas de Hotéis - 123milhas

## Visão Geral

O sistema de reservas de hotéis 123milhas é uma aplicação web completa desenvolvida com Spring Boot, que permite aos usuários buscar, visualizar e reservar hotéis. O sistema inclui funcionalidades para gerenciamento de hotéis, quartos, reservas, pagamentos e avaliações, além de um painel administrativo para gerenciamento completo da plataforma.

## Arquitetura do Sistema

O sistema foi desenvolvido seguindo uma arquitetura em camadas:

1. **Camada de Apresentação**: Interfaces HTML/CSS/JavaScript com Thymeleaf
2. **Camada de Controle**: Controladores REST Spring MVC
3. **Camada de Serviço**: Serviços com regras de negócio
4. **Camada de Persistência**: Repositórios Spring Data JPA
5. **Camada de Modelo**: Entidades JPA

## Tecnologias Utilizadas

- **Backend**: Java 17, Spring Boot 3.3.12
- **Frontend**: HTML5, CSS3, JavaScript, Thymeleaf
- **Persistência**: Spring Data JPA, Hibernate, PostgreSQL
- **Segurança**: Spring Security
- **Validação**: Spring Validation
- **Desenvolvimento**: Spring Boot DevTools, Lombok

## Estrutura do Projeto

```
projeto_java/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/trabalhofinal/fabrica_software/
│   │   │       ├── controller/    # Controladores REST
│   │   │       ├── enums/         # Enumerações
│   │   │       ├── model/         # Entidades JPA
│   │   │       ├── repository/    # Repositórios Spring Data JPA
│   │   │       ├── service/       # Serviços com regras de negócio
│   │   │       └── FabricaSoftwareApplication.java  # Classe principal
│   │   └── resources/
│   │       ├── static/            # Recursos estáticos (CSS, JS, imagens)
│   │       ├── templates/         # Templates Thymeleaf
│   │       └── application.properties  # Configurações da aplicação
│   └── test/                      # Testes unitários e de integração
└── docs/                          # Documentação do projeto
    └── diagrams/                  # Diagramas UML
```

## Modelo de Dados

O sistema é composto pelas seguintes entidades principais:

1. **User**: Representa os usuários do sistema (clientes e administradores)
2. **Role**: Define os papéis/permissões dos usuários
3. **Hotel**: Representa os hotéis disponíveis para reserva
4. **Room**: Representa os quartos disponíveis em cada hotel
5. **RoomAvailability**: Controla a disponibilidade dos quartos por data
6. **Reservation**: Representa as reservas feitas pelos usuários
7. **Payment**: Registra os pagamentos das reservas
8. **Review**: Armazena as avaliações dos usuários sobre os hotéis
9. **Address**: Armazena informações de endereço para hotéis e usuários

## Funcionalidades Principais

### Para Usuários (Clientes)

1. **Busca de Hotéis**
   - Busca por localização, datas e número de hóspedes
   - Filtros por preço, classificação e comodidades
   - Ordenação por relevância, preço ou avaliação

2. **Visualização de Hotéis**
   - Detalhes do hotel (descrição, fotos, localização)
   - Informações sobre quartos disponíveis
   - Avaliações de outros usuários
   - Mapa com localização do hotel

3. **Reservas**
   - Seleção de datas e tipo de quarto
   - Cálculo automático do preço total
   - Formulário para informações do hóspede
   - Opções de pagamento (cartão de crédito, PIX, transferência)

4. **Gerenciamento de Conta**
   - Histórico de reservas
   - Cancelamento de reservas
   - Avaliação de hotéis visitados
   - Hotéis favoritos
   - Atualização de dados pessoais

### Para Administradores

1. **Dashboard**
   - Visão geral das métricas do sistema
   - Gráficos de reservas e receita
   - Listagem de reservas recentes
   - Hotéis mais reservados

2. **Gerenciamento de Hotéis**
   - Cadastro e edição de hotéis
   - Gerenciamento de quartos e disponibilidade
   - Definição de preços e promoções

3. **Gerenciamento de Reservas**
   - Visualização e edição de reservas
   - Confirmação e cancelamento de reservas
   - Processamento de pagamentos

4. **Gerenciamento de Usuários**
   - Cadastro e edição de usuários
   - Atribuição de papéis/permissões
   - Bloqueio/desbloqueio de contas

5. **Relatórios**
   - Relatórios de ocupação
   - Relatórios financeiros
   - Análise de avaliações

## Fluxos Principais

### Fluxo de Reserva

1. Usuário busca hotéis por localização e datas
2. Sistema exibe lista de hotéis disponíveis
3. Usuário seleciona um hotel para ver detalhes
4. Usuário escolhe um quarto disponível e clica em "Reservar"
5. Usuário preenche formulário com dados da reserva
6. Usuário seleciona método de pagamento e fornece dados
7. Sistema processa o pagamento e confirma a reserva
8. Usuário recebe confirmação da reserva por e-mail

### Fluxo de Avaliação

1. Usuário acessa seu histórico de reservas
2. Usuário seleciona uma reserva concluída
3. Usuário clica em "Avaliar Hotel"
4. Usuário fornece nota e comentário sobre sua experiência
5. Sistema registra a avaliação e atualiza a média do hotel

## Configuração do Banco de Dados

O sistema utiliza PostgreSQL como banco de dados relacional. As configurações de conexão estão no arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/123milhas
spring.datasource.username=postgres
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## Segurança

O sistema utiliza Spring Security para autenticação e autorização:

- Autenticação baseada em formulário para usuários
- Controle de acesso baseado em papéis (ROLE_USER, ROLE_ADMIN)
- Proteção contra ataques CSRF
- Senhas armazenadas com algoritmo de hash seguro (BCrypt)

## Validação

Todas as entradas de usuário são validadas usando Spring Validation:

- Validação de campos obrigatórios
- Validação de formato (e-mail, telefone, etc.)
- Validação de regras de negócio (datas de check-in/check-out, disponibilidade, etc.)

## Interfaces do Usuário

O sistema possui as seguintes telas principais:

1. **Página Inicial**: Apresentação do site e formulário de busca
2. **Busca de Hotéis**: Listagem de hotéis com filtros e ordenação
3. **Detalhes do Hotel**: Informações detalhadas sobre o hotel e quartos
4. **Formulário de Reserva**: Dados do hóspede e detalhes da estadia
5. **Pagamento**: Seleção de método e processamento do pagamento
6. **Dashboard do Usuário**: Gerenciamento de conta e reservas
7. **Painel Administrativo**: Gerenciamento completo do sistema

## Executando o Sistema

### Requisitos

- Java 17 ou superior
- PostgreSQL 12 ou superior
- Maven 3.6 ou superior

### Passos para Execução

1. Clone o repositório
2. Configure o banco de dados PostgreSQL
3. Atualize as configurações em `application.properties`
4. Execute o comando: `mvn spring-boot:run`
5. Acesse a aplicação em: `http://localhost:8080`

## Considerações Finais

Este sistema foi desenvolvido como parte do trabalho final da disciplina de Fábrica de Software. Ele implementa um conjunto completo de funcionalidades para um sistema de reservas de hotéis, seguindo boas práticas de desenvolvimento e utilizando tecnologias modernas.

Para melhorias futuras, pode-se considerar:

- Implementação de um sistema de notificações em tempo real
- Integração com APIs de pagamento reais
- Implementação de um sistema de fidelidade
- Otimização para dispositivos móveis com Progressive Web App (PWA)
- Implementação de recursos de acessibilidade
