package lab04;

public class Aluno {
	private String nomeAluno;
	private int[] notasAluno;
	
	
	public Aluno(String nomeInicial, int qtdeNotas) {
		nomeAluno = nomeInicial;
		notasAluno = new int[qtdeNotas];
	}
	public String getNomeAluno() {
		return nomeAluno;
	}
	public int[] getNotasAluno() {
		return notasAluno;
	}
	
	public void setNomeAluno(String novoNome) {
		nomeAluno = novoNome;
	}
	
	public void setNota(int indiceNota, int valorNota) {
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
