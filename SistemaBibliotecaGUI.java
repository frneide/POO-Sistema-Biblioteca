package pctinterface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SistemaBibliotecaGUI extends JFrame {

    //Componentes principais para o CardLayout
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public SistemaBibliotecaGUI() {
        //Configuração do JFrame Principal
        setTitle("Sistema de Gerenciamento de Biblioteca - Trabalho Final POO");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Painel de Navegação (Lateral)
        JPanel menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.WEST);

        //Painel Principal com CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Adicionando as telas (JPanels) ao CardLayout
        mainPanel.add(createPanelCadastroLivro(), "Livro");
        mainPanel.add(createPanelCadastroUsuario(), "Usuario");
        mainPanel.add(createPanelEmprestimo(), "Emprestimo");
        mainPanel.add(createPanelDevolucao(), "Devolucao");
        mainPanel.add(createPanelListagem(), "Listagem");

        add(mainPanel, BorderLayout.CENTER);
    }

    // MENU LATERAL
    private JPanel createMenuPanel() {
        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(6, 1, 10, 10));
        menu.setBackground(Color.LIGHT_GRAY);
        menu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Botões
        JButton btnLivro = new JButton("Cadastrar Livro");
        JButton btnUsuario = new JButton("Cadastrar Usuário");
        JButton btnEmprestimo = new JButton("Realizar Empréstimo");
        JButton btnDevolucao = new JButton("Realizar Devolução");
        JButton btnListagem = new JButton("Listagem Geral");
        JButton btnSair = new JButton("Sair");

        // Ações dos Botões
        btnLivro.addActionListener(e -> cardLayout.show(mainPanel, "Livro"));
        btnUsuario.addActionListener(e -> cardLayout.show(mainPanel, "Usuario"));
        btnEmprestimo.addActionListener(e -> cardLayout.show(mainPanel, "Emprestimo"));
        btnDevolucao.addActionListener(e -> cardLayout.show(mainPanel, "Devolucao"));
        btnListagem.addActionListener(e -> cardLayout.show(mainPanel, "Listagem"));
        btnSair.addActionListener(e -> System.exit(0));

        menu.add(btnLivro);
        menu.add(btnUsuario);
        menu.add(btnEmprestimo);
        menu.add(btnDevolucao);
        menu.add(btnListagem);
        menu.add(btnSair);

        return menu;
    }

    //TELA 1- CADASTRO lIVRO
    private JPanel createPanelCadastroLivro() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Novo Livro"));

        JTextField txtTitulo = new JTextField();
        JTextField txtAutor = new JTextField();
        JTextField txtISBN = new JTextField();
        JButton btnSalvar = new JButton("Salvar Livro");

        panel.add(new JLabel("Título:"));
        panel.add(txtTitulo);
        panel.add(new JLabel("Autor:"));
        panel.add(txtAutor);
        panel.add(new JLabel("ISBN:"));
        panel.add(txtISBN);
        panel.add(new JLabel(""));//vazioo
        panel.add(btnSalvar);

        // Botão salvar
        btnSalvar.addActionListener(e -> {
            // [METODO **** COLOCA DPS ****]
            String msg = "Livro '" + txtTitulo.getText() + "' cadastrado com sucesso!";
            JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            // Limpa campos
            txtTitulo.setText("");
            txtAutor.setText("");
            txtISBN.setText("");
        });

        return wrapInPanel(panel); // Helper p ñ esticar componentes
    }

    //TELA 2- CADASTRO USUÁRIO
    private JPanel createPanelCadastroUsuario() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Novo Usuário"));

        JTextField txtNome = new JTextField();
        JTextField txtMatricula = new JTextField();
        JButton btnSalvar = new JButton("Salvar Usuário");

        panel.add(new JLabel("Nome Completo:"));
        panel.add(txtNome);
        panel.add(new JLabel("Matrícula/CPF:"));
        panel.add(txtMatricula);
        panel.add(new JLabel(""));
        panel.add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Usuário " + txtNome.getText() + " salvo!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            txtNome.setText("");
            txtMatricula.setText("");
        });

        return wrapInPanel(panel);
    }

    //TELA 3- EMPRÉSTIMO
    private JPanel createPanelEmprestimo() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Empréstimo"));

        JTextField txtIdUsuario = new JTextField();
        JTextField txtIsbnLivro = new JTextField();
        JButton btnConfirmar = new JButton("Confirmar Empréstimo");

        panel.add(new JLabel("Matrícula do Usuário:"));
        panel.add(txtIdUsuario);
        panel.add(new JLabel("ISBN do Livro:"));
        panel.add(txtIsbnLivro);
        panel.add(new JLabel(""));
        panel.add(btnConfirmar);

        btnConfirmar.addActionListener(e -> {
            // [METODO AQUI ***COLOCAR DPS***]
            JOptionPane.showMessageDialog(this, "Empréstimo realizado para o livro ISBN: " + txtIsbnLivro.getText());
        });

        return wrapInPanel(panel);
    }

    //TELA 4 -DEVOLUÇÃO
    private JPanel createPanelDevolucao() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Devolução"));

        JTextField txtIsbnLivro = new JTextField();
        JButton btnDevolver = new JButton("Confirmar Devolução");

        panel.add(new JLabel("ISBN do Livro:"));
        panel.add(txtIsbnLivro);
        panel.add(new JLabel(""));
        panel.add(btnDevolver);

        btnDevolver.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Livro devolvido com sucesso!");
        });

        return wrapInPanel(panel);
    }

    // TELA 5- LISTAGEM
    private JPanel createPanelListagem() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Listagem de Itens"));

        // Tabela testeee
        String[] colunas = {"ID", "Tipo", "Descrição", "Status"};
        Object[][] dados = {
                {"1", "Livro", "Morro dos Ventos Uivantes", "Disponível"},
                {"2", "Usuário", "Margarida Furio Jolyne", "Ativo"},
                {"3", "Livro", "O Processo", "Emprestado"}
        };
        
        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);

        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Consultando arquivos... (Integração necessária)");
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnAtualizar, BorderLayout.SOUTH);

        return panel;
    }

    //Centralizaçao
    private JPanel wrapInPanel(JPanel form) {
        JPanel wrapper = new JPanel(new FlowLayout());
        wrapper.add(form);
        return wrapper;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SistemaBibliotecaGUI().setVisible(true);
        });
    }
}