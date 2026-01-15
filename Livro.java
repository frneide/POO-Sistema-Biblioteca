package model;

public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private String genero;
    private short ano;
    private boolean disponibilidade;

    public Livro(int id, String titulo, String autor, String genero, short ano) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.ano = ano;
        this.disponibilidade = true;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getGenero() { return genero; }
    public short getAno() { return ano; }
    
    public boolean getDisponibilidade() { return disponibilidade; }
    public void setDisponibilidade(boolean disponibilidade) { 
        this.disponibilidade = disponibilidade; 
    }
}