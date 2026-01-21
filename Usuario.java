package model;

import java.util.ArrayList;
import java.time.LocalDate;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private LocalDate dataNasc;
    private String telefone;
    private ArrayList<Emprestimo> historico;

    public Usuario(int id, String nome, String email, LocalDate dataNasc, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = (email != null) ? email : "";
        // CORREÇÃO: Nunca use parse("") em LocalDate
        this.dataNasc = (dataNasc != null) ? dataNasc : LocalDate.now();
        this.telefone = (telefone != null) ? telefone : "";
        this.historico = new ArrayList<>();
    }

    // Construtor secundário para evitar erro de "constructor not found"
    public Usuario(int id, String nome, String email) {
        this(id, nome, email, LocalDate.now(), "");
    }

    public void adicionarEmprestimo(Emprestimo e) {
        this.historico.add(e);
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public LocalDate getDataNasc() { return dataNasc; }
    public String getTelefone() { return telefone; }
    public ArrayList<Emprestimo> getHistorico() { return historico; }


    public String toCSV() {
        String e = (email == null) ? "" : email;
        String d = (dataNasc == null) ? LocalDate.now().toString() : dataNasc.toString();
        String t = (telefone == null) ? "" : telefone;

        // Nota: O prefixo deve bater com o que a Biblioteca lê (USUARIO)
        return "USUARIO;" + id + ";" + nome + ";" + e + ";" + d + ";" + t;
    }
}

