package model;
import model.Excecoes.EmprestimoInvalido;
import model.Excecoes.LivroIndisponivel;

import java.time.LocalDate;

public class Emprestimo {

    private Usuario usuario;
    private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private boolean ativo;

    public Emprestimo(Usuario usuario, Livro livro)
            throws LivroIndisponivel {

        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = LocalDate.now();
        this.ativo = true;

        livro.emprestar();
    }

    public void devolverLivro() throws EmprestimoInvalido {
        if (!ativo) {
            throw new EmprestimoInvalido("Empréstimo já encerrado.");
        }

        livro.devolver();
        ativo = false;
        dataDevolucao = LocalDate.now();
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Livro getLivro() {
        return livro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }
}
