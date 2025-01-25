# **Desafio Técnico - API de Transações**
#### Um desafio do banco Itaú o link do desafio está aqui : https://github.com/rafaellins-itau/desafio-itau-vaga-99-junior
[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)


- Este projeto é uma API desenvolvida para gerenciar transações financeiras. Ela permite realizar operações de cadastro, listagem, exclusão e obtenção de estatísticas sobre transações. A API foi construída utilizando Spring Boot e Spring Data JPA para persistência de dados, utilizando H2 database com banco de dados em memória.

- ## Esse README fornece uma visão geral da aplicação feita por mim , como testar cada endpoint no Postman e como rodar os testes unitários.

## 🚀 Endpoints Disponíveis
# 
### **1. Cadastrar Transação**
- **URL:**
```
http://localhost:8080/transacoes
```
- **Método: POST**
- Descrição: Cadastra uma nova transação na base de dados
- Corpo da Requisição (JSON):
```
[
    {
        "valor": 100.50,
        "dataHora": "2025-01-23T20:00:00+00:00"
    },
    {
        "valor": 120.50,
        "dataHora": "2025-01-23T20:00:00+00:00"
    },
    {
        "valor": 123.50,
        "dataHora": "2025-01-23T20:00:00+00:00"
    }
]
```
- **Resposta:**
    - Status 201 (Created) - Se a transação foi criada com sucesso.
    - Status 422 (Unprocessable Entity) - Caso a transação seja inválida.
- **Exemplo de Resposta (Status 201):**
```
{
    "message": "Transações criadas com sucesso!",
    "status": "201 Created"
}
```
# 
### **2. Listar Transações**
- **URL:**
```
http://localhost:8080/transacoes
```
- **Método: GET**
- Descrição: Lista todas as transações cadastradas.
- Resposta:
    - Status 200 (OK) - Se as transações foram recuperadas com sucesso.
    - Status 404 (Not Found) - Caso não existam transações registradas.
- Exemplo de Resposta (Status 200):
```
[
    {
        "mensagem": "Não há transações cadastradas.",
        "transacoes": []
    }
]
```
# 
### **3. Excluir Transação**
- **URL:**
```
http://localhost:8080/transacoes/{id}
```
- **Método: DELETE**
- Descrição: Exclui uma transação com base no ID.
- Parâmetro: id - ID da transação a ser excluída.
- Resposta:
    - Status 200 (OK) - Se a transação foi excluída com sucesso.
    - Status 404 (Not Found) - Caso a transação não exista.
    - Status 422 (Unprocessable Entity) - Se o ID não for válido.
# 
### **4. Obter Estatísticas de Transações**
- **URL:**
```
http://localhost:8080/transacoes/estatisticas
```
- **Método: GET**
- Descrição: Retorna estatísticas sobre as transações, incluindo contagem, soma, média, valor mínimo e máximo.
- Resposta:
    - Status 200 (OK) - Se as estatísticas foram calculadas com sucesso.
    - Status 404 (Not Found) - Caso não existam transações recentes para calcular as estatísticas.
- Exemplo de Resposta (Status 200):
```
{
    "count": 0,
    "sum": 0.0,
    "avg": 0.0,
    "min": "Infinity",
    "max": "-Infinity"
}
```
- Quando o endpoint GET /transacoes/estatisticas retorna um JSON com os valores Infinity para o campo min e max, isso pode ocorrer devido à falta de transações dentro do intervalo de tempo desejado, ou seja, o filtro para selecionar as transações pode estar retornando uma lista vazia, mas ainda assim, o método summaryStatistics() é chamado e gera o valor de infinito para o mínimo e máximo.
# 
- **Explicação do Erro:**
- O método DoubleSummaryStatistics do Java, ao ser utilizado para calcular estatísticas sobre uma lista de números, tem comportamentos específicos quando não há elementos válidos para processar:
    - count: Se não houver transações dentro do intervalo de tempo especificado, o contador de transações será zero (0).
    - sum: Como não há transações, a soma será 0.0.
    - avg: A média será 0.0, pois não há valores para calcular a média.
    - min e max: Como não há elementos para comparar, os valores mínimos e máximos são definidos como Infinity e -Infinity, respectivamente. Esses são valores padrão para o caso de não haver dados disponíveis para essas operações.
# 
- **Solução:**
- O problema ocorre porque você está usando o método summaryStatistics(), que não lida bem com o caso de uma lista vazia. Para evitar esses valores infinitos, você pode modificar sua lógica para que, quando não houver transações, o retorno de min e max seja um valor padrão (como null ou um número como 0).
- Uma possível solução seria ajustar o comportamento de estatísticas com base na lista de transações antes de chamar summaryStatistics(). Por exemplo, verificando se a lista está vazia e, em caso afirmativo, retornando um objeto com valores padrão.
- **Alterando o código para evitar Infinity:**
```
public DoubleSummaryStatistics calcularEstatisticas() {
    OffsetDateTime umMinutoAtras = OffsetDateTime.now().minusSeconds(60);

    List<Transacao> ultimasTransacoes = repository.buscarTodas().stream()
            .filter(t -> t.getDataHora().isAfter(umMinutoAtras.toOffsetTime()))
            .collect(Collectors.toList());

    if (ultimasTransacoes.isEmpty()) {
    // Retorna um DoubleSummaryStatistics com valores nulos ou padrão se não houver transações
        return new DoubleSummaryStatistics();
    }

    return ultimasTransacoes.stream().mapToDouble(Transacao::getValor)
            .summaryStatistics();
}

```
- Se precisar garantir que os valores de min e max não apareçam como Infinity no seu JSON, pode criar uma lógica adicional para retornar 0.0 ou null quando as transações não existirem.
```
public EstatisticaDTO obterEstatisticas() {
    DoubleSummaryStatistics stats = service.calcularEstatisticas();
    
    // Se a estatística estiver vazia, definir valores padrão
    if (stats.getCount() == 0) {
        return new EstatisticaDTO(0, 0.0, 0.0, 0.0, 0.0);
    }

    return new EstatisticaDTO(
            stats.getCount(),
            stats.getSum(),
            stats.getAverage(),
            stats.getMin(),
            stats.getMax()
    );
}
```
- Com essa alteração, em vez de Infinity, os valores para mínimo e máximo serão retornados como 0.0 ou o valor padrão que você definir.
Alternativa com valores padrão:
# 
## 💻 Tecnologias Utilizadas

- **Spring Boot - Framework para criação da aplicação RESTful.**
- **Spring Data JPA - Para persistência de dados no banco de dados.**
- **H2 Database - Para teste com integração ao banco de dados em memória**

# 
## 💻 Ferramentas Utilizadas
- ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
- ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)


```
📁 estrutura do projeto:
transacao-api/
├── pom.xml
└── src/main/java/com/example/transacaoapi
    ├── TransacaoApiApplication.java
    ├── controller/
    │   └── TransacaoController.java
    ├── model/
    │   └── Transacao.java
    ├── repository/
    │   └── TransacaoRepository.java
    └── service/
        └── TransacaoService.java
```
# 
## 🎯 Objetivo do Projeto:
- O Desafio Técnico - Vaga Jr. Itau tem como objetivo a construção de uma API RESTful para gerenciar transações financeiras. A aplicação permite realizar operações de criação e listagem de transações, bem como calcular e retornar estatísticas sobre as transações em um intervalo de tempo específico.

## 🎯 Objetivo do Desafio:
- O objetivo deste desafio é criar uma solução simples e eficaz para o gerenciamento de transações financeiras, proporcionando:
    - Armazenamento eficiente das transações.
    - Cálculos de estatísticas para análise rápida.
    - Manipulação e validação de dados de forma robusta.
    - Além disso, a aplicação tem como foco ser simples, escalável e facilmente testável, com a implementação de endpoints que possibilitem a criação, listagem e remoção de transações.
# 
# **Obrigado! 😎😁**
