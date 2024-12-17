# UdineTour: Inovando no Turismo com Inteligência Artificial e Aprendizado de Máquina

---

![Java](https://img.shields.io/badge/Java-21%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.5-green)

## Visão Geral
Este repositório contém o código referente ao desenvolvimento do back-end do Trabalho de Conclusão de Curso intitulado _**"UdineTour: Inovando no Turismo com Inteligência Artificial e Aprendizado de Máquina"**_, de autoria de Jardel Nunes Cruz, orientado por Felipe Augusto Lima Reis, no Instituto de Ciências Exatas e Informática da Pontifícia Universidade Católica de Minas Gerais (PUC Minas).
## Resumo
Este trabalho apresenta o UdineTour, um guia turístico inteligente que utiliza Aprendizado de Máquina e Processamento de Linguagem Natural (PLN) para aprimorar a experiência do usuário no turismo. O sistema integra dados de diversas fontes e utiliza modelos de linguagem avançados (LLMs) para responder perguntas relacionadas a pontos turísticos, restaurantes e hospedagens, com base na localização do usuário.
O UdineTour foi desenvolvido a partir de uma melhoria na arquitetura Violet, que utiliza modelos de linguagem avançados, possibilitando interações naturais em múltiplos idiomas, compreensão de contexto e respostas a perguntas complexas. Com uma arquitetura baseada em microsserviços e a integração com a API do Google Maps, o \nomeAplicacao\ garante escalabilidade, desempenho e  respostas contextualmente relevantes. Os resultados esperados incluem a personalização da experiência turística, autonomia, acessibilidade e sustentabilidade, contribuindo para inovação no turismo inteligente.

--------------------------------------------------------------------------------
### ⚙ Configurar ambiente de desenvolvimento

1. Instalar java 17+;  Desconsidere caso já tenha a versão mais recente instalada.
   [Link oficial](https://www.oracle.com/br/java/technologies/downloads/)
2. Instalar e configurar o maven; [Link oficial](https://maven.apache.org/download.cgi)
3. Clone o projeto com o comando em ambiente local com o comando abaixo com `cmd, powershell...`
    ```shell  
    git clone https://github.com/jarnunes/tcc-backend-udine
    ```
6. Abra o diretório do projeto em um terminal `cmd, powershell etc` e rode o comando ``mvn clean package`` para gerar o build do projeto;
7. Ainda no diretório do projeto, execute o comando ``java -jar .\target\udine.jar`` para iniciar a aplicação;
8. Para acesso à documentação dos endpoints, acesse URL [http://localhost:5000/api/documentation/endpoints](http://localhost:5000/api/documentation/endpoints)

--------------------------------------------------------------------------------

### 🛠 Tecnologias

- [Java 21+](https://www.oracle.com/br/java/technologies/downloads/)
- [Spring Boot](https://spring.io/projects/spring-boot)
