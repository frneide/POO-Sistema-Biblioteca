package model;

public class Exemplares extends Livro{
    
    private quantidadeExemplares;
    private boolean disponibilidade;

    public Exemplares (int id, String titulo, String autor, String genero, short ano) {
        super(id, titulo, autor, genero, ano);
        this.disponibilidade = true;
    }
    public boolean getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}



