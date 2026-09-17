package janela;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

interface WindowConfigurer {
    void configure(JFrame window);
}


class DefaultWindowConfigurer implements WindowConfigurer {

    private Font font = new Font("Arial", Font.PLAIN, 14);

    public void configure(JFrame window) {
        window.setFont(font);
    }

    public void setFont(Font font) {
        this.font = font;
    }
}

class Something extends JFrame {
    private Diario meuDiario;
    private DefaultTableModel modeloTabela;
    private JTable tabelaAlunos;
    public Something(Diario diario) {
        this.meuDiario = diario;
       
        setTitle("Minha Janela Swing");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JPanel painelbotoes = new JPanel();
        painelbotoes.setLayout(new GridLayout(4, 1,10, 10));
        painelbotoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
       
        JButton btnRegistrarAluno = new JButton("Registrar novo aluno");
        JButton btnEditarNota = new JButton("Editar nota");
        JButton btnAtt = new JButton("Atualizar");
        JButton btnHistograma = new JButton("Gerar Histograma");

        painelbotoes.add(btnRegistrarAluno);
        painelbotoes.add(btnEditarNota);
        painelbotoes.add(btnAtt);
        painelbotoes.add(btnHistograma);
        add(painelbotoes, BorderLayout.WEST);


        int qtdeNotas = meuDiario.getQuantidadeNotas();
         String [] colunas = new String[qtdeNotas + 2];
         colunas[0] = "Aluno";

         for (int i = 0; i<qtdeNotas; i++){
            colunas[i+1] = "Nota " + (i+1);
         }
         colunas[qtdeNotas+1] = "Média";
        
        modeloTabela = new DefaultTableModel(colunas, 0);

        tabelaAlunos = new JTable(modeloTabela);
      
        
      
        JScrollPane scrollPane = new JScrollPane(tabelaAlunos);
        
        
        add(scrollPane, BorderLayout.CENTER);


      
        btnRegistrarAluno.addActionListener(e -> {
            try {
                String nomeDigitado = JOptionPane.showInputDialog(this, "Digite o nome do novo aluno:");
                if (nomeDigitado != null && !nomeDigitado.trim().isEmpty()) {
                    meuDiario.adicionarAluno(nomeDigitado);
                    JOptionPane.showMessageDialog(this, "Aluno registrado");
                    atualizarTabela();
                }
            } catch (Exception ex) {
              
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage());
            }
        
        });

                btnEditarNota.addActionListener(e -> {
            try {
                String nomeDigitado = JOptionPane.showInputDialog(this, "Digite o nome do aluno:");
                if (nomeDigitado != null && !nomeDigitado.trim().isEmpty()) {
                    String numStr = JOptionPane.showInputDialog("Qual é a prova?");
                    int numNota = Integer.parseInt(numStr);
                    String valorStr = JOptionPane.showInputDialog("Digite o valor da nota:");
                    int valorNota = Integer.parseInt(valorStr);

                    meuDiario.modificarNota(nomeDigitado, numNota, valorNota);
                    JOptionPane.showMessageDialog(this, "Nota atualizada.");
                    atualizarTabela();
                }
            } catch (Exception ex) {
              
                JOptionPane.showMessageDialog(this, "Erro ao editar nota: " + ex.getMessage());
            }

            
        
        });

      
        btnAtt.addActionListener(e -> atualizarTabela());
        btnHistograma.addActionListener(e -> {
            try {
                String textoDoHistograma = meuDiario.gerarHistograma();
                JTextArea areatexto = new JTextArea(textoDoHistograma);
                areatexto.setFont(new Font("Monospaced", Font.PLAIN, 14));
                areatexto.setEditable(false);
                areatexto.setBackground(null);
                JOptionPane.showMessageDialog(this, areatexto, "Histograma", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar histograma: " + ex.getMessage());
            }
        }

        );

       
        atualizarTabela();
    }

    
    public void atualizarTabela() {
       
        modeloTabela.setRowCount(0);
        int qtdeNotas = meuDiario.getQuantidadeNotas();
       
        
        Aluno[] lista = meuDiario.getListaDeAlunos(); // Supondo que você crie esse getter
        for (int i = 0; i < meuDiario.getQuantidadeCadastrados(); i++) {
        Aluno a = lista[i];
           
        Object[] linha =  new Object[qtdeNotas + 2];
        linha[0] = a.getNomeAluno();
        int [] notas = a.getNotasAluno();
        for (int j = 0; j < qtdeNotas; j++){
            linha[j+1] = notas[j];
        }
        linha[qtdeNotas + 1] = String.format("%.2f", a.calcularMedia());
        modeloTabela.addRow(linha);
        }
    } 
    
 
        /*btnRegistrarAluno.addActionListener(e -> {
            String nomeDigitado = JOptionPane.showInputDialog( this,
            "Digite o nome do novo aluno:",
            "Cadastro do aluno",
            JOptionPane.QUESTION_MESSAGE
        );

        if (nomeDigitado != null && !nomeDigitado.trim().isEmpty()){
            meuDiario.adicionarAluno(nomeDigitado);
            JOptionPane.showMessageDialog(
                this,
                "Aluno " + nomeDigitado + " cadastrado com sucesso.",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
            
            );
        }
           
   
        });*/
}
    



public class window {
    public static void main(String[] args) {
        String turma = JOptionPane.showInputDialog(
            null,
            "Qual o nome da turma?",
            "Lyceum2",
            JOptionPane.QUESTION_MESSAGE

        );

        String alunosStr = JOptionPane.showInputDialog(null, "Quantos alunos tem na sala?");
        int alunos = Integer.parseInt(alunosStr); 
        
        
        String notasStr = JOptionPane.showInputDialog(null, "Quantas notas por aluno precisa?");
        int notas = Integer.parseInt(notasStr);
      
        Diario meuDiario = new Diario(alunos + 50, notas);
        for (int i = 0; i< alunos; i++){
            String nomeDigitado = JOptionPane.showInputDialog(
                null,
                "Digite o nome do " + (i+1) + " aluno: "

            );
            meuDiario.adicionarAluno(nomeDigitado);
        }

        JOptionPane.showMessageDialog(null, "Turma " + turma + " Criada");

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Something something = new Something(meuDiario);
                WindowConfigurer windowConfigurer = new DefaultWindowConfigurer();
                windowConfigurer.configure(something);
                something.setTitle("Lyceum 2.0");
                something.setVisible(true); 
            }
        });
    }
}
