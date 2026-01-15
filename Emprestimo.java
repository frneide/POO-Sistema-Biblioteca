package model;
import java.time.LocalDate;

public class Emprestimo {
    
    private int idLivro;
    private LocalDate data;

    public Emprestimo(int idLivro) {
        this.idLivro = idLivro;
        this.data = LocalDate.now(); 
    }

    public int getIdLivro() {
        return idLivro;
    }

    public LocalDate getData() {
        return data;
    }
}