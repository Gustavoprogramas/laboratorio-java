package lab04;
import java.util.Scanner;

public class TesteAluno {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		int alunos;
		int notas;
		System.out.println("\nQuantos alunos tem na sala?\n");
		alunos = input.nextInt();
		System.out.println("\nQuantas notas por aluno precisa?\n");
		notas = input.nextInt();
		Diario meuDiario = new Diario(alunos, notas);
		input.nextLine();
		
		for (int i = 0; i < alunos; i++) {
			System.out.println("Digite o nome do " + (i + 1) + " Aluno: ");
			String nomeDigitado = input.nextLine();
			meuDiario.adicionarAluno(nomeDigitado);
		
			for (int j = 0; j < notas; j++) {
				System.out.println("Digite a nota " + (j + 1) + " de " + nomeDigitado + ":");
				double nota = input.nextDouble();
				if (nota > 10 || nota < 0) {
					System.out.println("Nao pode nota maior que 10, nem menor que 0.");
					j = j-1;
				} else {
					meuDiario.modificarNota(nomeDigitado, j + 1, nota);
				}
				
			}
			input.nextLine();
		}
		
		meuDiario.gerarRelatorioDeNotas();
		meuDiario.relatorioMaiorNota();
		
		
		
		
		
	}

}
