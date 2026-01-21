package model;

import java.time.LocalDate;
import java.util.Scanner;
import model.Excecoes.LivroIndisponivel;
import model.Excecoes.EmprestimoInvalido;

public class Principal {

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.carregarDados();

        Scanner sc = new Scanner(System.in);
        int opcao = 0;

        // Livros iniciais
//        biblioteca.adicionarLivro("Java Básico", "Deitel", "Programação", (short) 2019);
//        biblioteca.adicionarLivro("Clean Code", "Robert C. Martin", "Engenharia", (short) 2008);
//        biblioteca.adicionarLivro("O Senhor dos Anéis", "Tolkien", "Fantasia", (short) 1954);
//        biblioteca.adicionarLivro("Dom Casmurro", "Machado de Assis", "Romance", (short) 1899);
//        biblioteca.adicionarLivro("1984", "George Orwell", "Ficção Científica", (short) 1949);

        do {
            System.out.println("\n===== MENU BIBLIOTECA =====");
            System.out.println("1 - Cadastrar novo usuário");
            System.out.println("2 - Adicionar novo livro");
            System.out.println("3 - Listar todos os livros");
            System.out.println("4 - Emprestar livro");
            System.out.println("5 - Devolver livro");
            System.out.println("6 - Ver histórico de empréstimos");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            try {
                opcao = sc.nextInt();
                sc.nextLine(); // limpa buffer
            } catch (Exception e) {
                System.out.println("Digite apenas números.");
                sc.nextLine();
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    cadastrarUsuarioMenu(sc, biblioteca);
                    break;
                case 2:
                    adicionarLivroMenu(sc, biblioteca);
                    break;
                case 3:
                    biblioteca.listarLivros();
                    break;
                case 4:
                    realizarEmprestimo(sc, biblioteca);
                    break;
                case 5:
                    realizarDevolucao(sc, biblioteca);
                    break;
                case 6:
                    listarHistorico(sc, biblioteca);
                    break;
                case 0:
                    System.out.println("Salvando dados e saindo...");
                    biblioteca.salvarDados();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        sc.close();
    }

    private static void cadastrarUsuarioMenu(Scanner sc, Biblioteca biblioteca) {
        System.out.println("--- CADASTRO DE USUÁRIO ---");
        System.out.println("1 - Aluno | 2 - Professor | 3 - Geral");
        int tipo = sc.nextInt();
        sc.nextLine();

        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();

        // Exemplo de como tratar a data com o novo tipo LocalDate
        LocalDate dataNasc = null;
        try {
            System.out.print("Data de nascimento (AAAA-MM-DD): ");
            dataNasc = LocalDate.parse(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Data inválida! Usando data atual como padrão.");
            dataNasc = LocalDate.now();
        }

        System.out.print("Telefone: ");
        String telefone = sc.nextLine();

        int id = biblioteca.gerarIdUsuario();
        Usuario usuario;

        if (tipo == 1) {
            System.out.print("Matrícula: ");
            String mat = sc.nextLine();
            System.out.print("Curso: ");
            String curso = sc.nextLine();
            usuario = new Aluno(id, nome, email, dataNasc, telefone, mat, curso);
        } else if (tipo == 2) {
            System.out.print("SIAPE: ");
            String siape = sc.nextLine();
            usuario = new Professor(id, nome, email, dataNasc, telefone, siape);
        } else {
            usuario = new Usuario(id, nome, email, dataNasc, telefone);
        }

        biblioteca.cadastrarUsuario(usuario);
        System.out.println("Usuário cadastrado com sucesso! ID: " + id);
    }

    private static void adicionarLivroMenu(Scanner sc, Biblioteca biblioteca) {
        System.out.println("--- NOVO LIVRO ---");
        System.out.print("Título: ");
        String titulo = sc.nextLine();
        System.out.print("Autor: ");
        String autor = sc.nextLine();
        System.out.print("Gênero: ");
        String genero = sc.nextLine();
        System.out.print("Ano: ");
        short ano = sc.nextShort();
        sc.nextLine();

        biblioteca.adicionarLivro(titulo, autor, genero, ano);
        System.out.println("Livro adicionado com sucesso!");
    }

    private static void realizarEmprestimo(Scanner sc, Biblioteca biblioteca) {
        System.out.println("--- EMPRÉSTIMO ---");
        System.out.print("ID do Usuário: ");
        int idUser = sc.nextInt();
        sc.nextLine();

        Usuario usuario = biblioteca.buscarUsuario(idUser);
        if (usuario == null) {
            System.out.println("Erro: Usuário não encontrado.");
            return;
        }

        System.out.print("ID do Livro: ");
        int idLivro = sc.nextInt();
        sc.nextLine();

        try {
            biblioteca.emprestarLivro(usuario, idLivro);
            System.out.println("Empréstimo realizado com sucesso!");
        } catch (LivroIndisponivel e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (EmprestimoInvalido e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void realizarDevolucao(Scanner sc, Biblioteca biblioteca) {
        System.out.println("--- DEVOLUÇÃO ---");
        System.out.print("ID do Usuário: ");
        int idUser = sc.nextInt();
        sc.nextLine();

        Usuario usuario = biblioteca.buscarUsuario(idUser);
        if (usuario == null) {
            System.out.println("Erro: Usuário não encontrado.");
            return;
        }

        System.out.print("ID do Livro: ");
        int idLivro = sc.nextInt();
        sc.nextLine();

        try {
            biblioteca.devolverLivro(usuario, idLivro);
            System.out.println("Livro devolvido com sucesso!");
        } catch (EmprestimoInvalido e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void listarHistorico(Scanner sc, Biblioteca biblioteca) {
        System.out.print("ID do Usuário: ");
        int idUser = sc.nextInt();
        sc.nextLine();

        Usuario usuario = biblioteca.buscarUsuario(idUser);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        biblioteca.listarEmprestimosUsuario(usuario);
    }
}
