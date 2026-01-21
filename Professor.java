package model;

import java.time.LocalDate;

public class Professor extends Usuario {
    private String siape;

    // Adicionado o campo 'email' para coincidir com a classe Usuario
    public Professor(int id, String nome, String email, LocalDate dataNasc, String telefone, String siape) {
        super(id, nome, email, dataNasc, telefone);
        this.siape = siape;
    }

    public String getSiape() { return siape; }
    public void setSiape(String siape) { this.siape = siape; }

    @Override
    public String toCSV() {
        return "PROF;" + getId() + ";" + getNome() + ";" + getEmail() + ";" + getDataNasc() + ";" + getTelefone() + ";" + siape;
    }
}