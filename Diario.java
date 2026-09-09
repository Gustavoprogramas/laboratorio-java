package lab04;



public class Diario {
	private Aluno[] listaDeAlunos;
	private int quantidadedeNotasPorAluno;
	private int contadorDeAlunosCadastrados;
	
	public Diario (int capacidadeMaximaAlunos, int qtdeNotas) {
		listaDeAlunos = new Aluno[capacidadeMaximaAlunos];
		quantidadedeNotasPorAluno = qtdeNotas;
		contadorDeAlunosCadastrados = 0;
	}
	
	public void adicionarAluno(String nomeNovoAluno) {
		if (contadorDeAlunosCadastrados < listaDeAlunos.length) {
			Aluno novoAluno = new Aluno(nomeNovoAluno, quantidadedeNotasPorAluno);
			listaDeAlunos[contadorDeAlunosCadastrados] = novoAluno;
			contadorDeAlunosCadastrados++;
		} else {
			System.out.println("Diario cheio.");
		}
	}
	
	public void modificarNota(String nomeAluno, int numeroDaNota, double novaNota) {
		for (int cont = 0; cont < contadorDeAlunosCadastrados; cont++) {
			if (listaDeAlunos[cont].getNomeAluno().equals(nomeAluno)) {
				listaDeAlunos[cont].setNota(numeroDaNota - 1, novaNota);
				return;
			}
		}
	}
	
	
	
	public static void imprimirCabecalhoRelatorio() {
        System.out.println("========================================");
        System.out.println("            RELATORIO DE NOTAS           ");
        System.out.println("========================================");
	}
	
	public void gerarRelatorioDeNotas() {
		imprimirCabecalhoRelatorio();
		for (int cont = 0; cont < contadorDeAlunosCadastrados; cont++) {
			Aluno a = listaDeAlunos[cont];
		    System.out.print("Aluno " + a.getNomeAluno() + " | Notas: ");
		    double[] notas = a.getNotasAluno();
		    for (int j = 0; j < notas.length; j++) {
		    	System.out.print(notas[j] + " | ");
		    }
		    System.out.printf(" Média: %.2f\n", a.calcularMedia());
		}
	}
	public void relatorioMaiorNota() {
		double maior = 0;
		double menor = 10;
		for (int i = 0; i < contadorDeAlunosCadastrados; i++) {
			double[] notas = listaDeAlunos[i].getNotasAluno();
			for (int j = 0; j < notas.length; j++) {
				if (notas[j] > maior) maior = notas[j];
				if (notas[j] < menor) menor = notas[j];
			}
		}
		System.out.println("\nMaior nota: " + maior);
		System.out.println("\nMenor nota: "+ menor);
	}
}
