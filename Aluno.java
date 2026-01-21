package model;

public class Aluno extends Usuario {
    private String matricula;
    private String curso;

    public Aluno(int id, String nome, String email, String dataNasc,
                 String telefone, String matricula, String curso) {

        super(id, nome, email, dataNasc, telefone);
        this.matricula = matricula;
        this.curso = curso;
    }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    @Override
    public String toCSV() {
        return "ALUNO;" + getId() + ";" + getNome() + ";" + getEmail() + ";"
                + getDataNasc() + ";" + getTelefone() + ";"
                + matricula + ";" + curso;
    }
}
