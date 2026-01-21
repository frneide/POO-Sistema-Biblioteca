package model;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Excecoes.LivroIndisponivel;
import model.Excecoes.EmprestimoInvalido;

public class Biblioteca {
    // Listas principais (Privadas)
    private ArrayList<Usuario> usuarios;
    private ArrayList<Livro> livros;

    // Contadores de ID
    private int idUsuarioCount = 1;
    private int idLivroCount = 1;

    // Nomes dos arquivos
    private final String ARQUIVO_LIVROS = "livros.txt";
    private final String ARQUIVO_USUARIOS = "usuarios.txt";
    private final String ARQUIVO_EMPRESTIMOS = "emprestimos.txt";

    public Biblioteca() {
        usuarios = new ArrayList<>();
        livros = new ArrayList<>();
    }

    // --- MÉTODOS DE GET (Necessários para corrigir o erro 'Cannot resolve symbol') ---
    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public ArrayList<Usuario> getUsuarios() { // Esse era o que faltava para a aba de Usuários!
        return usuarios;
    }

    // --- GERENCIAMENTO DE IDs ---
    public int gerarIdUsuario() { return idUsuarioCount++; }
    public int gerarIdLivro() { return idLivroCount++; }

    // --- CADASTROS E BUSCAS ---
    public void adicionarLivro(String titulo, String autor, String genero, short ano) {
        int id = gerarIdLivro();
        Livro livro = new Livro(id, titulo, autor, genero, ano);
        livros.add(livro);
        System.out.println("Livro cadastrado: " + titulo);
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

    public Livro buscarLivro(int id) {
        for (Livro l : livros) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }

    // --- OPERAÇÕES DE EMPRÉSTIMO ---
    public void emprestarLivro(Usuario usuario, int idLivro) throws LivroIndisponivel, EmprestimoInvalido {
        Livro livro = buscarLivro(idLivro);
        if (livro == null) throw new EmprestimoInvalido("Livro não encontrado.");

        // Cria o empréstimo (o construtor já marca o livro como indisponível)
        Emprestimo e = new Emprestimo(usuario, livro);
        usuario.adicionarEmprestimo(e);
    }

    public void devolverLivro(Usuario usuario, int idLivro) throws EmprestimoInvalido {
        boolean encontrou = false;
        for (Emprestimo e : usuario.getHistorico()) {
            // Procura um empréstimo ATIVO deste livro
            if (e.getLivro().getId() == idLivro && e.isAtivo()) {
                e.devolverLivro();
                encontrou = true;
                break;
            }
        }
        if (!encontrou) {
            throw new EmprestimoInvalido("Nenhum empréstimo ativo encontrado deste livro para este usuário.");
        }
    }

    // --- LISTAGENS NO CONSOLE (Opcional, pois você usa GUI) ---
    public void listarLivros() {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }
        for (Livro l : livros) {
            System.out.println("ID: " + l.getId() + " | " + l.getTitulo() + " | Disp: " + (l.getDisponibilidade() ? "Sim" : "Não"));
        }
    }

    public void listarEmprestimosUsuario(Usuario u) {
        System.out.println("Empréstimos de: " + u.getNome());
        for (Emprestimo e : u.getHistorico()) {
            System.out.println("- " + e.getLivro().getTitulo() + " (Ativo: " + e.isAtivo() + ")");
        }
    }

    // --- PERSISTÊNCIA DE DADOS (Salvar/Carregar) ---
    public void salvarDados() {
        // 1. Salvar Livros
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_LIVROS))) {
            for (Livro l : livros) {
                writer.println(l.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar livros: " + e.getMessage());
        }

        // 2. Salvar Usuários
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_USUARIOS))) {
            for (Usuario u : usuarios) {
                writer.println(u.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }

        // 3. Salvar Empréstimos (CORREÇÃO CRÍTICA)
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_EMPRESTIMOS))) {
            for (Usuario u : usuarios) {
                for (Emprestimo e : u.getHistorico()) {
                    writer.println(e.toCSV());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar empréstimos: " + e.getMessage());
        }
    }

    public void carregarDados() {
        try {
            // 1. Carregar Livros
            if (Files.exists(Paths.get(ARQUIVO_LIVROS))) {
                for (String linha : Files.readAllLines(Paths.get(ARQUIVO_LIVROS))) {
                    livros.add(Livro.fromCSV(linha));
                }
            }

            // 2. Carregar Usuários
            if (Files.exists(Paths.get(ARQUIVO_USUARIOS))) {
                for (String linha : Files.readAllLines(Paths.get(ARQUIVO_USUARIOS))) {
                    String[] p = linha.split(";", -1);
                    // Evita erro se a data vier nula ou mal formatada
                    LocalDate dataNasc = (p[4].equals("null") || p[4].isEmpty()) ? LocalDate.now() : LocalDate.parse(p[4]);

                    if (p[0].equals("ALUNO")) {
                        usuarios.add(new Aluno(
                                Integer.parseInt(p[1]), p[2], p[3], dataNasc, p[5], p[6], p[7]
                        ));
                    } else if (p[0].equals("PROF")) {
                        usuarios.add(new Professor(
                                Integer.parseInt(p[1]), p[2], p[3], dataNasc, p[5], p[6]
                        ));
                    } else {
                        usuarios.add(new Usuario(
                                Integer.parseInt(p[1]), p[2], p[3], dataNasc, p[5]
                        ));
                    }
                }
            }

            // 3. Carregar Empréstimos (Lógica Nova)
            if (Files.exists(Paths.get(ARQUIVO_EMPRESTIMOS))) {
                List<String> linhas = Files.readAllLines(Paths.get(ARQUIVO_EMPRESTIMOS));
                for (String linha : linhas) {
                    try {
                        String[] p = linha.split(";", -1);
                        int idU = Integer.parseInt(p[0]);
                        int idL = Integer.parseInt(p[1]);
                        LocalDate dtEmp = LocalDate.parse(p[2]);
                        LocalDate dtDev = p[3].equals("null") ? null : LocalDate.parse(p[3]);
                        boolean ativo = Boolean.parseBoolean(p[4]);

                        Usuario u = buscarUsuario(idU);
                        Livro l = buscarLivro(idL);

                        if (u != null && l != null) {
                            // Usa o construtor especial de carga (sem validação de disponibilidade)
                            Emprestimo emp = new Emprestimo(u, l, dtEmp, dtDev, ativo);
                            u.adicionarEmprestimo(emp);
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha de empréstimo: " + linha);
                    }
                }
            }

            // Atualiza contadores para não repetir IDs
            atualizarContadores();

        } catch (Exception e) {
            System.err.println("Erro geral ao carregar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarContadores() {
        int maiorIdLivro = 0;
        for (Livro l : livros) {
            if (l.getId() > maiorIdLivro) maiorIdLivro = l.getId();
        }
        this.idLivroCount = maiorIdLivro + 1;

        int maiorIdUsuario = 0;
        for (Usuario u : usuarios) {
            if (u.getId() > maiorIdUsuario) maiorIdUsuario = u.getId();
        }
        this.idUsuarioCount = maiorIdUsuario + 1;
    }
}