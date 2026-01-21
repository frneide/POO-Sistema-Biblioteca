package model.Excecoes;

public class EmprestimoInvalido extends Exception{
    public EmprestimoInvalido() {
        super("Operação de empréstimo inválida.");
    }

    public EmprestimoInvalido(String mensagem) {
        super(mensagem);
    }
}
