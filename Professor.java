package model;
public class Professor extends Usuario {
    private String siape;

    public Professor(int id, String nome, String dataNasc, String telefone, String siape) {
        super(id, nome, dataNasc, telefone);
        this.siape = siape;
    }
    
    public String getSiape() { return siape; }
    public void setSiape(String siape) { this.siape = siape; }
}