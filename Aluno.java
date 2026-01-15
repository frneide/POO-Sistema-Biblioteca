package model;
public class Aluno extends Usuario {
    private String matricula;
    private String curso;

    public Aluno(int id, String nome, String dataNasc, String telefone, String matricula, String curso) {
        super(id, nome, dataNasc, telefone);
        this.matricula = matricula;
        this.curso = curso;
    }
    
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
}