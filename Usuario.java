package model;

import java.util.ArrayList;

public class Usuario {
    private int id;
    private String nome;
    private String email; // Adicionado
    private String dataNasc;
    private String telefone;
    private ArrayList<Emprestimo> historico;

    public Usuario(int id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.historico = new ArrayList<>();
    }
    public Usuario(int id, String nome, String dataNasc, String telefone) {
        this.id = id;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.telefone = telefone;
        this.historico = new ArrayList<>();
    }

    public void adicionarEmprestimo(Emprestimo e) {
        this.historico.add(e);
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public ArrayList<Emprestimo> getHistorico() { return historico; }
    
    public String getDataNasc() { return dataNasc; }
    public String getTelefone() { return telefone; }
}