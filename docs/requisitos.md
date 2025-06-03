# Requisitos do Sistema de Reservas de Hotéis "123milhas"

## Requisitos Funcionais

### RF01 - Gerenciamento de Usuários
- RF01.1: O sistema deve permitir o cadastro de novos usuários com informações pessoais (nome completo, e-mail, senha, telefone, documento, data de nascimento).
- RF01.2: O sistema deve permitir a autenticação de usuários através de e-mail e senha.
- RF01.3: O sistema deve permitir a recuperação de senha através do e-mail cadastrado.
- RF01.4: O sistema deve permitir que usuários visualizem e atualizem seus dados pessoais.
- RF01.5: O sistema deve implementar diferentes níveis de acesso (usuário comum e administrador).

### RF02 - Gerenciamento de Hotéis
- RF02.1: O sistema deve permitir o cadastro de hotéis com informações detalhadas (nome, descrição, classificação, endereço, contato).
- RF02.2: O sistema deve permitir o cadastro de comodidades para cada hotel.
- RF02.3: O sistema deve permitir o upload de imagens para cada hotel.
- RF02.4: O sistema deve permitir a atualização e exclusão lógica de hotéis.
- RF02.5: O sistema deve calcular e exibir a classificação média de cada hotel com base nas avaliações dos usuários.

### RF03 - Gerenciamento de Quartos
- RF03.1: O sistema deve permitir o cadastro de diferentes tipos de quartos para cada hotel.
- RF03.2: O sistema deve registrar informações detalhadas sobre cada quarto (número, tipo, capacidade, preço, comodidades).
- RF03.3: O sistema deve permitir o upload de imagens para cada tipo de quarto.
- RF03.4: O sistema deve permitir a definição de preços diferenciados por temporada ou datas específicas.
- RF03.5: O sistema deve controlar a disponibilidade de quartos por data.

### RF04 - Sistema de Reservas
- RF04.1: O sistema deve permitir a busca de hotéis disponíveis por localização e datas.
- RF04.2: O sistema deve permitir a aplicação de filtros na busca (preço, classificação, comodidades).
- RF04.3: O sistema deve verificar a disponibilidade de quartos em tempo real.
- RF04.4: O sistema deve calcular o preço total da reserva com base nas datas selecionadas e tipo de quarto.
- RF04.5: O sistema deve permitir a realização de reservas por usuários autenticados.
- RF04.6: O sistema deve enviar confirmação da reserva por e-mail.
- RF04.7: O sistema deve permitir o cancelamento de reservas dentro de um prazo estabelecido.
- RF04.8: O sistema deve permitir a visualização do histórico de reservas do usuário.

### RF05 - Sistema de Pagamentos
- RF05.1: O sistema deve suportar múltiplos métodos de pagamento (cartão de crédito, débito, transferência).
- RF05.2: O sistema deve processar pagamentos de forma segura.
- RF05.3: O sistema deve emitir comprovantes de pagamento.
- RF05.4: O sistema deve processar reembolsos em caso de cancelamento dentro do prazo.

### RF06 - Sistema de Avaliações
- RF06.1: O sistema deve permitir que usuários avaliem hotéis após a estadia.
- RF06.2: O sistema deve permitir a atribuição de notas e comentários nas avaliações.
- RF06.3: O sistema deve exibir as avaliações de cada hotel para outros usuários.
- RF06.4: O sistema deve permitir a moderação de avaliações por administradores.

## Requisitos Não Funcionais

### RNF01 - Usabilidade
- RNF01.1: A interface do sistema deve ser intuitiva e de fácil navegação.
- RNF01.2: O sistema deve ser responsivo, adaptando-se a diferentes tamanhos de tela.
- RNF01.3: O sistema deve fornecer feedback claro sobre ações realizadas pelo usuário.

### RNF02 - Desempenho
- RNF02.1: O sistema deve responder a consultas de disponibilidade em menos de 3 segundos.
- RNF02.2: O sistema deve suportar pelo menos 1000 usuários simultâneos.
- RNF02.3: O tempo de carregamento das páginas não deve exceder 5 segundos.

### RNF03 - Segurança
- RNF03.1: As senhas dos usuários devem ser armazenadas de forma criptografada.
- RNF03.2: O sistema deve utilizar HTTPS para todas as comunicações.
- RNF03.3: O sistema deve implementar proteção contra ataques comuns (SQL Injection, XSS, CSRF).
- RNF03.4: O sistema deve realizar validação de dados em todas as entradas.
- RNF03.5: O sistema deve implementar controle de acesso baseado em papéis.

### RNF04 - Disponibilidade
- RNF04.1: O sistema deve estar disponível 24/7, com tempo de inatividade planejado não superior a 2 horas por mês.
- RNF04.2: O sistema deve implementar mecanismos de backup diários.

### RNF05 - Escalabilidade
- RNF05.1: O sistema deve ser projetado para permitir escalabilidade horizontal.
- RNF05.2: O sistema deve utilizar cache para otimizar consultas frequentes.

### RNF06 - Manutenibilidade
- RNF06.1: O código deve seguir padrões de desenvolvimento e boas práticas.
- RNF06.2: O sistema deve ser modular, facilitando a manutenção e evolução.
- RNF06.3: O sistema deve incluir documentação técnica adequada.

### RNF07 - Compatibilidade
- RNF07.1: O sistema deve funcionar nos principais navegadores (Chrome, Firefox, Safari, Edge).
- RNF07.2: O sistema deve ser compatível com dispositivos móveis (iOS e Android).
