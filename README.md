Projeto utilizando Java 17, SpringBoot, MySql



```mermaid

classDiagram
  class Usuario {
    -id: int
    -nome: string
    -email: string
    -senha: string
  }

  class MembroEquipe {
    -projetos: List<Projeto>
    +cadastrarTarefa(projeto: Projeto, descricao: string): Tarefa
    +visualizarStatusTarefa(tarefa: Tarefa): string
    +alterarStatusTarefa(tarefa: Tarefa, novoStatus: string): void
  }

  class GerenteProjetos {
    +projetos: List<Projeto>
    +cadastrarProjeto(nome: string, descricao: string): Projeto
    +alterarProjeto(projeto: Projeto, novaDescricao: string): void
    +atribuirTarefa(projeto: Projeto, tarefa: Tarefa, membro: MembroEquipe): void
    +visualizarAndamentoProjeto(projeto: Projeto): string
  }

  class Administrador {
    +usuarios: List<Usuario>
    +projetos: List<Projeto>
    +adicionarUsuario(nome: string, email: string, senha: string): Usuario
    +alterarUsuario(usuario: Usuario, novoNome: string): void
    +removerUsuario(usuario: Usuario): void
    +adicionarProjeto(nome: string, descricao: string): Projeto
    +alterarProjeto(projeto: Projeto, novaDescricao: string): void
    +removerProjeto(projeto: Projeto): void
  }

  class Projeto {
    -id: int
    -nome: string
    -descricao: string
    -tarefas: List<Tarefa>
  }

  class Tarefa {
    -id: int
    -descricao: string
    -status: string
    -responsavel: MembroEquipe
  }

  Usuario <|-- MembroEquipe
  Usuario <|-- GerenteProjetos
  Usuario <|-- Administrador
  MembroEquipe "*" -- "1..*" Projeto
  GerenteProjetos "1" -- "1..*" Projeto
  Administrador "1" -- "*" Usuario
  Administrador "1" -- "*" Projeto
  Projeto "1" -- "*" Tarefa
  Tarefa "1" -- "1" MembroEquipe
