package model.Excecoes;

public class LivroIndisponivel extends Exception{
    public LivroIndisponivel() {
        super("Livro indisponível para empréstimo.");
    }

    public LivroIndisponivel(String mensagem) {
        super(mensagem);
    }
}

