package lab04;

public class Aluno {
	private String nomeAluno;
	private double[] notasAluno;
	
	
	public Aluno(String nomeInicial, int qtdeNotas) {
		nomeAluno = nomeInicial;
		notasAluno = new double[qtdeNotas];
	}
	public String getNomeAluno() {
		return nomeAluno;
	}
	public double[] getNotasAluno() {
		return notasAluno;
	}
	
	public void setNomeAluno(String novoNome) {
		nomeAluno = novoNome;
	}
	
	public void setNota(int indiceNota, double valorNota) {
		notasAluno[indiceNota] = valorNota;
	}
	
	public double calcularMedia() {
		double soma = 0;
		for (double nota : notasAluno) {
			soma += nota;
		}
		return soma / notasAluno.length;
	}
}
