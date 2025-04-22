# 🔗 HubSpot Integration v2

Este projeto em **Java 17** com **Spring Boot 3.1** integra sua aplicação ao HubSpot, fornecendo:

- 🔐 Autenticação OAuth 2.0 (authorization code flow)  
- 💾 Armazenamento de tokens em banco H2 via JPA  
- 📇 Criação de contatos usando `WebClient`  
- 🌐 Recebimento de webhooks para eventos de contato  
- ✅ Validação de dados e tratamento global de erros  
- 📖 Documentação interativa via Swagger/OpenAPI  

---

## 🚀 **Tecnologias Utilizadas**

- **Spring WebFlux (WebClient)** → Chamadas HTTP reativas e fácil tratamento de timeouts e retries manual  
- **Spring Data JPA + H2** → Persistência leve de tokens (`access_token`, `refresh_token`, `expiresAt`)  
- **Jakarta Validation** (`@NotBlank`, `@Email`) → Garante integridade dos dados antes de enviar ao HubSpot  
- **Springdoc OpenAPI** → Geração automática de documentação e UI Swagger  
- **Lombok** → Redução de boilerplate com `@Data`, `@Slf4j`, `@RequiredArgsConstructor`  

---

## 📌 **Pré-requisitos**

- **Java 17** instalado  
- **Maven 3.6+**  
- Conta de desenvolvedor no HubSpot com **Client ID** e **Client Secret**  

---

## ⚙️ **Configuração e Instalação**

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/hubspot-integration-v2.git
   cd hubspot-integration-v2


## Atualize src/main/resources/application.yml com suas credenciais:
```bash
hubspot:
  client-id: YOUR_CLIENT_ID
  client-secret: YOUR_CLIENT_SECRET
  redirect-uri: http://localhost:8080/api/hubspot/oauth/callback

spring:
  security:
    user:
      name: apiuser
      password: strongpassword123
```
## Contrua e execute
```bash
mvn clean package
mvn spring-boot:run
```
## Abra o Swagger UI em
```bash
http://localhost:8080/swagger-ui.html
```

## Endpoints:

```bash
Endpoint | Método | Descrição
GET  /api/hubspot/oauth/authorize-url | 200 | Retorna URL para iniciar autorização OAuth
GET  /api/hubspot/oauth/callback?code=… | 200 | Troca código por token e persiste no banco
POST /api/hubspot/contacts | 200 | Cria um novo contato no HubSpot
POST /api/hubspot/webhooks/contact-creation | 200 | Recebe notificações de criação de contato
```

## 🔎 **Testando a API **

### **1️⃣ Gerar URL de autorização**
```bash
curl -u apiuser:strongpassword123 \
     -X GET http://localhost:8080/api/hubspot/oauth/authorize-url
```

### **2️⃣ Processar callback e salvar tokens**
```bash
curl -u apiuser:strongpassword123 \
     -G http://localhost:8080/api/hubspot/oauth/callback \
     --data-urlencode "code=SEU_CODIGO"
```

### **3️⃣ Criar contato no HubSpot**
```bash
curl -u apiuser:strongpassword123 \
     -X POST http://localhost:8080/api/hubspot/contacts \
     -H "Content-Type: application/json" \
     -d '{
           "email": "joao.silva@exemplo.com",
           "firstname": "João",
           "lastname": "Silva"
         }'

```
### **4️⃣ Simular webhook de criação de contato **
```bash
curl -u apiuser:strongpassword123 \
     -X POST http://localhost:8080/api/hubspot/webhooks/contact-creation \
     -H "Content-Type: application/json" \
     -d '{
           "eventId": "evt_12345",
           "subscriptionType": "contact.creation",
           "objectId": 123456
         }'

```

### **5️⃣ Testar erro de validação**
```bash
curl -u apiuser:strongpassword123 \
     -X POST http://localhost:8080/api/hubspot/contacts \
     -H "Content-Type: application/json" \
     -d '{ "email": "invalido", "firstname": "", "lastname": "" }'

```

## 🧠 **Decisões Técnicas e Justificativas**

### **✅ Uso do WebClient (Spring WebFlux)**
- **Motivo**: Optei pelo WebClient em vez do RestTemplate por ser não bloqueante e mais moderno, com melhor controle de fluxo e suporte a programação reativa.
- **Implementação**: Utilizei `WebClient.builder()` para personalizar a chamada HTTP, com tratamento de exceções para capturar erros como `429 Too Many Requests` do HubSpot.

---

### **🗂️ Persistência de Tokens com JPA + H2**
- **Motivo**: Precisei armazenar `access_token`, `refresh_token` e `expiresAt` para reusar a autorização do usuário, e escolhi o H2 por ser leve e ideal para testes locais.
- **Implementação**: Modelei a entidade `OAuthToken` com JPA e utilizei um repositório simples com Spring Data (`OAuthTokenRepository`) para persistência.

---

### **📦 Validação de Entrada com Jakarta Validation**
- **Motivo**: Quis garantir que os dados enviados à API fossem válidos antes de tentar criar um contato no HubSpot, evitando chamadas desnecessárias.
- **Implementação**: Anotei os campos do `ContatoDTO` com `@NotBlank` e `@Email`, e usei `@Valid` nos controllers. As exceções são tratadas globalmente.

---

### **🧰 Tratamento Centralizado de Erros**
- **Motivo**: Para manter os controllers limpos e evitar repetição de lógica de erro, optei por centralizar o tratamento.
- **Implementação**: Criei uma classe `GlobalExceptionHandler` com `@ControllerAdvice` para capturar e retornar respostas padronizadas de erro.

---

### **🔐 Autenticação com Spring Security (HTTP Basic)**
- **Motivo**: Para simular um ambiente mais próximo do real, protegi os endpoints da API com autenticação básica, exigindo credenciais para acessá-los.
- **Implementação**: Configurei o `SecurityConfig` para exigir autenticação HTTP Basic apenas nos caminhos da API, deixando o Swagger e H2 Console públicos para testes.

---

### **📖 Documentação via Swagger/OpenAPI**
- **Motivo**: Acredito que uma API bem documentada facilita testes, integração e avaliação durante o processo seletivo.
- **Implementação**: Usei a lib `springdoc-openapi` com anotações como `@Tag`, `@Operation` e `@ApiResponses` nos controllers, disponibilizando a UI em `/swagger-ui.html`.

---


