package model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import model.Excecoes.LivroIndisponivel;
import model.Excecoes.EmprestimoInvalido;

public class Biblioteca {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Livro> livros;
    private int idUsuarioCount = 1;
    private int idLivroCount = 1;
    private final String ARQUIVO_LIVROS = "livros.txt";
    private final String ARQUIVO_USUARIOS = "usuarios.txt";

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
            System.out.println("Nenhum empréstimo registrado.");
            return;
        }

        for (Emprestimo e : u.getHistorico()) {
            Livro l = e.getLivro();

            System.out.println(
                    "Livro: " + l.getTitulo() +
                            " | Data do empréstimo: " + e.getDataEmprestimo()
            );
        }
    }

    public Livro buscarLivro(int id) {
        for (Livro l : livros) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }

    public void emprestarLivro(Usuario usuario, int idLivro) throws LivroIndisponivel, EmprestimoInvalido {
        Livro livro = buscarLivro(idLivro);
        if (livro == null) throw new EmprestimoInvalido("Livro não encontrado.");

        // O construtor de Emprestimo já chama livro.emprestar(), que lança LivroIndisponivel
        Emprestimo e = new Emprestimo(usuario, livro);
        usuario.adicionarEmprestimo(e);
    }

    public void devolverLivro(Usuario usuario, int idLivro) throws EmprestimoInvalido {
        boolean encontrou = false;
        for (Emprestimo e : usuario.getHistorico()) {
            if (e.getLivro().getId() == idLivro && e.isAtivo()) {
                e.devolverLivro(); // Chama livro.devolver() que lança EmprestimoInvalido
                encontrou = true;
                break;
            }
        }
        if (!encontrou) {
            throw new EmprestimoInvalido("Empréstimo ativo não encontrado para este utilizador.");
        }
    }

    public void salvarDados() {
        // Salvar Livros
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_LIVROS))) {
            for (Livro l : livros) {
                writer.println(l.toCSV());
            }
            System.out.println("Livros salvos com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar livros: " + e.getMessage());
        }

        // Salvar Usuários
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_USUARIOS))) {
            for (Usuario u : usuarios) {
                writer.println(u.toCSV());
            }
            System.out.println("Usuários salvos com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }

    public void carregarDados() {
        try {
            // Carregar Livros
            if (Files.exists(Paths.get("livros.txt"))) {
                for (String linha : Files.readAllLines(Paths.get("livros.txt"))) {
                    livros.add(Livro.fromCSV(linha));
                }
            }

            // Carregar Usuários (Lógica de Herança)
            if (Files.exists(Paths.get("usuarios.txt"))) {
                for (String linha : Files.readAllLines(Paths.get("usuarios.txt"))) {
                    String[] p = linha.split(";");
                    LocalDate dataNasc = LocalDate.parse(p[4]);

                    if (p[0].equals("ALUNO")) {
                        usuarios.add(new Aluno(
                                Integer.parseInt(p[1]), // id
                                p[2],                   // nome
                                p[3],                   // email
                                dataNasc,               // dataNasc
                                p[5],                   // telefone
                                p[6],                   // matricula
                                p[7]                    // curso
                        ));

                    } else if (p[0].equals("PROF")) {
                        usuarios.add(new Professor(
                                Integer.parseInt(p[1]), // id
                                p[2],                   // nome
                                p[3],                   // email
                                dataNasc,               // dataNasc
                                p[5],                   // telefone
                                p[6]                    // siape
                        ));

                    } else {

                        usuarios.add(new Usuario(
                                Integer.parseInt(p[1]), // id
                                p[2],                   // nome
                                p[3],                   // email
                                dataNasc,               // dataNasc (Agora como LocalDate!)
                                p[5]                    // telefone
                        ));
                    }
                }
            }


            // (Lógica adicional necessária para ler historico.txt)

        } catch (Exception e) {
            System.err.println("Aviso: Arquivos não encontrados ou erro na leitura.");
        }

        // (Lógica para atualizar os contadores)

        int maiorIdLivro = 0;
        for (Livro l : livros) {
            if (l.getId() > maiorIdLivro) {
                maiorIdLivro = l.getId();
            }
        }
        this.idLivroCount = maiorIdLivro + 1;


        int maiorIdUsuario = 0;
        for (Usuario u : usuarios) {
            if (u.getId() > maiorIdUsuario) {
                maiorIdUsuario = u.getId();
            }
        }
        this.idUsuarioCount = maiorIdUsuario + 1;
    }

    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }



}
