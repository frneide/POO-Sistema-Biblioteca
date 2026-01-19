package model;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.carregarDados();

        Scanner sc = new Scanner(System.in);
        int opcao = 0; 
        
        biblioteca.adicionarLivro("Java Básico", "Deitel", "Programação", (short) 2019);
        biblioteca.adicionarLivro("Clean Code", "Robert C. Martin", "Engenharia", (short) 2008);
        biblioteca.adicionarLivro("O Senhor dos Anéis", "Tolkien", "Fantasia", (short) 1954);
        biblioteca.adicionarLivro("Dom Casmurro", "Romance", "Machado de Assis", (short) 1899);
        biblioteca.adicionarLivro("O Senhor dos Anéis", "Fantasia", "J.R.R. Tolkien", (short) 1954);
        biblioteca.adicionarLivro("1984", "Ficção Científica", "George Orwell", (short) 1949);
        biblioteca.adicionarLivro("O Pequeno Príncipe", "Fábula", "Antoine de Saint-Exupéry", (short) 1943);
        biblioteca.adicionarLivro("Código Limpo", "T3ecnologia", "Robert C. Martin", (short) 2008);
        biblioteca.adicionarLivro("Harry Potter e a Pedra Filosof0al", "Fantasia", "J.K. Rowling", (short) 1997);
        biblioteca.adicionarLivro("Vidas Secas", "Romance", "Graciliano Ramos", (short) 1938);


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
                sc.nextLine(); 
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
                    System.out.println("--- NOVO LIVRO ---");
                    System.out.print("Título: ");
                    String titulo = sc.nextLine();
                    System.out.print("Autor: ");
                    String autor = sc.nextLine();
                    System.out.print("Gênero: ");
                    String genero = sc.nextLine();
                    System.out.print("Ano: ");
                    short ano = sc.nextShort();
                    biblioteca.adicionarLivro(titulo, autor, genero, ano);
                    break;
                case 3:
                    System.out.println("--- LISTA DE LIVROS ---");
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
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Data de nascimento: ");
        String dataNasc = sc.nextLine();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine();


        int id = biblioteca.gerarIdUsuario();
        Usuario u = new Usuario(id, nome, email, dataNasc, telefone);
        biblioteca.cadastrarUsuario(u);
        System.out.println("Usuário cadastrado! ID: " + id);
    }

    private static void realizarEmprestimo(Scanner sc, Biblioteca biblioteca) {
        System.out.println("--- EMPRÉSTIMO ---");
        System.out.print("ID do Usuário: ");
        int idUser = sc.nextInt();
        
        Usuario usuarioEncontrado = biblioteca.buscarUsuario(idUser);

        if (usuarioEncontrado == null) {
            System.out.println("Erro: Usuário não encontrado.");
            return;
        }

        System.out.print("ID do Livro: ");
        int idLivro = sc.nextInt();
        biblioteca.emprestarLivro(usuarioEncontrado, idLivro);
    }

    private static void realizarDevolucao(Scanner sc, Biblioteca biblioteca) {
        System.out.println("--- DEVOLUÇÃO ---");
        System.out.print("ID do Usuário: ");
        int idUser = sc.nextInt();
        
        Usuario usuarioEncontrado = biblioteca.buscarUsuario(idUser);
        if (usuarioEncontrado == null) {
            System.out.println("Erro: Usuário não encontrado.");
            return;
        }

        System.out.print("ID do Livro: ");
        int idLivro = sc.nextInt();
        biblioteca.devolverLivro(usuarioEncontrado, idLivro);
    }

    private static void listarHistorico(Scanner sc, Biblioteca biblioteca) {
        System.out.print("ID do Usuário: ");
        int idUser = sc.nextInt();
        Usuario usuarioEncontrado = biblioteca.buscarUsuario(idUser);

        if (usuarioEncontrado != null) {
            biblioteca.listarEmprestimosUsuario(usuarioEncontrado);
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }
}
