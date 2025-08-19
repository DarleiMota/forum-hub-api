# Fórum Hub 🚀

## 📄 Descrição

**Fórum Hub** é um projeto de fórum online desenvolvido para replicar o funcionamento de um fórum de perguntas e respostas, onde os usuários podem interagir, tirar dúvidas e compartilhar conhecimento sobre cursos e projetos. Ele permite que os usuários criem, visualizem, atualizem e removam tópicos, implementando funcionalidades completas de CRUD (Create, Read, Update, Delete).

Este projeto foi desenvolvido como parte do **Challenge Back End**, com foco em:

- Criar uma **API REST** seguindo as melhores práticas do modelo REST;
- Validar dados segundo regras de negócio específicas;
- Implementar uma **base de dados relacional** para persistência de informações;
- Garantir **autenticação e autorização** via JWT, restringindo o acesso a certas operações apenas a usuários autorizados.

## 🛠 Tecnologias

![Java](https://img.shields.io/badge/Java-21-blue?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green?logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-20.10-blue?logo=docker&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-secure-yellow)
![Maven](https://img.shields.io/badge/Maven-3.9-red?logo=apachemaven&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-1.18-orange?logo=lombok&logoColor=white)


## 🏗️ Estrutura do Banco

O banco é em MySQL e possui as tabelas principais:

- **usuario**
- **curso**
- **topico**
- **resposta** (planejado, não implementado)

### Docker Compose do MySQL

```yaml
services:
  mysql:
    image: mysql:8.0
    container_name: mysql_forumhub
    restart: always
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: forumhub
      MYSQL_USER: user
      MYSQL_PASSWORD: password
    volumes:
      - ./mysql-data:/var/lib/mysql
```

## 🔗 Endpoints

### Usuários

- `POST /auth/login` - Login

- `POST /usuarios`       → Cadastra um novo usuário
- `GET /usuarios/{id}`   → Busca um usuário pelo ID
- `GET /usuarios`        → Lista todos os usuários (paginado)
- `PUT /usuarios/{id}`   → Atualiza os dados de um usuário
- `DELETE /usuarios/{id}`→ Remove um usuário


### Cursos

- `GET /cursos` - Listar cursos
- `GET /cursos/{id}` - Buscar curso por ID


### Tópicos

- `POST /topicos` - Criar tópico (usuário logado)
- `GET /topicos` - Listar todos os tópicos
- `GET /topicos/{id}` - Buscar tópico por ID
- `GET /topicos/por-curso/{nomeCurso}` - Filtrar por curso
- `GET /topicos/por-status/{status}` - Filtrar por status
- `GET /topicos/por-autor/{autorId}` - Filtrar por autor
- `GET /topicos/buscar/?texto=...` - Buscar por texto
- `GET /topicos/filtro-combinado?nomeCurso=...&status=...` - Filtro combinado
- `PUT /topicos/{id}` - Atualizar tópico (apenas autor)
- `DELETE /topicos/{id}` - Excluir tópico (apenas autor)

## 🔐 Segurança

- Autenticação via **JWT**
- Usuários só podem alterar/excluir seus próprios tópicos

## 📥 Download / Repositório

- [Download ZIP do repositório](https://github.com/DarleiMota/FormacaoBackEnd/archive/refs/heads/main.zip)
- [GitHub - Fórum Hub](https://github.com/DarleiMota/FormacaoBackEnd)

## 📝 Observações

- Banco em MySQL
- Docker Compose já configurado
- Endpoints completos documentados acima