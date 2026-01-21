package model.Excecoes;

public class UsuarioNaoEncontrado extends Exception {
    public UsuarioNaoEncontrado() {
        super("Usuário não encontrado.");
    }

    public UsuarioNaoEncontrado(String mensagem) {
        super(mensagem);
    }
}

