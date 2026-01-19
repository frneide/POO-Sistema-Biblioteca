package model;

import java.util.ArrayList;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String dataNasc;
    private String telefone;
    private ArrayList<Emprestimo> historico;

    public Usuario(int id, String nome, String email, String dataNasc, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = (email != null) ? email : "";
        this.dataNasc = (dataNasc != null) ? dataNasc : "";
        this.telefone = (telefone != null) ? telefone : "";
        this.historico = new ArrayList<>();
    }

    public Usuario(int id, String nome, String email) {
        this(id, nome, email, "", ""); 
    }

    public void adicionarEmprestimo(Emprestimo e) {
        this.historico.add(e);
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getDataNasc() { return dataNasc; }
    public String getTelefone() { return telefone; }
    public ArrayList<Emprestimo> getHistorico() { return historico; }
    
   public String toCSV() {
    // Garante que se o campo for null, salve apenas um espaço vazio
    String e = (email == null) ? "" : email;
    String d = (dataNasc == null) ? "" : dataNasc;
    String t = (telefone == null) ? "" : telefone;
    
        return "USUARIO;" + id + ";" + nome + ";" + e + ";" + d + ";" + t;
    }
}

