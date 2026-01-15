package model;
import java.util.ArrayList;

public class Biblioteca {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Livro> livros;
    private int idUsuarioCount = 1;
    private int idLivroCount = 1;

    public Biblioteca() {
        usuarios = new ArrayList<>();
        livros = new ArrayList<>();
    }

    public int gerarIdUsuario() { return idUsuarioCount++; }
    public int gerarIdLivro() { return idLivroCount++; }

    public void adicionarLivro(String titulo, String autor, String genero, short ano) {
        int id = gerarIdLivro();
        Livro livro = new Livro(id, titulo, autor, genero, ano);
        livros.add(livro);
        System.out.println("Livro cadastrado com sucesso! ID: " + id);
    }

    public void cadastrarUsuario(Usuario u) {
        usuarios.add(u);
    }
    public Usuario buscarUsuario(int id) {
        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    public void listarLivros() {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }
        for (Livro l : livros) {
            System.out.println(
                "ID: " + l.getId() +
                " | Título: " + l.getTitulo() +
                " | Autor: " + l.getAutor() +
                " | Disp.: " + (l.getDisponibilidade() ? "Sim" : "Não")
            );
        }
    }

    public void listarEmprestimosUsuario(Usuario u) {
        System.out.println("\nEmpréstimos de: " + u.getNome());
        if (u.getHistorico().isEmpty()) {
            System.out.println("Nenhum empréstimo ativo.");
            return;
        }

        for (Emprestimo e : u.getHistorico()) {
            for (Livro l : livros) {
                if (l.getId() == e.getIdLivro()) {
                    System.out.println("Livro: " + l.getTitulo() + " | Data: " + e.getData());
                }
            }
        }
    }

    public boolean emprestarLivro(Usuario u, int idLivro) {
        for (Livro l : livros) {
            if (l.getId() == idLivro) {
                if (l.getDisponibilidade()) {
                    l.setDisponibilidade(false);
                    Emprestimo novoEmprestimo = new Emprestimo(l.getId());
                    u.adicionarEmprestimo(novoEmprestimo);
                    
                    System.out.println("SUCESSO: Livro '" + l.getTitulo() + "' emprestado para " + u.getNome());
                    return true;
                } else {
                    System.out.println("ERRO: Este livro já está emprestado!");
                    return false;
                }
            }
        }
        System.out.println("ERRO: Livro não encontrado!");
        return false;
    }

    public boolean devolverLivro(Usuario u, int idLivro) {
        for (Livro l : livros) {
            if (l.getId() == idLivro) {
                if (!l.getDisponibilidade()) {
                    l.setDisponibilidade(true);
                    System.out.println("SUCESSO: Livro '" + l.getTitulo() + "' devolvido!");
                    return true;
                } else {
                    System.out.println("ERRO: Este livro já está disponível!");
                    return false;
                }
            }
        }
        System.out.println("ERRO: Livro não encontrado.");
        return false;
    }
}