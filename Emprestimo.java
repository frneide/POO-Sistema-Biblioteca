package model;
import model.Excecoes.LivroIndisponivel;
import model.Excecoes.EmprestimoInvalido;
import java.time.LocalDate;

public class Emprestimo {

    private final Usuario usuario;
    private final Livro livro;
    private final LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private boolean ativo;

    // Construtor Padrão (Novo Empréstimo)
    public Emprestimo(Usuario usuario, Livro livro) throws LivroIndisponivel {
        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = LocalDate.now();
        this.ativo = true;
        livro.emprestar(); // Marca livro como indisponível
    }

    // --- NOVO: Construtor para Carga de Dados (Do Arquivo) ---
    // Permite recriar o objeto sem tentar "emprestar" de novo o livro
    public Emprestimo(Usuario usuario, Livro livro, LocalDate dataEmp, LocalDate dataDev, boolean ativo) {
        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = dataEmp;
        this.dataDevolucao = dataDev;
        this.ativo = ativo;
    }

    public void devolverLivro() throws EmprestimoInvalido {
        if (!ativo) {
            throw new EmprestimoInvalido("Empréstimo já encerrado.");
        }
        livro.devolver();
        ativo = false;
        dataDevolucao = LocalDate.now();
    }

    public boolean isAtivo() { return ativo; }
    public Livro getLivro() { return livro; }
    public Usuario getUsuario() { return usuario; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataDevolucao() { return dataDevolucao; }

    // --- NOVO: Método para gerar CSV ---
    public String toCSV() {
        String dtDev = (dataDevolucao == null) ? "null" : dataDevolucao.toString();
        // Formato: ID_USUARIO;ID_LIVRO;DATA_EMP;DATA_DEV;ATIVO
        return usuario.getId() + ";" + livro.getId() + ";" + dataEmprestimo + ";" + dtDev + ";" + ativo;
    }
}