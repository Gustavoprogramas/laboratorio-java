package lab04;
import java.util.Scanner;

public class TesteAluno {
    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);

        int alunos;
        int notas;
        
        System.out.println("Quantos alunos tem na sala?");
        alunos = input.nextInt();
        
        System.out.println("Quantas notas por aluno precisa?");
        notas = input.nextInt();
        
    
        Diario meuDiario = new Diario(alunos, notas);
        
    
        input.nextLine(); 

        int opcao = -1; 

     
        while (opcao != 0) {
            System.out.println("\n========= MENU =========");
            System.out.println("1 - Adicionar um novo aluno");
            System.out.println("2 - Adicionar/Editar a nota de um aluno");
            System.out.println("3 - Gerar relatório de notas");
            System.out.println("0 - Sair do programa");
            System.out.print("Escolha uma opção: ");
            
            opcao = input.nextInt();
            input.nextLine(); 

            switch (opcao) {
                case 1:
                    System.out.print("\nDigite o nome do novo aluno: ");
                  
                    String nomeDigitado = input.nextLine(); 
                    meuDiario.adicionarAluno(nomeDigitado);
                    System.out.println("Aluno cadastrado!");
                    break;
                    
                case 2:
                    System.out.print("\nDigite o nome do aluno que receberá a nota: ");
                    String nomeNota = input.nextLine();
                    
                    System.out.print("Qual é o número da nota (Ex: 1, 2...)? ");
                    int numNota = input.nextInt();
                    
                    System.out.print("Digite o valor da nota: ");
                    double valorNota = input.nextDouble();
                    
                    
                    if (valorNota > 10 || valorNota < 0) {
                        System.out.println("Erro: Não pode nota maior que 10, nem menor que 0.");
                    } else if (numNota < 1 || numNota > notas) {
                        System.out.println("Erro: Esse número de avaliação não existe.");
                    } else {
                        meuDiario.modificarNota(nomeNota, numNota, valorNota);
                        System.out.println("Nota atualizada com sucesso!");
                    }
                    break;
                    
                case 3:
                    System.out.println();
                    meuDiario.gerarRelatorioDeNotas();
                    meuDiario.relatorioMaiorNota();
                    break;
                    
                case 0:
                    System.out.println("\nEncerrando o sistema. Até logo!");
                    break;
                    
                default:
                    System.out.println("\nOpção inválida! Tente novamente.");
            }
        }
        
        
        input.close(); 
    }
}
